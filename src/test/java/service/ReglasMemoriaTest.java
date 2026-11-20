package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;
import services.NegocioService;
import services.PeticionNegocioService;
import services.PlayaService;
import services.ValoracionNegocioService;
import utilities.AbstractTest;
import utilities.validators.NifCifValidator;
import domain.Evento;
import domain.PeticionNegocio;

/*
 * Fase 6: reglas de la memoria del TFG que el código no cumplía. Cada test
 * corresponde a una fila del informe "memoria frente a código".
 */
@Transactional
public class ReglasMemoriaTest extends AbstractTest {

	@Autowired
	private PlayaService				playaService;

	@Autowired
	private PeticionNegocioService		peticionNegocioService;

	@Autowired
	private ValoracionNegocioService	valoracionNegocioService;

	@Autowired
	private NegocioService				negocioService;

	@Autowired
	private Validator					validator;

	@PersistenceContext
	private EntityManager				em;

	// CRQ-0008: una playa con valoraciones no se borra (antes se borraban en cascada).
	@Test
	public void noSeBorraUnaPlayaConValoraciones() {
		em.createNativeQuery("insert into valoracionplaya (id, version, titulo, fecha, puntuacion, comentario, usuario_id, playa_id) "
			+ "values (900, 0, 'T', now(), 5, 'C', 10, 3)").executeUpdate();
		em.clear();

		assertThrows(IllegalArgumentException.class, () -> playaService.delete(playaService.findOne(3)));
	}

	@Test
	public void seBorraUnaPlayaSinNegociosNiValoraciones() {
		playaService.delete(playaService.findOne(3));
		em.flush();
		assertEquals(null, playaService.findOne(3));
	}

	// Una petición rechazada ya no se puede aceptar.
	@Test
	public void unaPeticionRechazadaNoSePuedeAceptar() {
		authenticate("administrador2");
		PeticionNegocio rechazada = peticionNegocioService.findOneToEdit(4);

		assertThrows(IllegalArgumentException.class, () -> peticionNegocioService.accept(rechazada));
	}

	// La media se recalcula sin la valoración borrada: Comida Fast tiene un 7 y un 3.
	@Test
	public void laMediaSeRecalculaTrasBorrarUnaValoracion() {
		authenticate("usuario2");
		valoracionNegocioService.delete(valoracionNegocioService.findOne(2));
		em.flush();
		em.clear();

		assertEquals(7.0, negocioService.findOneToDisplay(1).getValoracionMedia(), 0.001);
	}

	@Test
	public void sinValoracionesLaMediaEsCero() {
		authenticate("usuario1");
		valoracionNegocioService.delete(valoracionNegocioService.findOne(1));
		authenticate("usuario2");
		valoracionNegocioService.delete(valoracionNegocioService.findOne(2));
		em.flush();
		em.clear();

		assertEquals(0.0, negocioService.findOneToDisplay(1).getValoracionMedia(), 0.001);
	}

	// El patrón de Authority no tenía paréntesis y aceptaba "ADMINISTRADORxyz".
	@Test
	public void laAutoridadSoloAdmiteLosTresRoles() {
		Authority mala = new Authority();
		mala.setAuthority("ADMINISTRADORxyz");
		Authority buena = new Authority();
		buena.setAuthority("EMPRESARIO");

		assertFalse(validator.validate(mala).isEmpty());
		assertTrue(validator.validate(buena).isEmpty());
	}

	@Test
	public void elPrecioDeUnEventoNoPuedeSerNegativo() {
		Evento evento = new Evento();
		evento.setPrecio(new BigDecimal("-1.00"));
		Set<ConstraintViolation<Evento>> violaciones = validator.validateProperty(evento, "precio");
		assertFalse(violaciones.isEmpty());

		evento.setPrecio(new BigDecimal("12.50"));
		assertTrue(validator.validateProperty(evento, "precio").isEmpty());
	}

	@Test
	public void cifNifYNie() {
		assertTrue(NifCifValidator.esValido("B12345674"));
		assertTrue(NifCifValidator.esValido("P1568656A"));
		assertTrue(NifCifValidator.esValido("12345678Z"));
		assertTrue(NifCifValidator.esValido("X1234567L"));
		assertFalse(NifCifValidator.esValido("B12345678"));
		assertFalse(NifCifValidator.esValido("12345678A"));
		assertFalse(NifCifValidator.esValido("B99"));
	}
}
