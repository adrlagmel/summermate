/* VistasAdvice.java
 *
 * Fase 5: lo común a todos los controladores.
 *
 * Errores: antes AbstractController capturaba cualquier excepción y mostraba
 * su mensaje y la traza de pila completa (misc/panic.jsp), lo que enseñaba
 * detalles internos a cualquiera. Ahora se muestra una página de error sin
 * detalles y la traza solo va al log.
 *
 * Las reglas de negocio de los servicios fallan con IllegalArgumentException
 * (utilities.Assert): un recurso ajeno, un id que no existe o una acción no
 * permitida. Se responde 403 sin distinguir entre esos casos, para no revelar
 * qué ids existen.
 */

package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class VistasAdvice {

	private static final Logger	log	= LoggerFactory.getLogger(VistasAdvice.class);

	@Autowired
	private Enlaces				enlaces;


	// Thymeleaf 3.1 no deja llamar a beans (@enlaces) dentro de fragmentos con
	// parámetros, así que se pasa como variable del modelo.
	@ModelAttribute("enlaces")
	public Enlaces enlaces() {
		return enlaces;
	}


	@ExceptionHandler(IllegalArgumentException.class)
	public ModelAndView noPermitido(IllegalArgumentException e, HttpServletRequest request) {
		log.info("Operación rechazada en {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
		return error(HttpStatus.FORBIDDEN, "fase5.error.403");
	}

	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ModelAndView conflicto(ObjectOptimisticLockingFailureException e) {
		return error(HttpStatus.CONFLICT, "fase5.error.409");
	}

	@ExceptionHandler({ NoResourceFoundException.class, MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class })
	public ModelAndView noEncontrado(Exception e) {
		return error(HttpStatus.NOT_FOUND, "fase5.error.404");
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView inesperado(Exception e, HttpServletRequest request) {
		log.error("Error inesperado en {} {}", request.getMethod(), request.getRequestURI(), e);
		return error(HttpStatus.INTERNAL_SERVER_ERROR, "fase5.error.500");
	}

	private static ModelAndView error(HttpStatus estado, String mensaje) {
		ModelAndView result = new ModelAndView("error");

		result.setStatus(estado);
		result.addObject("status", estado.value());
		result.addObject("mensajeError", mensaje);

		return result;
	}

}
