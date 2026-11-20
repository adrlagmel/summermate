/* Enlaces.java
 *
 * Fase 5: construye los enlaces de paginación y ordenación de las tablas a
 * partir de la URL actual, conservando el resto de parámetros (filtros de
 * búsqueda). Las plantillas lo usan como ${enlaces.pagina(2)} (VistasAdvice lo añade al modelo).
 * Sustituye a lo que hacía displaytag con su PaginatedList.
 */

package controllers;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

@Component("enlaces")
public class Enlaces {

	public String pagina(int numero) {
		return actual().replaceQueryParam("page", numero).build().toUriString();
	}

	// Números de página que se muestran: la actual y dos a cada lado
	public List<Integer> ventana(Page<?> pagina) {
		int actual = pagina.getNumber() + 1;
		int desde = Math.max(1, actual - 2);
		int hasta = Math.min(pagina.getTotalPages(), actual + 2);

		return IntStream.rangeClosed(desde, hasta).boxed().toList();
	}

	// Pulsar en una columna ordena por ella; pulsar otra vez invierte el orden.
	public String orden(String campo) {
		String dir = campo.equals(param("sort")) && !"desc".equalsIgnoreCase(param("dir")) ? "desc" : "asc";

		return actual().replaceQueryParam("page").replaceQueryParam("sort", campo).replaceQueryParam("dir", dir).build().toUriString();
	}

	public String icono(String campo) {
		if (!campo.equals(param("sort")))
			return "bi-arrow-down-up text-body-tertiary";
		return "desc".equalsIgnoreCase(param("dir")) ? "bi-sort-down" : "bi-sort-up";
	}

	private static UriComponentsBuilder actual() {
		// Solo ruta y parámetros: los enlaces no dependen del host
		UriComponentsBuilder b = ServletUriComponentsBuilder.fromCurrentRequest();
		return UriComponentsBuilder.fromPath(b.build().getPath()).query(b.build().getQuery());
	}

	private static String param(String nombre) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getParameter(nombre);
	}

}
