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
import domain.CalendarioNegocio;
import domain.Empresario;
import domain.Evento;
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
		Assert.isTrue(peticionNegocio.getEstado().equals("ACEPTADO"));
		
		result.setNegocioActivo(true);
		
		result.setReservas(new ArrayList<Reserva>());
		result.setCalendarioNegocios(new ArrayList<CalendarioNegocio>());
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
	
	public void suspenderNegocio(Negocio negocio){
		checkPrincipal(negocio);
		Assert.notNull(negocio);
		
		negocio.setNegocioActivo(false);
		
		negocioRepository.save(negocio);	
	}
	
	public void altaNegocio(Negocio negocio){
		checkPrincipal(negocio);
		Assert.notNull(negocio);
		
		negocio.setNegocioActivo(true);
		
		negocioRepository.save(negocio);	
	}
	
	public Collection<Negocio> findAll(){
		Collection<Negocio> result = negocioRepository.findAll();
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> findNegociosActivos(int empresarioId){
		Collection<Negocio> result = negocioRepository.findNegociosActivos(empresarioId);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> findNegociosDePlaya(int playaId){
		Collection<Negocio> result = negocioRepository.NegociosDeLaPlaya(playaId);
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
	public Collection<Negocio> findNegocioMejorValorado(){
				
		Collection<Negocio> result = negocioRepository.findNegocioMejorValorado();
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> negociosConMasReservas(){
		
		Collection<Negocio> result = negocioRepository.findNegocioMasReservas();
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
