package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CarpetaRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Carpeta;
import domain.Mensaje;

@Service
@Transactional
public class CarpetaService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private CarpetaRepository carpetaRepository;
	
	// Supporting services ---------------------------------------------------
	
	// Constructor -----------------------------------------------------------
	
	public CarpetaService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Carpeta create(){
		Carpeta carpeta = new Carpeta();

		carpeta.setMensajes(new ArrayList<Mensaje>());
		
		return carpeta;
	}
	
	
	public void save(Carpeta carpeta){
		
		carpetaRepository.save(carpeta);
	}
	
	public Carpeta findOne(int carpetaId){

		Carpeta carpeta = carpetaRepository.findOne(carpetaId);

		checkPrincipal(carpeta);

		return carpeta;

	}
	
	
	// Other business methods ------------------------------------------------
	
	public void createDefaultCarpetas(Actor actor) {
		Carpeta inbox = create();
		Carpeta outbox = create();
		Carpeta trashbox = create();

		inbox.setNombre("Entrada");
		inbox.setActor(actor);
		save(inbox);

		outbox.setNombre("Enviados");
		outbox.setActor(actor);
		save(outbox);                

		trashbox.setNombre("Papelera");
		trashbox.setActor(actor);
		save(trashbox);
		
       }

	public void checkPrincipal(Carpeta carpeta) {
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(carpeta.getActor().getUserAccount().equals(userAccount));
		
	}


	public Carpeta findEntradaActor(Actor actor){

		Assert.notNull(actor);

		Carpeta carpeta = carpetaRepository.entradaCarpeta(actor.getId());

		Assert.notNull(carpeta);

		return carpeta;

	}

	

	public Carpeta findEnviadosActor(Actor actor){
		Assert.notNull(actor);

		Carpeta carpeta = carpetaRepository.salidaCarpeta(actor.getId());
		Assert.notNull(carpeta);

		return carpeta;
	}

	
	public Collection<Carpeta> findCarpetasByActor(Actor actor) {
		Assert.notNull(actor);

		Collection<Carpeta> carpetas = carpetaRepository.findCarpetasByActor(actor.getId());
		Assert.notNull(carpetas);

		return carpetas;

	}
	
}
