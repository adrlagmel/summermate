package services;

import java.util.Collection;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.Assert;

import repositories.ValoracionPlayaRepository;
import security.UserAccount;
import security.LoginService;

import domain.Playa;
import domain.Usuario;
import domain.ValoracionPlaya;
import forms.ValoracionPlayaForm;

@Service
@Transactional
public class ValoracionPlayaService {

	
	@Autowired
	private ValoracionPlayaRepository valoracionPlayaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public ValoracionPlayaService() {

		super();

	}
	public ValoracionPlaya create(Playa p){
		Usuario u = usuarioService.findByPrincipal();
		ValoracionPlaya vp= new ValoracionPlaya();
		vp.setPlaya(p);
		vp.setUsuario(u);
		vp.setTitulo("");
		vp.setFecha(LocalDateTime.now());
		vp.setComentario("");
		vp.setPuntuacion(0);
		return vp;
	}
	
	// Copia en la entidad solo lo que escribe el usuario.
	public ValoracionPlaya reconstruct(ValoracionPlayaForm form){
		ValoracionPlaya result;

		if(form.isNew()){
			result = create(form.getPlaya());
		}else{
			result = findOneToEdit(form.getId());
			form.checkVersion(result);
		}

		result.setTitulo(form.getTitulo());
		result.setComentario(form.getComentario());
		result.setPuntuacion(form.getPuntuacion());

		return result;
	}

	public void save(ValoracionPlaya vp) {   
		Assert.notNull(vp);
		checkPrincipal(vp);
		Integer id = vp.getPlaya().getId();
		valoracionPlayaRepository.save(vp);
		vp.getPlaya().setValoracionMedia(valoracionPlayaRepository.valoracionMediaPlaya(id));
	}
	
	public void delete(ValoracionPlaya vPlaya){
		checkPrincipal(vPlaya);
		Integer id = vPlaya.getPlaya().getId();
		valoracionPlayaRepository.delete(vPlaya);
		vPlaya.getPlaya().setValoracionMedia(valoracionPlayaRepository.valoracionMediaPlaya(id));

		

	}
	
	
	
	public ValoracionPlaya findOne(int id) {

		ValoracionPlaya vp = valoracionPlayaRepository
				.findById(id).orElse(null);

		return vp;

	}
	
	public ValoracionPlaya findOneToEdit(int valoracionPlayaId) {
		ValoracionPlaya result = valoracionPlayaRepository.findById(valoracionPlayaId).orElse(null);

		checkPrincipal(result);

		return result;
	}

	// Antes no se comprobaba: cualquier usuario podía editar o borrar la
	// valoración de otro cambiando el id.
	public void checkPrincipal(ValoracionPlaya valoracionPlaya) {
		Assert.notNull(valoracionPlaya);

		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(valoracionPlaya.getUsuario().getUserAccount().equals(userAccount));
	}



	
	public Collection<ValoracionPlaya> valoracionesDeUsuario(){
		Usuario u = usuarioService.findByPrincipal();
		int id = u.getId();
		Collection<ValoracionPlaya> result = valoracionPlayaRepository.findValoracionPlayaByUsuario(id);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<ValoracionPlaya> findValoracionPlayaByPlaya(int playaId){
		
		Collection<ValoracionPlaya> result = valoracionPlayaRepository.findValoracionPlayaByPlaya(playaId);
		Assert.notNull(result);
		return result;
	}

}
