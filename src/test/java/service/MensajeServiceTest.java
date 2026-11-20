package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import services.ActorService;
import services.CarpetaService;
import services.MensajeService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Mensaje;
import forms.MensajeForm;

/*
 * Envío de mensajes. Tras pasar a PostgreSQL dejó de funcionar sin que nada
 * avisara: los datos de ejemplo llamaban "entrada" a la bandeja que el código
 * busca como "Entrada" (MySQL no distinguía mayúsculas al comparar).
 */
@Transactional
public class MensajeServiceTest extends AbstractTest {

	@Autowired
	private MensajeService mensajeService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private CarpetaService carpetaService;

	@PersistenceContext
	private EntityManager em;

	private long enEntrada(Actor actor, String asunto) {
		return em.createQuery("select count(m) from Mensaje m where m.carpeta = :c and m.asunto = :a", Long.class)
			.setParameter("c", carpetaService.findEntradaActor(actor)).setParameter("a", asunto).getSingleResult();
	}

	@Test
	public void todosLosActoresTienenSusCarpetasPorDefecto() {
		for (Actor actor : actorService.findAll()) {
			assertNotNull(carpetaService.findEntradaActor(actor), actor.getUserAccount().getUsername());
			assertNotNull(carpetaService.findEnviadosActor(actor), actor.getUserAccount().getUsername());
		}
	}

	@Test
	public void unMensajeQuedaEnEnviadosYEnLaEntradaDelDestinatario() {
		authenticate("usuario1");
		Actor destinatario = actorService.findAllMessages().iterator().next();

		MensajeForm form = new MensajeForm();
		form.setBeneficiario(destinatario);
		form.setAsunto("Hola");
		form.setCuerpo("Prueba");
		Mensaje mensaje = mensajeService.reconstruct(form);
		mensajeService.save(mensaje);

		Actor remitente = actorService.findByPrincipal();
		assertEquals(carpetaService.findEnviadosActor(remitente), mensaje.getCarpeta());
		assertEquals(1L, enEntrada(destinatario, "Hola"));
		assertEquals("usuario1", mensaje.getRemitente().getUserAccount().getUsername());
	}

	@Test
	public void borrarLaCopiaEnviadaNoBorraLaDelDestinatario() {
		authenticate("usuario1");
		Actor destinatario = actorService.findAllMessages().iterator().next();

		MensajeForm form = new MensajeForm();
		form.setBeneficiario(destinatario);
		form.setAsunto("Borrable");
		form.setCuerpo("Prueba");
		Mensaje enviado = mensajeService.reconstruct(form);
		mensajeService.save(enviado);
		mensajeService.delete(enviado);

		assertEquals(1L, enEntrada(destinatario, "Borrable"));
	}
}
