package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import services.NegocioService;
import utilities.AbstractTest;
import domain.Negocio;

/*
 * Tests de caracterización de NegocioService sobre los datos de
 * PopulateDatabase.xml.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
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
	@Test(expected = IllegalArgumentException.class)
	public void empresarioConPeticionPendienteNoPuedeCrearNegocio() {
		authenticate("empresario3");

		negocioService.create();
	}

	// "Comida Fast" es de empresario1.
	@Test(expected = IllegalArgumentException.class)
	public void empresarioNoPuedeEditarNegocioAjeno() {
		int id = buscar("Comida Fast").getId();
		authenticate("empresario2");

		negocioService.findOneToEdit(id);
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
