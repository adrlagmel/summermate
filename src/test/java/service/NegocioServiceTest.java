package service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import services.NegocioService;
import utilities.AbstractTest;
import domain.Negocio;

/*
 * Tests de caracterización de NegocioService sobre los datos de
 * PopulateDatabase.xml.
 */
@Transactional
public class NegocioServiceTest extends AbstractTest {

	@Autowired
	private NegocioService negocioService;

	// En MySQL "like" no distinguía mayúsculas; en PostgreSQL sí, así que la
	// consulta usa lower() para conservar el comportamiento.
	@Test
	public void buscarPorCiudadNoDistingueMayusculas() {
		assertEquals(nombres("El Faro", "La vie est bella"), nombres(negocioService.searchByTown("chipiona")));
		assertEquals(nombres("El Faro", "La vie est bella"), nombres(negocioService.searchByTown("CHIPIONA")));
	}

	@Test
	public void buscarPorCiudadAdmiteTextoParcial() {
		assertEquals(nombres("Heladeria Fran"), nombres(negocioService.searchByTown("cad")));
		assertTrue(negocioService.searchByTown("Ciudad que no existe").isEmpty());
	}

	@Test
	public void empresarioAceptadoPuedeCrearNegocio() {
		authenticate("empresario1");

		Negocio negocio = negocioService.create();

		assertNotNull(negocio.getEmpresario());
		assertTrue(negocio.getNegocioActivo());
	}

	// empresario3 tiene la petición de negocio PENDIENTE.
	@Test
	public void empresarioConPeticionPendienteNoPuedeCrearNegocio() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("empresario3");

			negocioService.create();
	
		});
	}

	// "Comida Fast" es de empresario1.
	@Test
	public void empresarioNoPuedeEditarNegocioAjeno() {
		assertThrows(IllegalArgumentException.class, () -> {
			int id = buscar("Comida Fast").getId();
			authenticate("empresario2");

			negocioService.findOneToEdit(id);
	
		});
	}

	private Negocio buscar(String nombre) {
		for (Negocio n : negocioService.findAll())
			if (n.getNombre().equals(nombre))
				return n;
		throw new AssertionError("No existe el negocio " + nombre);
	}

	private static Set<String> nombres(String... nombres) {
		Set<String> result = new HashSet<String>();
		for (String n : nombres)
			result.add(n);
		return result;
	}

	private static Set<String> nombres(Collection<Negocio> negocios) {
		Set<String> result = new HashSet<String>();
		for (Negocio n : negocios)
			result.add(n.getNombre());
		return result;
	}
}
