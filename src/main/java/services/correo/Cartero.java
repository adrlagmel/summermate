/* Cartero.java
 *
 * Envía los avisos por correo (Fase 6) cuando la transacción que los generó
 * se ha confirmado: si la reserva falla, no sale ningún correo. Un fallo al
 * enviar se registra y no afecta al usuario.
 *
 * Con summermate.correo.activo=false (por defecto) los correos solo se
 * escriben en el log. En Docker Compose se envían a Mailpit
 * (http://localhost:8025) para verlos sin mandar nada fuera.
 */

package services.correo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class Cartero {

	private static final Logger			log			= LoggerFactory.getLogger(Cartero.class);

	private final ObjectProvider<JavaMailSender>	mailSender;
	private final boolean				activo;
	private final String				remitente;

	// Últimos correos "enviados", para los tests
	private final List<Correo>			enviados	= Collections.synchronizedList(new ArrayList<>());


	public Cartero(ObjectProvider<JavaMailSender> mailSender, @Value("${summermate.correo.activo:false}") boolean activo,
		@Value("${summermate.correo.remitente:no-responder@summermate.example}") String remitente) {
		this.mailSender = mailSender;
		this.activo = activo;
		this.remitente = remitente;
	}

	@TransactionalEventListener
	public void enviar(Correo correo) {
		enviados.add(correo);
		if (!activo) {
			log.info("Correo (sin enviar) para {}: {}", correo.para(), correo.asunto());
			return;
		}
		try {
			SimpleMailMessage mensaje = new SimpleMailMessage();
			mensaje.setFrom(remitente);
			mensaje.setTo(correo.para());
			mensaje.setSubject(correo.asunto());
			mensaje.setText(correo.cuerpo());
			mailSender.getObject().send(mensaje);
		} catch (RuntimeException fallo) {
			log.warn("No se pudo enviar el correo a {}: {}", correo.para(), fallo.getMessage());
		}
	}

	public List<Correo> enviados() {
		return enviados;
	}

}
