package services;

import java.util.ArrayList;


import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.NegocioRepository;
import security.LoginService;
import security.UserAccount;
import domain.Empresario;
import domain.Evento;
import domain.Localizacion;
import domain.Mesa;
import domain.Negocio;
import domain.PeticionNegocio;
import domain.Reserva;

@Service
@Transactional
public class NegocioService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private NegocioRepository negocioRepository;
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private EmpresarioService empresarioService;
	
	@Autowired
	private PeticionNegocioService peticionNegocioService;
	
	@Autowired
	private AdministradorService administradorService;
	// Constructor -----------------------------------------------------------
	
	public NegocioService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Negocio create(){
		Negocio result = new Negocio();
		
		Empresario principal = empresarioService.findByPrincipal();
		
		PeticionNegocio peticionNegocio = peticionNegocioService.findByEmpresarioId(principal.getId());
		Assert.notNull(peticionNegocio);
		
		result.setReservas(new ArrayList<Reserva>());
		result.setLocalizacion(new Localizacion());
		result.setMesas(new ArrayList<Mesa>());
		result.setEventos(new ArrayList<Evento>());
		result.setEmpresario(principal);
		
		return result;
	}
	
	public void save(Negocio negocio){
		checkPrincipal(negocio);
		
		PeticionNegocio peticionNegocio = peticionNegocioService.findByEmpresarioId(negocio.getEmpresario().getId());
		Assert.notNull(peticionNegocio);
		
		if(peticionNegocio != null)
			Assert.isTrue(peticionNegocio.getEstado().equals("ACEPTADO"));
		
		negocioRepository.save(negocio);
		
	}
	
	public Collection<Negocio> findAll(){
		
		Collection<Negocio> result = negocioRepository.findAll();
		Assert.notNull(result);
		
		return result;
		
	}
	
	
	public Negocio findOneToEdit(int negocioId){
		Negocio negocio = negocioRepository.findOne(negocioId);
		checkPrincipal(negocio);
		return negocio;
	}
	
	// Other business methods ------------------------------------------------
	public Collection<Negocio> negocioConMasReservas(){
		administradorService.checkPrincipal();
		Collection<Negocio> result = negocioRepository.negocioConMasReservas();
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> searchByTown(String town){
		
		Collection<Negocio> result = negocioRepository.search(town);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> searchByTownAndSportPartner(String ciudad){
		Empresario empresario = empresarioService.findByPrincipal();
		
		Collection<Negocio> result = negocioRepository.searchNegocio(ciudad, empresario.getId());
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Negocio findOneToDisplay(int negocioId){
		
		Negocio result = negocioRepository.findOne(negocioId);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> findByEmpresario(){
		
		Empresario empresario = empresarioService.findByPrincipal();
		
		Collection<Negocio> result = negocioRepository.findByEmpresario(empresario.getId());
		Assert.notNull(result);
		
		return result;
		
	}
	

	public void checkPrincipal(Negocio negocio) {
		Assert.notNull(negocio);
		
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(negocio.getEmpresario().getUserAccount().equals(userAccount));
	}
	
	public Collection<Negocio> nearToMe(Double lat, Double lon) {
		Collection<Negocio> res=new ArrayList<Negocio>();
		for(Negocio sp:findAll()){
			if(distance(lat,lon,sp.getLocalizacion().getLatitud(),sp.getLocalizacion().getLongitud())<=10){
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
	
	public void addImageToNegocio(int negocioId, byte[] bytes) {
		
		Negocio n  = findOneToEdit(negocioId);
		if(bytes.length==0){
		
			n.setImagen(null);
		}else{
			n.setImagen(bytes);
		}
		
		save(n);
	}
}
