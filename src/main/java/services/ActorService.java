package services;

import java.util.Collection;


import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utilities.Assert;

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

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	// Constructor -----------------------------------------------------------
	
	public ActorService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Collection<Actor> findAll(){
		
		Collection<Actor> actors = actorRepository.findAll();
		Assert.notNull(actors);
		
		return actors;
	}
	
	public Collection<Actor> findAllMessages(){
		
		Collection<Actor> actors = actorRepository.findAllMessages(findByPrincipal().getUserAccount().getId());
		Assert.notNull(actors);
		
		return actors;
	}
	
public Collection<Actor> searchUsuarioForApellido(String apellidos){
		
		Collection<Actor> result = actorRepository.searchUsuarioForApellidos(apellidos);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public void savePassword(Actor actor){
		Assert.notNull(actor);
					
		String password = actor.getUserAccount().getPassword();
		password = passwordEncoder.encode(password);
		
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

	// Fase 3: listados paginados en la base de datos ---------------------

	public Page<Actor> findAll(Pageable pageable){
		return actorRepository.findAll(pageable);
	}

	public Page<Actor> searchUsuarioForApellido(String apellidos, Pageable pageable){
		return actorRepository.searchUsuarioForApellidos(apellidos, pageable);
	}

}
