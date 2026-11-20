/* Avisos.java
 *
 * Redacta los avisos por correo en el idioma de la petición y los publica.
 *   - Reserva confirmada: al usuario.
 *   - Petición de negocio aceptada o rechazada: al empresario.
 *   - Mensaje nuevo: al destinatario (sin el texto, solo el aviso).
 */

package services.correo;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import domain.EstadoPeticion;
import domain.Mensaje;
import domain.PeticionNegocio;
import domain.Reserva;

@Component
public class Avisos {

	private static final DateTimeFormatter	FECHA	= DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private final ApplicationEventPublisher	publisher;
	private final MessageSource				mensajes;
	private final String					urlBase;


	public Avisos(ApplicationEventPublisher publisher, MessageSource mensajes, @Value("${summermate.correo.url-base:http://localhost:8080}") String urlBase) {
		this.publisher = publisher;
		this.mensajes = mensajes;
		this.urlBase = urlBase;
	}

	public void reservaConfirmada(Reserva r) {
		String mesa = r.getMesa() == null ? "" : texto("correo.reserva.mesa", r.getMesa().getNombre());
		publicar(r.getUsuario().getEmail(), texto("correo.reserva.asunto", r.getNegocio().getNombre()),
			texto("correo.reserva.cuerpo", r.getUsuario().getNombre(), r.getNegocio().getNombre(), r.getFecha().format(FECHA), r.getComensales(), mesa, r.getCodigo(), urlBase + "/mis/reservas"));
	}

	public void peticionResuelta(PeticionNegocio p) {
		boolean aceptada = p.getEstado() == EstadoPeticion.ACEPTADO;
		publicar(p.getEmpresario().getEmail(), texto(aceptada ? "correo.peticion.aceptada.asunto" : "correo.peticion.rechazada.asunto"),
			texto(aceptada ? "correo.peticion.aceptada.cuerpo" : "correo.peticion.rechazada.cuerpo", p.getEmpresario().getNombre(), p.getTitulo(),
				p.getComentarios() == null ? "" : p.getComentarios(), urlBase + "/empresario/peticion"));
	}

	public void mensajeRecibido(Mensaje m) {
		publicar(m.getBeneficiario().getEmail(), texto("correo.mensaje.asunto", m.getRemitente().getNombre()),
			texto("correo.mensaje.cuerpo", m.getBeneficiario().getNombre(), m.getRemitente().getNombre(), m.getAsunto(), urlBase + "/carpetas"));
	}

	private void publicar(String para, String asunto, String cuerpo) {
		publisher.publishEvent(new Correo(para, asunto, cuerpo));
	}

	private String texto(String clave, Object... args) {
		Locale locale = LocaleContextHolder.getLocale();
		return mensajes.getMessage(clave, args, locale);
	}

}
