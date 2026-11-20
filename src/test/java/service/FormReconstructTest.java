package service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import services.DenunciaValoracionService;
import services.NegocioService;
import services.PeticionNegocioService;
import services.PlayaService;
import services.ValoracionNegocioService;
import services.ValoracionPlayaService;
import utilities.AbstractTest;
import domain.DenunciaValoracion;
import domain.Negocio;
import domain.PeticionNegocio;
import domain.Playa;
import domain.ValoracionNegocio;
import domain.ValoracionPlaya;
import domain.EstadoPeticion;
import domain.TipoDenuncia;
import forms.DenunciaValoracionForm;
import forms.NegocioForm;
import forms.PeticionNegocioResolucionForm;
import forms.PlayaForm;
import forms.ValoracionPlayaForm;

/*
 * Los controladores ya no enlazan entidades sino formularios (forms.*). Estos
 * tests comprueban que reconstruct() solo copia lo que el formulario declara,
 * que se mantiene el bloqueo optimista y que se comprueba el propietario.
 */
@Transactional
public class FormReconstructTest extends AbstractTest {

	@Autowired
	private NegocioService negocioService;

	@Autowired
	private PlayaService playaService;

	@Autowired
	private ValoracionPlayaService valoracionPlayaService;

	@Autowired
	private ValoracionNegocioService valoracionNegocioService;

	@Autowired
	private DenunciaValoracionService denunciaValoracionService;

	@Autowired
	private PeticionNegocioService peticionNegocioService;

	// Antes, editar un negocio borraba su imagen y permitía cambiar
	// negocioActivo o valoracionMedia añadiendo campos a la petición.
	@Test
	public void editarUnNegocioConservaLoQueNoEstaEnElFormulario() {
		authenticate("empresario1");
		Negocio negocio = negocio("Comida Fast");
		// Cabecera mínima de un PNG: basta para que ImagenService lo acepte
		byte[] imagen = { (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 1, 2, 3 };
		negocioService.addImageToNegocio(negocio.getId(), imagen);
		negocio.setValoracionMedia(4.5);

		NegocioForm form = new NegocioForm(negocio);
		form.setNombre("Comida Fast 2");
		negocioService.save(negocioService.reconstruct(form));

		Negocio guardado = negocioService.findOneToDisplay(negocio.getId());
		assertEquals("Comida Fast 2", guardado.getNombre());
		assertArrayEquals(imagen, guardado.getImagen().getDatos());
		assertEquals(Double.valueOf(4.5), guardado.getValoracionMedia());
		assertTrue(guardado.getNegocioActivo());
	}

	@Test
	public void unaVersionAntiguaDelFormularioSeRechaza() {
		assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
			authenticate("empresario1");
			NegocioForm form = new NegocioForm(negocio("Comida Fast"));
			form.setVersion(form.getVersion() - 1);

			negocioService.reconstruct(form);
	
		});
	}

	@Test
	public void unEmpresarioNoPuedeEditarElNegocioDeOtro() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("empresario1");
			NegocioForm form = new NegocioForm(negocio("Comida Fast"));
			authenticate("empresario2");

			negocioService.reconstruct(form);
	
		});
	}

	// Antes, editar una playa dejaba valoracionMedia a null.
	@Test
	public void editarUnaPlayaConservaSuValoracionMedia() {
		authenticate("admin");
		Playa playa = playa("Playa de Regla");

		PlayaForm form = new PlayaForm(playa);
		form.setDescripcion("Nueva descripción");
		playaService.save(playaService.reconstruct(form));

		Playa guardada = playaService.findOne(playa.getId());
		assertEquals("Nueva descripción", guardada.getDescripcion());
		assertEquals(Double.valueOf(8.0), guardada.getValoracionMedia());
	}

	@Test
	public void unUsuarioEditaSuValoracionDePlaya() {
		authenticate("usuario1");
		ValoracionPlaya valoracion = valoracionPlayaService.valoracionesDeUsuario().iterator().next();

		ValoracionPlayaForm form = new ValoracionPlayaForm(valoracion);
		form.setTitulo("Otro título");
		valoracionPlayaService.save(valoracionPlayaService.reconstruct(form));

		assertEquals("Otro título", valoracionPlayaService.findOne(valoracion.getId()).getTitulo());
	}

	// Antes no se comprobaba el propietario al editar ni al borrar.
	@Test
	public void unUsuarioNoPuedeEditarLaValoracionDeOtro() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("usuario1");
			int id = valoracionPlayaService.valoracionesDeUsuario().iterator().next().getId();
			authenticate("usuario2");

			valoracionPlayaService.findOneToEdit(id);
	
		});
	}

	@Test
	public void unUsuarioNoPuedeBorrarLaValoracionDeOtro() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("usuario1");
			ValoracionPlaya valoracion = valoracionPlayaService.valoracionesDeUsuario().iterator().next();
			authenticate("usuario2");

			valoracionPlayaService.delete(valoracion);
	
		});
	}

	@Test
	public void unEmpresarioNoPuedeEditarLaDenunciaDeOtro() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("empresario1");
			int id = denunciaValoracionService.denunciasDelEmpresario().iterator().next().getId();
			authenticate("empresario2");

			denunciaValoracionService.findOneToEdit(id);
	
		});
	}

	// valoracionNegocio2 es de "Comida Fast", que no es de empresario2.
	@Test
	public void soloSePuedenDenunciarValoracionesDeUnNegocioPropio() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("empresario2");
			DenunciaValoracionForm form = new DenunciaValoracionForm();
			form.setValoracionNegocio(valoracion("Falta de higiene!"));
			form.setTitulo("Título");
			form.setComentario("Comentario");
			form.setTipo(TipoDenuncia.BURLA);

			denunciaValoracionService.reconstruct(form);
	
		});
	}

	@Test
	public void unEmpresarioDenunciaUnaValoracionDeSuNegocio() {
		authenticate("empresario1");
		DenunciaValoracionForm form = new DenunciaValoracionForm();
		form.setValoracionNegocio(valoracion("Falta de higiene!"));
		form.setTitulo("Título");
		form.setComentario("Comentario");
		form.setTipo(TipoDenuncia.BURLA);

		DenunciaValoracion denuncia = denunciaValoracionService.reconstruct(form);
		denunciaValoracionService.save(denuncia);

		assertEquals("empresario1", denuncia.getCliente().getUserAccount().getUsername());
	}

	// El administrador solo puede escribir comentarios al resolver.
	@Test
	public void resolverUnaPeticionSoloCambiaLosComentarios() {
		authenticate("admin");
		PeticionNegocio peticion = peticionNegocioService.findPeticionNegocioPendiente().iterator().next();
		String titulo = peticion.getTitulo();

		PeticionNegocioResolucionForm form = new PeticionNegocioResolucionForm(peticion);
		form.setComentarios("Todo correcto");
		peticionNegocioService.accept(peticionNegocioService.reconstruct(form));

		PeticionNegocio guardada = peticionNegocioService.findOneToEdit(peticion.getId());
		assertEquals(EstadoPeticion.ACEPTADO, guardada.getEstado());
		assertEquals("Todo correcto", guardada.getComentarios());
		assertEquals(titulo, guardada.getTitulo());
	}

	private Negocio negocio(String nombre) {
		for (Negocio n : negocioService.findAll())
			if (n.getNombre().equals(nombre))
				return n;
		throw new AssertionError("No existe el negocio " + nombre);
	}

	private Playa playa(String nombre) {
		for (Playa p : playaService.findAllBeaches())
			if (p.getNombre().equals(nombre))
				return p;
		throw new AssertionError("No existe la playa " + nombre);
	}

	private ValoracionNegocio valoracion(String titulo) {
		for (Negocio n : negocioService.findAll())
			for (ValoracionNegocio v : valoracionNegocioService.valoracionesNegocioDeUnNegocio(n.getId()))
				if (v.getTitulo().equals(titulo))
					return v;
		throw new AssertionError("No existe la valoración " + titulo);
	}
}
