/* Paginacion.java
 *
 * Fase 3: los listados cargaban todas las filas y displaytag mostraba 5 en
 * memoria. Ahora se pide a la base de datos solo la página que se ve.
 *
 * Spring rellena este objeto con los parámetros que genera displaytag al
 * paginar u ordenar (configurados en displaytag.properties):
 *   page  número de página, empezando en 1
 *   sort  propiedad por la que ordenar (la de la columna pulsada)
 *   dir   asc o desc
 * Basta con declararlo como parámetro del método del controlador.
 */

package controllers;

import java.util.Arrays;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Paginacion {

	public static final int	TAMANO	= 10;

	// Columnas ordenables de cada listado (las que tienen sortable="true" en
	// la JSP) y orden por defecto.
	public static final String[]	PLAYAS				= { "nombre", "localizacion.provincia", "localizacion.ciudad", "valoracionMedia" };
	public static final Sort		PLAYAS_ORDEN		= Sort.by("nombre");
	public static final String[]	NEGOCIOS			= { "nombre", "playa.localizacion.ciudad", "playa.nombre", "aforo", "valoracionMedia" };
	public static final Sort		NEGOCIOS_ORDEN		= Sort.by("nombre");
	public static final String[]	EVENTOS				= { "nombre", "negocio.playa.localizacion.provincia", "negocio.playa.localizacion.ciudad", "negocio.playa.nombre" };
	public static final Sort		EVENTOS_ORDEN		= Sort.by("fechaCelebracion");
	public static final String[]	RESERVAS			= { "fechaCreacion", "fecha", "comensales", "negocio.nombre" };
	public static final Sort		RESERVAS_ORDEN		= Sort.by("fecha");
	public static final String[]	ACTORES				= { "nombre", "apellidos", "email" };
	public static final Sort		ACTORES_ORDEN		= Sort.by("apellidos", "nombre");

	private Integer			page;
	private String			sort;
	private String			dir;


	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	// Solo se ordena por las propiedades permitidas: el parámetro "sort" llega
	// del navegador y acaba en la consulta.
	public Pageable playas() {
		return pageable(PLAYAS_ORDEN, PLAYAS);
	}

	public Pageable negocios() {
		return pageable(NEGOCIOS_ORDEN, NEGOCIOS);
	}

	public Pageable eventos() {
		return pageable(EVENTOS_ORDEN, EVENTOS);
	}

	public Pageable reservas() {
		return pageable(RESERVAS_ORDEN, RESERVAS);
	}

	public Pageable actores() {
		return pageable(ACTORES_ORDEN, ACTORES);
	}

	public Pageable pageable(Sort porDefecto, String... ordenables) {
		int numero = page == null || page < 1 ? 0 : page - 1;
		Sort orden = porDefecto;

		if (sort != null && Arrays.asList(ordenables).contains(sort))
			orden = Sort.by("desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC, sort);

		return PageRequest.of(numero, TAMANO, orden);
	}

}
