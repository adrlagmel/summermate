package services;

import java.util.ArrayList;


import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EventoRepository;
import security.LoginService;
import security.UserAccount;
import domain.Empresario;
import domain.Evento;

@Service
@Transactional
public class EventoService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private EventoRepository eventoRepository;
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private EmpresarioService empresarioService;
	
	
	// Constructor -----------------------------------------------------------
	
	public EventoService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public void save(Evento e){
		checkPrincipal(e);
		
		eventoRepository.save(e);
		
	}
	
	public Evento findOneToEdit(int eventoId){
		Evento e = eventoRepository.findOne(eventoId);
		checkPrincipal(e);
		return e;
	}
	
	// Other business methods ------------------------------------------------
	
	public void checkPrincipal(Evento e) {
		Assert.notNull(e);
		
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(e.getNegocio().getEmpresario().getUserAccount().equals(userAccount));
	}
	
	
	public void addImageToEvento(int eventoId, byte[] bytes) {
		
		Evento e = findOneToEdit(eventoId);
		if(bytes.length==0){
		
			e.setImagen(null);
		}else{
			e.setImagen(bytes);
		}
		
		save(e);
	}
}
