package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdministradorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Administrador;
import domain.Carpeta;
import domain.PeticionNegocio;
import domain.Playa;
import forms.AdministradorRegistroForm;

@Service
@Transactional
public class AdministradorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdministradorRepository administradorRepository;
	
	// Supporting services ----------------------------------------------------

	
	@Autowired
	private CarpetaService carpetaService;
	
	// Constructors -----------------------------------------------------------

	public AdministradorService() {
		super();
		
	}
	// Simple CRUD methods ----------------------------------------------------

	public Administrador create(){
		Administrador admin = new Administrador();
		
		Authority authority = new Authority();
		UserAccount userAccount = new UserAccount();
				
		authority.setAuthority(Authority.ADMININISTRADOR);
		userAccount.addAuthority(authority);
		admin.setUserAccount(userAccount);
		
		Collection<Playa> playas	= new ArrayList<Playa>();
		Collection<PeticionNegocio> peticionNegocios = new ArrayList<PeticionNegocio>();
		Collection<Carpeta> carpetas	= new ArrayList<Carpeta>();
		
		admin.setPlayas(playas);
		admin.setPeticionNegocios(peticionNegocios);	
		admin.setCarpetas(carpetas);
		
		return admin;
	}
	
	public void save(Administrador admin){
		Assert.notNull(admin);
		
		String password = admin.getUserAccount().getPassword();
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(password, null);
		
		admin.getUserAccount().setPassword(password);
		
		Administrador saved = administradorRepository.save(admin);
		carpetaService.createDefaultCarpetas(saved);
					
		administradorRepository.save(saved);
				
	}
	
	public Administrador reconstruct(AdministradorRegistroForm form) {
		
		Administrador admin=create();
		
		admin.setEmail(form.getEmail());
		admin.setNombre(form.getNombre());
		admin.setApellidos(form.getApellidos());
		
		admin.getUserAccount().setUsername(form.getUsername());
		admin.getUserAccount().setPassword(form.getPassword());
		
		return admin;
	}

	
	public Administrador findByPrincipal(){
		
		UserAccount principalAccount = LoginService.getPrincipal();
		
		Administrador principal = findByUserAccount(principalAccount.getId());
		return principal;		
	}
	
	
	public void checkPrincipal(){
		
		UserAccount userAccount = LoginService.getPrincipal();
		
		Collection<Authority> authorities = userAccount.getAuthorities();
		
		Authority auth = new Authority();
		auth.setAuthority("ADMINISTRADOR");
		
		Assert.isTrue(authorities.contains(auth));
	}
	public void checkPrincipal(Administrador administrador){
		Assert.notNull(administrador);
		UserAccount userAccount = LoginService.getPrincipal();
				
		Assert.isTrue(administrador.getUserAccount().equals(userAccount));
	}
	
	public Administrador findByUserAccount(int id){
		Assert.isTrue(id != 0);
		Administrador res;
		 
		 res = this.administradorRepository.findByUserAccountId(id);
		 Assert.notNull(res);
		 
		 return res;
	}

	
}

