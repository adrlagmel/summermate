package services;

import java.util.Collection;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PlayaRepository;

import domain.Playa;
import domain.ValoracionPlaya;

@Service
@Transactional
public class PlayaService {
	
	@Autowired
	private PlayaRepository playaRepository;
	
	
	public PlayaService(){
		super();
	}
	
	public Playa create() {

		Playa p = new Playa();
		
		Collection<ValoracionPlaya> valoracionPlayas = new HashSet<>();
		p.setValoracionPlayas(valoracionPlayas);
		
		
		return p;

	}
	
	public Collection<Playa> findPlayaForNoAuthenticate(){
		
		Collection<Playa> playas = playaRepository.findPlayaForNoAuthenticate();
		
		return playas;
	}

	public Playa findOneToEdit(int playaId) {
		Playa result;
			
		result = playaRepository.findOne(playaId);		
		return result;
	}
	
public void save(Playa playa){
		
		Assert.notNull(playa);
		
		playaRepository.save(playa);
		
	}

public void delete(Playa playa){
	Assert.notNull(playa);
	playaRepository.delete(playa);
}
	
	
}
