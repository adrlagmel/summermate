package services;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utilities.Assert;

import repositories.MensajeRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Carpeta;
import domain.Mensaje;
import forms.MensajeForm;

@Service
@Transactional
public class MensajeService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private services.correo.Avisos avisos;

	@Autowired
	private MensajeRepository mensajeRepository;
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private CarpetaService carpetaService;
	
	@Autowired
	private ActorService actorService;
	
	// Constructor -----------------------------------------------------------
	
	public MensajeService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Mensaje create(){

		Mensaje mensaje = new Mensaje();

		Actor remitente = actorService.findByPrincipal();

		mensaje.setRemitente(remitente);
		mensaje.setFechaEnvio(LocalDateTime.now());

		Carpeta carpeta = carpetaService.findEnviadosActor(remitente);

		mensaje.setCarpeta(carpeta);

		return mensaje;

	}
	
	// Un formulario siempre crea un mensaje nuevo del usuario autenticado.
	public Mensaje reconstruct(MensajeForm form){
		Mensaje result = create();

		result.setBeneficiario(form.getBeneficiario());
		result.setAsunto(form.getAsunto());
		result.setCuerpo(form.getCuerpo());

		return result;
	}

	// Un mensaje nuevo se guarda dos veces: una copia en "Enviados" del
	// remitente y otra en "Entrada" del destinatario. Cada uno borra la suya
	// sin tocar la del otro.
	public void save(Mensaje mensaje){

		checkPrincipal(mensaje);
		Assert.isTrue(!mensaje.getBeneficiario().equals(mensaje.getRemitente()));

		if(mensaje.getId() != 0){
			mensajeRepository.save(mensaje);
			return;
		}

		Actor remitente = actorService.findByPrincipal();
		LocalDateTime fecha = LocalDateTime.now().minusSeconds(1);

		mensaje.setRemitente(remitente);
		mensaje.setFechaEnvio(fecha);
		mensaje.setCarpeta(carpetaService.findEnviadosActor(remitente));
		mensajeRepository.save(mensaje);

		Mensaje copia = new Mensaje();
		copia.setRemitente(remitente);
		copia.setBeneficiario(mensaje.getBeneficiario());
		copia.setAsunto(mensaje.getAsunto());
		copia.setCuerpo(mensaje.getCuerpo());
		copia.setFechaEnvio(fecha);
		copia.setCarpeta(carpetaService.findEntradaActor(mensaje.getBeneficiario()));
		mensajeRepository.save(copia);
		avisos.mensajeRecibido(copia);

	}
	// Cada actor solo ve las copias que están en sus carpetas.
	public Mensaje findOne(int mensajeId){

		Mensaje mensaje = mensajeRepository.findById(mensajeId).orElse(null);

		checkPropietario(mensaje);

		return mensaje;

	}

	private void checkPropietario(Mensaje mensaje) {
		Assert.notNull(mensaje);
		Assert.isTrue(mensaje.getCarpeta().getActor().getUserAccount().equals(LoginService.getPrincipal()));
	}

	public void delete(Mensaje mensaje){

		// Solo se borra la copia que está en una carpeta propia.
		checkPropietario(mensaje);

		mensajeRepository.delete(mensaje);

	}
	
	// Other business methods ------------------------------------------------

	
	public void checkPrincipal(Mensaje mensaje) {
		Assert.notNull(mensaje);
		
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(mensaje.getRemitente().getUserAccount().equals(userAccount) 
				|| mensaje.getBeneficiario().getUserAccount().equals(userAccount));
	}
}
