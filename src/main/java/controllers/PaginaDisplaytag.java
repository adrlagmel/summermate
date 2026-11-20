/* PaginaDisplaytag.java
 *
 * Adapta una página de Spring Data (Page) a la interfaz que entiende
 * displaytag para paginar fuera de memoria: con esto, <display:table> pinta
 * los enlaces de página y de ordenación pero no ordena ni corta la lista.
 */

package controllers;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public class PaginaDisplaytag<T> implements PaginatedList {

	private final Page<T>	page;


	public PaginaDisplaytag(Page<T> page) {
		this.page = page;
	}

	@Override
	public List<T> getList() {
		return page.getContent();
	}

	@Override
	public int getPageNumber() {
		return page.getNumber() + 1;
	}

	@Override
	public int getObjectsPerPage() {
		return page.getSize();
	}

	@Override
	public int getFullListSize() {
		return (int) page.getTotalElements();
	}

	@Override
	public String getSortCriterion() {
		Sort.Order orden = primerOrden();
		return orden == null ? null : orden.getProperty();
	}

	@Override
	public SortOrderEnum getSortDirection() {
		Sort.Order orden = primerOrden();
		return orden != null && orden.isDescending() ? SortOrderEnum.DESCENDING : SortOrderEnum.ASCENDING;
	}

	// Algunas JSP comprueban si el listado está vacío (${negocios.isEmpty()})
	public boolean isEmpty() {
		return page.isEmpty();
	}

	@Override
	public String getSearchId() {
		return null;
	}

	private Sort.Order primerOrden() {
		for (Sort.Order orden : page.getSort())
			return orden;
		return null;
	}

}
