package services;

import java.util.ArrayList;



import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Carpeta;
import domain.DenunciaValoracion;
import domain.Evento;
import domain.Reserva;
import domain.Usuario;
import domain.ValoracionPlaya;
import forms.UsuarioEditForm;
import forms.UsuarioRegistroForm;

import repositories.UsuarioRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class UsuarioService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	// Supporting services ----------------------------------------------------

	
	@Autowired
	private CarpetaService carpetaService;
	
	// Constructors -----------------------------------------------------------

	public UsuarioService() {
		super();
		
	}
	// Simple CRUD methods ----------------------------------------------------

	public Usuario create(){
		Usuario usuario = new Usuario();
		
		Authority authority = new Authority();
		UserAccount userAccount = new UserAccount();
				
		authority.setAuthority(Authority.USUARIO);
		userAccount.addAuthority(authority);
		usuario.setUserAccount(userAccount);
		
		Collection<Reserva> reservas	= new ArrayList<Reserva>();
		Collection<Evento> eventos		= new ArrayList<Evento>();
		Collection<Carpeta> carpetas	= new ArrayList<Carpeta>();
		Collection<DenunciaValoracion> denuncias 	= new ArrayList<DenunciaValoracion>();
		Collection<ValoracionPlaya> valoracionPlaya = new ArrayList<ValoracionPlaya>();
		
		usuario.setReservas(reservas);
		usuario.setEventos(eventos);		
		usuario.setCarpetas(carpetas);
		usuario.setDenuncias(denuncias);
		usuario.setValoracionPlayas(valoracionPlaya);	
		
		return usuario;
	}
	
	public void save(Usuario usuario){
		Assert.notNull(usuario);
		
		String password = usuario.getUserAccount().getPassword();
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(password, null);
		
		usuario.getUserAccount().setPassword(password);
		
		Usuario saved = usuarioRepository.save(usuario);
		carpetaService.createDefaultCarpetas(saved);
					
		usuarioRepository.save(saved);
				
	}
	
	public void saveImage(Usuario usuario){
		Assert.notNull(usuario);
				
		usuarioRepository.save(usuario);	
	}
	
	public void saveEdit(Usuario usuario){
		Assert.notNull(usuario);
		
		usuarioRepository.save(usuario);
				
	}
	
	// Other business methods ------------------------------------------------

	public Usuario findByUserAccount(int id){
		Assert.isTrue(id != 0);
		Usuario res;
		 
		 res = this.usuarioRepository.findByUserAccountId(id);
		 Assert.notNull(res);
		 
		 return res;
	}
	
	public Usuario findOne(int id){
		Assert.isTrue(id != 0);
		Usuario res;
		 
		 res = this.usuarioRepository.findOne(id);
		 Assert.notNull(res);
		 
		 return res;
	}
	
	public Usuario findOneToEdit(int id){
		checkPrincipal();
		Assert.isTrue(id != 0);
		Usuario res;
		 
		res = this.usuarioRepository.findOne(id);
		Assert.notNull(res);
		 
		return res;
	}
	
	public Usuario findByPrincipal(){
		UserAccount principalAccount = LoginService.getPrincipal();
		Usuario principal = findByUserAccount(principalAccount.getId());
		
		return principal;		
	}
	
	public Collection<Usuario> findAll(){
		return usuarioRepository.findAll();
	}		
		
	public void checkPrincipal(){
		UserAccount userAccount = LoginService.getPrincipal();
		
		Collection<Authority> authorities = userAccount.getAuthorities();
		
		Authority auth = new Authority();
		auth.setAuthority("USUARIO");
		
		Assert.isTrue(authorities.contains(auth));
	 }

	
	public Usuario reconstruct(UsuarioRegistroForm form) {
		
		Usuario usuario=create();
		
		usuario.setEmail(form.getRegistroForm().getEmail());
		usuario.setNombre(form.getRegistroForm().getNombre());
		usuario.setApellidos(form.getRegistroForm().getApellidos());
		
		usuario.setTelefono(form.getRegistroForm().getTelefono());
		usuario.setFechaNacimiento(form.getRegistroForm().getFechaNacimiento());
		usuario.setNacionalidad(form.getRegistroForm().getNacionalidad());
		usuario.setDireccion(form.getRegistroForm().getDireccion());
		usuario.setSexo(form.getRegistroForm().getSexo());
		
		usuario.setEstadoActual(form.getEstadoActual());
		usuario.setNivelColaborador(form.getNivelColaborador());
		usuario.setPuntos(form.getPuntos());
		
		usuario.getUserAccount().setUsername(form.getRegistroForm().getUsername());
		usuario.getUserAccount().setPassword(form.getRegistroForm().getPassword());
		
		return usuario;
	}
	
	public Usuario reconstructEdit(UsuarioEditForm form) {
		
		Usuario result = findByPrincipal();
		
		result.setEmail(form.getEmail());
		result.setNombre(form.getNombre());
		result.setApellidos(form.getApellidos());
		
		result.setTelefono(form.getTelefono());
		result.setFechaNacimiento(form.getFechaNacimiento());
		result.setNacionalidad(form.getNacionalidad());
		result.setDireccion(form.getDireccion());
		result.setSexo(form.getSexo());
		
		result.setEstadoActual(form.getEstadoActual());
		result.setNivelColaborador(form.getNivelColaborador());
		result.setPuntos(form.getPuntos());
		
		return result;
	}

	public void savePassword(Usuario usuario) {
		Assert.notNull(usuario);
		
		String password = usuario.getUserAccount().getPassword();
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(password, null);
		usuario.getUserAccount().setPassword(password);
		checkPrincipal(usuario);
		usuarioRepository.save(usuario);
		
	}

	public void checkPrincipal(Usuario u){
		Assert.notNull(u);
		
		Assert.isTrue(findByPrincipal().equals(u));
		
		
	}
	
	public void addImageToNegocio(int usuarioId, byte[] bytes) {
		Usuario usuario = findOneToEdit(usuarioId);
		
		if(bytes.length==0){
			usuario.setImagen(null);
		}else{
			usuario.setImagen(bytes);
		}
		
		saveImage(usuario);
	}
	}
