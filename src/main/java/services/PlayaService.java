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
import domain.Localizacion;
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
		
		playa.setLocalizacion(new Localizacion());
		playa.setNegocios(new ArrayList<Negocio>());
		playa.setValoracionPlayas(valoracionPlayas);
		playa.setAdministrador(administrador);
		playa.setValoracionMedia(0.0);
		
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
		Assert.isTrue(playa.getNegocios().size()<1);
		
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
	
	public Collection<Playa> nearToMe(Double lat, Double lon) {
		Collection<Playa> res = new ArrayList<Playa>();
		for(Playa sp:findAllBeaches()){
			if(distance(lat,lon,sp.getLocalizacion().getLatitud(),sp.getLocalizacion().getLongitud())<=10000){
				res.add(sp);
			}
		}
		
		return res;
	}
	
	
	
	
	private static double distance(double lat1, double lon1, double lat2, double lon2) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		
		return (dist);

	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
