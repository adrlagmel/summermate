package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.EmpresarioRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Carpeta;
import domain.DenunciaValoracion;
import domain.Empresario;
import domain.Negocio;
import domain.Pago;

import forms.EmpresarioRegistroForm;


@Service
@Transactional
public class EmpresarioService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private EmpresarioRepository empresarioRepository;
	
	
	
	// Supporting services ----------------------------------------------------
	@Autowired
	private CarpetaService carpetaService;
	
	// Constructors -----------------------------------------------------------

	public EmpresarioService() {
		super();
		
	}
	// Simple CRUD methods ----------------------------------------------------

	public Empresario create(){
		Empresario e = new Empresario();
		
		Authority authority = new Authority();
		UserAccount userAccount = new UserAccount();
				
		authority.setAuthority(Authority.EMPRESARIO);
		userAccount.addAuthority(authority);
		e.setUserAccount(userAccount);
		
		Collection<Negocio> negocios  				= new ArrayList<Negocio>();
		Collection<Carpeta> carpetas				= new ArrayList<Carpeta>();
		Collection<DenunciaValoracion> denuncias	= new ArrayList<DenunciaValoracion>();
		Collection<Pago> pagos						= new ArrayList<Pago>();
	
		e.setNegocios(negocios);
		e.setCarpetas(carpetas);
		e.setDenuncias(denuncias);
		e.setPagos(pagos);
	
		return e;
	}
	
	public void save(Empresario empresario){
		Assert.notNull(empresario);
		//Assert.isTrue(empresario.get)
		
		String password = empresario.getUserAccount().getPassword();
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(password, null);
		
		empresario.getUserAccount().setPassword(password);
		
		Empresario saved = empresarioRepository.save(empresario);
		carpetaService.createDefaultCarpetas(saved);
					
		empresarioRepository.save(saved);
				
	}
	
	// Other business methods ------------------------------------------------

	
		public Empresario findByUserAccount(int id){
			Assert.isTrue(id != 0);
			 Empresario res;
			 
			 res = this.empresarioRepository.findByUserAccountId(id);
			 Assert.notNull(res);
			 
			 return res;
		}
		
		public Empresario findByPrincipal(){
			
			UserAccount principalAccount = LoginService.getPrincipal();
			
			Empresario principal = findByUserAccount(principalAccount.getId());
			return principal;		
		}
		
		public void checkPrincipal(){
			
			UserAccount userAccount = LoginService.getPrincipal();
			
			Collection<Authority> authorities = userAccount.getAuthorities();
			
			Authority auth = new Authority();
			auth.setAuthority("EMPRESARIO");
			
			Assert.isTrue(authorities.contains(auth));
	  }
		
		public void checkPrincipal(Empresario empresario){
			Assert.notNull(empresario);
			UserAccount userAccount = LoginService.getPrincipal();
					
			Assert.isTrue(empresario.getUserAccount().equals(userAccount));
	  }
	
public Empresario reconstruct(EmpresarioRegistroForm form) {
		
		Empresario empresario=create();
		
		empresario.setEmail(form.getRegistroForm().getEmail());
		empresario.setNombre(form.getRegistroForm().getNombre());
		empresario.setApellidos(form.getRegistroForm().getApellidos());
		
		empresario.setTelefono(form.getRegistroForm().getTelefono());
		empresario.setFechaNacimiento(form.getRegistroForm().getFechaNacimiento());
		empresario.setNacionalidad(form.getRegistroForm().getNacionalidad());
		empresario.setDireccion(form.getRegistroForm().getDireccion());
		empresario.setSexo(form.getRegistroForm().getSexo());
		
		empresario.setCif(form.getCif());
		
		empresario.getUserAccount().setUsername(form.getRegistroForm().getUsername());
		empresario.getUserAccount().setPassword(form.getRegistroForm().getPassword());
		
		return empresario;
	}


	}

