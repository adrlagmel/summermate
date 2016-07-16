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
		Assert.isTrue(canCreatePeticionNegocio());

		PeticionNegocio res = new PeticionNegocio();
		
		RandomString rs = new RandomString(6);
		String randomStr = rs.nextString();
		
		res.setFecha(new Date(System.currentTimeMillis()-1000));
		res.setCodigo(randomStr);
		res.setEstado("PENDIENTE");
		res.setEmpresario(empresario);
		
		return res;
	}
	
	public PeticionNegocio save(PeticionNegocio peticionNegocio){
		
		checkPrincipal(peticionNegocio);
		
		if(peticionNegocio.getId() == 0){
			Assert.isTrue(canCreatePeticionNegocio());
			peticionNegocio.setFecha(new Date(System.currentTimeMillis()-1000));
			peticionNegocio.setEstado("PENDIENTE");
		}
		
		return peticionNegocioRepository.save(peticionNegocio);
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
	
	public PeticionNegocio findOneToChangeStatus(int peticionNegocioId){
		PeticionNegocio peticionNegocio = peticionNegocioRepository.findOne(peticionNegocioId);
		administradorService.checkPrincipal(peticionNegocio.getAdministrador());
		return peticionNegocio;
	}
	
	
	// Other business Methods -----------------------------------------
	public Boolean canCreatePeticionNegocio(){
		Boolean result = false;
		Empresario empresario = empresarioService.findByPrincipal();
		
		PeticionNegocio peticionNegocio = peticionNegocioRepository.findByEmpresarioId(empresario.getId());
		
		if(peticionNegocio == null)
			result = true;
				
		return result;
	}
	
	public Boolean canPublish(){
		Boolean result = false;
		Empresario empresario = empresarioService.findByPrincipal();
		
		PeticionNegocio peticionNegocio = peticionNegocioRepository.findByEmpresarioId(empresario.getId());
		
		if(peticionNegocio != null && peticionNegocio.getEstado().equals("ACEPTADO"))
			result = true;
				
		return result;
		
	}

	public PeticionNegocio manageNegociosByAdministrator(PeticionNegocio peticionNegocio){
		administradorService.checkPrincipal();
		Administrador administrador = administradorService.findByPrincipal();
		
		peticionNegocio.setAdministrador(administrador);
				
		return peticionNegocio;
	}
	
	public PeticionNegocio findPeticionNegocioPorEmpresario(Empresario empresario){
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
		Administrador administrador = administradorService.findByPrincipal();

		peticionNegocio.setAdministrador(administrador);

		result = peticionNegocioRepository.save(peticionNegocio);

		return result;
	}
	
	public PeticionNegocio findByEmpresarioId(int peticionNegocioId){
		
		PeticionNegocio peticionNegocio = peticionNegocioRepository.findByEmpresarioId(peticionNegocioId);
		return peticionNegocio;
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
