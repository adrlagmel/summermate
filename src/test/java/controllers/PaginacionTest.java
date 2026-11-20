package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/*
 * Traducción de los parámetros de displaytag (page empieza en 1, sort, dir) a
 * un Pageable de Spring Data. "sort" llega del navegador: solo se aceptan las
 * columnas ordenables de cada listado.
 */
public class PaginacionTest {

	@Test
	public void sinParametrosEsLaPrimeraPaginaConElOrdenPorDefecto() {
		Pageable p = new Paginacion().negocios();

		assertEquals(0, p.getPageNumber());
		assertEquals(Paginacion.TAMANO, p.getPageSize());
		assertEquals(Sort.by("nombre"), p.getSort());
	}

	@Test
	public void laPaginaDeDisplaytagEmpiezaEnUno() {
		Paginacion paginacion = new Paginacion();
		paginacion.setPage(3);

		assertEquals(2, paginacion.negocios().getPageNumber());
	}

	@Test
	public void unaPaginaInvalidaEsLaPrimera() {
		Paginacion paginacion = new Paginacion();
		paginacion.setPage(-4);

		assertEquals(0, paginacion.negocios().getPageNumber());
	}

	@Test
	public void ordenaPorUnaColumnaPermitida() {
		Paginacion paginacion = new Paginacion();
		paginacion.setSort("aforo");
		paginacion.setDir("desc");

		assertEquals(Sort.by(Sort.Direction.DESC, "aforo"), paginacion.negocios().getSort());
	}

	@Test
	public void ignoraColumnasNoPermitidas() {
		Paginacion paginacion = new Paginacion();
		paginacion.setSort("empresario.userAccount.password");

		assertEquals(Sort.by("nombre"), paginacion.negocios().getSort());
		assertNull(paginacion.negocios().getSort().getOrderFor("empresario.userAccount.password"));
	}
}
