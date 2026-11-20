package service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import services.NegocioService;
import services.PlayaService;
import utilities.AbstractTest;
import domain.DomainEntity;
import domain.Negocio;
import domain.Playa;

/*
 * Consultas del dashboard del administrador, reescritas con GROUP BY en la
 * Fase 3. Datos de PopulateDatabase.xml: "Comida Fast" y "Heladeria Fran"
 * tienen 2 reservas cada uno; Matalascañas y Playa de Regla, 1 valoración.
 */
@Transactional
public class DashboardTest extends AbstractTest {

	@Autowired
	private NegocioService	negocioService;

	@Autowired
	private PlayaService	playaService;

	@Test
	public void negociosConMasReservasIncluyeLosEmpates() {
		authenticate("admin");

		assertEquals(set("Comida Fast", "Heladeria Fran"), nombres(negocioService.negocioConMasReservas()));
		assertEquals(set("Comida Fast", "Heladeria Fran"), nombres(negocioService.negociosConMasReservas()));
	}

	@Test
	public void playasConMasValoraciones() {
		assertEquals(set("Matalascañas", "Playa de Regla"), nombres(playaService.findPlayaMasComentarios()));
		assertEquals(Integer.valueOf(1), playaService.masValoraciones());
	}

	private static Set<String> set(String... nombres) {
		return new HashSet<String>(Arrays.asList(nombres));
	}

	private static Set<String> nombres(Collection<? extends DomainEntity> entidades) {
		Set<String> result = new HashSet<String>();
		for (DomainEntity e : entidades)
			result.add(e instanceof Negocio ? ((Negocio) e).getNombre() : ((Playa) e).getNombre());
		return result;
	}
}
