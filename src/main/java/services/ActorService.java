package services;

import java.util.Collection;



import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private ActorRepository actorRepository;
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private AdministradorService administradorService;
	
	// Constructor -----------------------------------------------------------
	
	public ActorService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Collection<Actor> findAll(){
		
		Collection<Actor> actors = actorRepository.findAll();
		Assert.notNull(actors);
		
		return actors;
	}
	
//	public Collection<Actor> actorBlockedByMoreUsers(){
//		administradorService.checkPrincipal();
//		Collection<Actor> actors = actorRepository.actorBlockedByMoreUsers();
//		Assert.notNull(actors);
//		
//		return actors;
//	}
	
//	public Collection<Actor> actorWhoHasSentMoreMessages(){
//		administradorService.checkPrincipal();
//		Collection<Actor> actors = actorRepository.actorWhoHasSentMoreMessages();
//		Assert.notNull(actors);
//		
//		return actors;
//	}
//	
//	public Collection<Actor> actorWhoHasReceivedMoreMessages(){
//		administradorService.checkPrincipal();
//		Collection<Actor> actors = actorRepository.actorWhoHasReceivedMoreMessages();
//		Assert.notNull(actors);
//		
//		return actors;
//	}
	
	// Other business methods ------------------------------------------------
	
	public Actor findByUserAccount(int id){
		Assert.isTrue(id != 0);
		 Actor res;
		 
		 res = this.actorRepository.findByUserAccountId(id);
		 Assert.notNull(res);
		 
		 return res;
	}
	
	public Actor findByPrincipal(){
		
		UserAccount principalAccount = LoginService.getPrincipal();
		
		Actor principal = findByUserAccount(principalAccount.getId());
		return principal;		
	}
	
//	public Collection<Actor> findBlockedByActors(){
//		Actor actor = findByPrincipal();
//		
//		Collection<Actor> actors = actorRepository.findBlockedByActors(actor.getId());
//		Assert.notNull(actors);
//		
//		return actors;
//	}
//	
//	public Collection<Actor> findBlockedActors(){
//		Actor actor = findByPrincipal();
//		
//		Collection<Actor> actors = actorRepository.findBlockedActors(actor.getId());
//		Assert.notNull(actors);
//		
//		return actors;
//	}

}
