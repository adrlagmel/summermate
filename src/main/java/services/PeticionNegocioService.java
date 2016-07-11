package services;

import java.util.Collection;


import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PeticionNegocioRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import utilities.validators.RandomString;
import domain.Administrador;
import domain.Empresario;
import domain.PeticionNegocio;

@Service
@Transactional
public class PeticionNegocioService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private PeticionNegocioRepository peticionNegocioRepository;
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private AdministradorService administradorService;
	
	@Autowired
	private EmpresarioService empresarioService;
	
	// Constructor -----------------------------------------------------------
	
	public PeticionNegocioService(){
		
	}
	
	// Simple CRUD Methods --------------------------------------------
	
	
	public PeticionNegocio create(){
		empresarioService.checkPrincipal();
		Empresario empresario = empresarioService.findByPrincipal();
		Assert.isTrue(canCreateSportPartnerRequest());

		PeticionNegocio res = new PeticionNegocio();
		
		RandomString rs = new RandomString(6);
		String randomStr = rs.nextString();
		
		res.setFecha(new Date(System.currentTimeMillis()-1000));
		res.setCodigo(randomStr);
		res.setEstado("PENDIENTE");
		res.setEmpresario(empresario);
		
		return res;
	}
	
	public PeticionNegocio save(PeticionNegocio sportPartnerRequest){
		
		checkPrincipal(sportPartnerRequest);
		
		if(sportPartnerRequest.getId() == 0){
			Assert.isTrue(canCreateSportPartnerRequest());
			sportPartnerRequest.setFecha(new Date(System.currentTimeMillis()-1000));
			sportPartnerRequest.setEstado("PENDIENTE");
		}
		
		return peticionNegocioRepository.save(sportPartnerRequest);
	}
	
	public void delete(PeticionNegocio peticionNegocio){
		checkPrincipal(peticionNegocio);
					
		peticionNegocioRepository.delete(peticionNegocio);
	}
	
	public PeticionNegocio findOneToEdit(int peticionNegocioId){
		PeticionNegocio peticionNegocio = peticionNegocioRepository.findOne(peticionNegocioId);
		checkPrincipal(peticionNegocio);
		
		return peticionNegocio;
	}
	
	public PeticionNegocio findOneToChangeStatus(int sportPartnerRequestId){
		PeticionNegocio sportPartnerRequest = peticionNegocioRepository.findOne(sportPartnerRequestId);
		administradorService.checkPrincipal(sportPartnerRequest.getAdministrador());
		return sportPartnerRequest;
	}
	
	
	// Other business Methods -----------------------------------------
	public Boolean canCreateSportPartnerRequest(){
		Boolean result = false;
		Empresario sportPartner = empresarioService.findByPrincipal();
		
		PeticionNegocio sportPartnerRequest = peticionNegocioRepository.findByEmpresarioId(sportPartner.getId());
		
		if(sportPartnerRequest == null)
			result = true;
				
		return result;
	}
	
	public Boolean canPublish(){
		Boolean result = false;
		Empresario sportPartner = empresarioService.findByPrincipal();
		
		PeticionNegocio sportPartnerRequest = peticionNegocioRepository.findByEmpresarioId(sportPartner.getId());
		
		if(sportPartnerRequest != null && sportPartnerRequest.getEstado().equals("ACEPTADO"))
			result = true;
				
		return result;
		
	}

	public PeticionNegocio manageSportPartnerRequestByAdministrator(PeticionNegocio sportPartnerRequest){
		administradorService.checkPrincipal();
		Administrador administrador = administradorService.findByPrincipal();
		
		sportPartnerRequest.setAdministrador(administrador);
				
		return sportPartnerRequest;
	}
	
	public PeticionNegocio findSportPartnerRequestsBySportPartner(Empresario empresario){
		empresarioService.checkPrincipal(empresario);
		return peticionNegocioRepository.findByEmpresarioId(empresario.getId());
	}
	
	public void reject(PeticionNegocio peticionNegocio){
		checkPrincipal(peticionNegocio);
		Administrador administrador = administradorService.findByPrincipal();
		
		peticionNegocio.setAdministrador(administrador);
		peticionNegocio.setEstado("RECHAZADO");
		peticionNegocioRepository.save(peticionNegocio);
	}
	
	public void accept(PeticionNegocio peticionNegocio){
		checkPrincipal(peticionNegocio);
		Administrador administrator = administradorService.findByPrincipal();
		
		peticionNegocio.setAdministrador(administrator);
		peticionNegocio.setEstado("ACEPTADO");
		peticionNegocioRepository.save(peticionNegocio);
	}
	
		
	public Collection<PeticionNegocio> findPeticionNegocioPendiente(){
		Collection<PeticionNegocio> result = peticionNegocioRepository.findPeticionesNegociosPendientes();
		
		return result;
	}
	
	public PeticionNegocio assignAdministrator(PeticionNegocio peticionNegocio){

		
		administradorService.checkPrincipal();

		Assert.isNull(peticionNegocio.getAdministrador());

		PeticionNegocio result;
		Administrador administrator = administradorService.findByPrincipal();

		peticionNegocio.setAdministrador(administrator);

		result = peticionNegocioRepository.save(peticionNegocio);

		return result;
	}
	
	public PeticionNegocio findBySportPartnerId(int peticionNegocioId){
		
		PeticionNegocio sportPartnerRequest = peticionNegocioRepository.findByEmpresarioId(peticionNegocioId);
		return sportPartnerRequest;
	}

	

	public void checkPrincipal(PeticionNegocio peticionNegocio) {

		Assert.notNull(peticionNegocio);

		UserAccount userAccount = LoginService.getPrincipal();

		Authority adminAuth = new Authority();
		adminAuth.setAuthority("ADMINISTRADOR");

		if(userAccount.getAuthorities().contains(adminAuth)){
			
			if(peticionNegocio.getAdministrador() != null)
				Assert.isTrue(peticionNegocio.getAdministrador().getUserAccount().equals(userAccount));

		}else

			Assert.isTrue(peticionNegocio.getEmpresario().getUserAccount().equals(userAccount));
	}
}
