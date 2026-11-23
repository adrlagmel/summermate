/* ApiErrores.java
 *
 * Errores de la API como problem+json (RFC 9457), en lugar de las páginas de
 * error de la web:
 *   400  datos no válidos (con "errores": campo -> mensaje)
 *   401  credenciales o token de renovación no válidos
 *   403  regla de negocio o recurso ajeno (las IllegalArgumentException de los servicios)
 *   404  no existe
 *   409  la reserva no cabe ("motivo" y, si es por aforo, "plazasLibres"),
 *        o alguien cambió el dato a la vez (control de versión)
 *   429  demasiados intentos de inicio de sesión
 */

package api;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import services.AgendaService;

@RestControllerAdvice(basePackages = "api")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiErrores {

	public static class NoEncontrado extends RuntimeException {

		private static final long serialVersionUID = 1L;
	}


	private static ProblemDetail problema(HttpStatus estado, String titulo, String detalle) {
		ProblemDetail p = ProblemDetail.forStatusAndDetail(estado, detalle);
		p.setTitle(titulo);
		p.setType(URI.create("about:blank"));
		return p;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail invalido(MethodArgumentNotValidException e) {
		Map<String, String> errores = new LinkedHashMap<>();
		e.getBindingResult().getFieldErrors().forEach(f -> errores.putIfAbsent(f.getField(), f.getDefaultMessage()));
		ProblemDetail p = problema(HttpStatus.BAD_REQUEST, "Datos no válidos", "Revisa los campos marcados");
		p.setProperty("errores", errores);
		return p;
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class })
	public ProblemDetail malFormado(Exception e) {
		return problema(HttpStatus.BAD_REQUEST, "Petición mal formada", "El cuerpo o los parámetros no tienen el formato esperado");
	}

	@ExceptionHandler(ApiTokens.NoAutorizado.class)
	public ProblemDetail noAutorizado(ApiTokens.NoAutorizado e) {
		return problema(HttpStatus.UNAUTHORIZED, "No autorizado", e.getMessage());
	}

	@ExceptionHandler(ApiAuthController.DemasiadosIntentos.class)
	public ProblemDetail demasiados() {
		return problema(HttpStatus.TOO_MANY_REQUESTS, "Demasiados intentos", "Espera 15 minutos antes de volver a intentarlo");
	}

	@ExceptionHandler(NoEncontrado.class)
	public ProblemDetail noEncontrado() {
		return problema(HttpStatus.NOT_FOUND, "No encontrado", "No existe");
	}

	@ExceptionHandler(AgendaService.ReservaRechazada.class)
	public ProblemDetail reservaRechazada(AgendaService.ReservaRechazada e) {
		ProblemDetail p = problema(HttpStatus.CONFLICT, "La reserva no cabe", switch (e.motivo) {
			case AFORO -> "No quedan plazas suficientes a esa hora";
			case SIN_MESA -> "No queda ninguna mesa libre para tantas personas a esa hora";
			case CERRADO -> "El negocio cierra ese día";
			case FUERA_DE_HORARIO -> "Esa hora está fuera del horario del negocio";
			case PASADA -> "La fecha ya ha pasado";
		});
		p.setProperty("motivo", e.motivo.name());
		if (e.motivo == AgendaService.Motivo.AFORO)
			p.setProperty("plazasLibres", e.libres);
		return p;
	}

	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	public ProblemDetail conflicto() {
		return problema(HttpStatus.CONFLICT, "Conflicto", "Otra persona ha cambiado este dato a la vez; vuelve a cargarlo");
	}

	@ExceptionHandler({ IllegalArgumentException.class, NullPointerException.class })
	public ProblemDetail noPermitido(RuntimeException e) {
		return problema(HttpStatus.FORBIDDEN, "No permitido", "La operación no está permitida o el recurso no es tuyo");
	}

}
