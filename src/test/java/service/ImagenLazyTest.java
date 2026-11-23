package service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import services.NegocioService;
import utilities.AbstractTest;
import domain.Negocio;

/*
 * Las imágenes están en su propia tabla y se cargan de forma perezosa: un
 * listado de negocios no lee sus bytes hasta que alguien los pide.
 */
@Transactional
public class ImagenLazyTest extends AbstractTest {

	private static final byte[]	PNG	= { (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 42 };

	@Autowired
	private NegocioService		negocioService;

	@PersistenceContext
	private EntityManager		entityManager;

	@Test
	public void listarNegociosNoCargaSusImagenes() {
		authenticate("empresario1");
		int id = negocioConImagen();

		// Se vacía la caché de primer nivel para leer de verdad de la base de datos
		entityManager.flush();
		entityManager.clear();

		for (Negocio negocio : negocioService.findAll())
			if (negocio.getId() == id) {
				assertNotNull(negocio.getImagen());
				assertFalse(Hibernate.isInitialized(negocio.getImagen()));

				assertArrayEquals(PNG, negocio.getImagen().getDatos());
				assertTrue(Hibernate.isInitialized(negocio.getImagen()));
				return;
			}
		throw new AssertionError("No se encontró el negocio");
	}

	@Test
	public void cambiarLaImagenBorraLaAnterior() {
		authenticate("empresario1");
		int id = negocioConImagen();
		// Como en la aplicación, cada subida es una petición (y transacción) distinta
		entityManager.flush();
		entityManager.clear();

		negocioService.addImageToNegocio(id, PNG);
		entityManager.flush();

		Number imagenes = (Number) entityManager.createQuery("select count(i) from Imagen i").getSingleResult();
		org.junit.jupiter.api.Assertions.assertEquals(1, imagenes.intValue());
	}

	private int negocioConImagen() {
		Negocio negocio = negocioService.findByEmpresario().iterator().next();
		negocioService.addImageToNegocio(negocio.getId(), PNG);
		return negocio.getId();
	}
}
