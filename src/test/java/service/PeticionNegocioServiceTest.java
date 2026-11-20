package service;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import services.PeticionNegocioService;
import utilities.AbstractTest;
import domain.EstadoPeticion;
import domain.PeticionNegocio;

/*
 * Estados de las peticiones de negocio como enum (EstadoPeticion).
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
public class PeticionNegocioServiceTest extends AbstractTest {

	@Autowired
	private PeticionNegocioService	peticionNegocioService;

	@PersistenceContext
	private EntityManager			entityManager;

	// En PopulateDatabase.xml hay dos pendientes: las de empresario3 y empresario6.
	@Test
	public void listaLasPeticionesPendientes() {
		authenticate("admin");
		Set<String> empresarios = new HashSet<String>();

		for (PeticionNegocio p : peticionNegocioService.findPeticionNegocioPendiente()) {
			assertEquals(EstadoPeticion.PENDIENTE, p.getEstado());
			empresarios.add(p.getEmpresario().getUserAccount().getUsername());
		}

		assertEquals(new HashSet<String>(java.util.Arrays.asList("empresario3", "empresario6")), empresarios);
	}

	// @Enumerated(STRING): en la base de datos sigue habiendo texto legible.
	@Test
	public void elEstadoSeGuardaComoTexto() {
		Object estados = entityManager.createNativeQuery("select string_agg(distinct estado, ',' order by estado) from peticionnegocio").getSingleResult();

		assertEquals("ACEPTADO,PENDIENTE,RECHAZADO", estados);
	}
}
