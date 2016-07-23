package services;

import java.util.ArrayList;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PlayaRepository;
import security.LoginService;
import security.UserAccount;

import domain.Administrador;
import domain.Negocio;
import domain.Playa;
import domain.ValoracionPlaya;

@Service
@Transactional
public class PlayaService {
	
	@Autowired
	private PlayaRepository playaRepository;
	
	@Autowired
	private AdministradorService administradorService;
	
	
	public PlayaService(){
		super();
	}
	
	public Playa create() {
		Playa playa = new Playa();
		Administrador administrador 				= administradorService.findByPrincipal();	
		Collection<ValoracionPlaya> valoracionPlayas = new ArrayList<ValoracionPlaya>();
		
		playa.setNegocios(new ArrayList<Negocio>());
		playa.setValoracionPlayas(valoracionPlayas);
		playa.setAdministrador(administrador);
		
		return playa;
	}
	
	public Collection<Playa> findAllBeaches(){
		Collection<Playa> playas = playaRepository.findAll();
		
		return playas;
	}

	public Playa findOneToEdit(int playaId) {
		Playa result = playaRepository.findOne(playaId);
					
		return result;
	}
	
	public Playa findOne(int playaId) {
		Playa result = playaRepository.findOne(playaId);
					
		return result;
	}
	
	public void save(Playa playa){	
		checkPrincipal(playa);
		
		playaRepository.save(playa);
	}

	public void delete(Playa playa){
		Assert.notNull(playa);
		playaRepository.delete(playa);
	}
	
	public void checkPrincipal(Playa playa) {
		Assert.notNull(playa);
		
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(playa.getAdministrador().getUserAccount().equals(userAccount));
	}
	
	public void addImageToFoto(int playaId, byte[] bytes) {
		System.out.println("metodo de add");
		Playa p = findOne(playaId);
		if(bytes.length==0){
		
			p.setImagen(null);
		}else{
			p.setImagen(bytes);
		}
		
		save(p);
	}
}
