package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
public class MensajeServiceTest extends AbstractTest {

	@Autowired
	private MensajeService mensajeService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private CarpetaService carpetaService;

	@Test
	public void todosLosActoresTienenSusCarpetasPorDefecto() {
		for (Actor actor : actorService.findAll()) {
			assertNotNull(actor.getUserAccount().getUsername(), carpetaService.findEntradaActor(actor));
			assertNotNull(actor.getUserAccount().getUsername(), carpetaService.findEnviadosActor(actor));
		}
	}

	@Test
	public void unMensajeLlegaALaBandejaDeEntradaDelDestinatario() {
		authenticate("usuario1");
		Actor destinatario = actorService.findAllMessages().iterator().next();

		MensajeForm form = new MensajeForm();
		form.setBeneficiario(destinatario);
		form.setAsunto("Hola");
		form.setCuerpo("Prueba");
		Mensaje mensaje = mensajeService.reconstruct(form);
		mensajeService.save(mensaje);

		assertEquals(carpetaService.findEntradaActor(destinatario), mensaje.getCarpeta());
		assertEquals("usuario1", mensaje.getRemitente().getUserAccount().getUsername());
	}
}
