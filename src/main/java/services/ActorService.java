package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import forms.ActorEditPasswordForm;

@Service
@Transactional
public class ActorService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private ActorRepository actorRepository;
	
	// Supporting services ---------------------------------------------------
	
	
	// Constructor -----------------------------------------------------------
	
	public ActorService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Collection<Actor> findAll(){
		
		Collection<Actor> actors = actorRepository.findAll();
		Assert.notNull(actors);
		
		return actors;
	}
	
	public void savePassword(Actor actor){
		Assert.notNull(actor);
					
		String password = actor.getUserAccount().getPassword();
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(password, null);
		
		actor.getUserAccount().setPassword(password);
		
		actorRepository.save(actor);
				
	}
	
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
	
	
	public Actor reconstructPassword(ActorEditPasswordForm form) {
		
		Actor result = findByPrincipal();
		
		result.getUserAccount().setPassword(form.getPassword());
		
		return result;
	}

}
