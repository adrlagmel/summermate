package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MensajeRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Carpeta;
import domain.Mensaje;

@Service
@Transactional
public class MensajeService {
	
	// Managed repository ----------------------------------------------------
	
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
		mensaje.setFechaEnvio(new Date());

		Carpeta carpeta = carpetaService.findEnviadosActor(remitente);

		mensaje.setCarpeta(carpeta);

		return mensaje;

	}
	
	public void save(Mensaje mensaje){

		checkPrincipal(mensaje);
		Assert.isTrue(!mensaje.getBeneficiario().equals(mensaje.getRemitente()));
		
		
		if(mensaje.getId() != 0)
			mensajeRepository.save(mensaje);

		if(mensaje.getId() == 0){
			
			Actor remitente = actorService.findByPrincipal();

			mensaje.setRemitente(remitente);
			Carpeta outbox = carpetaService.findEnviadosActor(remitente);
			mensaje.setCarpeta(outbox);

			Date d = new Date(System.currentTimeMillis() - 1000);

			mensaje.setFechaEnvio(d);
			
			mensajeRepository.save(mensaje);
			
			Mensaje mensajeToSend = mensaje;
			Actor beneficiario = mensaje.getBeneficiario();
			Carpeta carpeta = carpetaService.findEntradaActor(beneficiario);
			mensajeToSend.setCarpeta(carpeta);

			mensajeRepository.save(mensajeToSend);

		}

	}
	
	public Mensaje findOne(int mensajeId){

		Mensaje mensaje = mensajeRepository.findOne(mensajeId);

		checkPrincipal(mensaje);

		return mensaje;

	}
	
	public void delete(Mensaje mensaje){

		checkPrincipal(mensaje);

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
