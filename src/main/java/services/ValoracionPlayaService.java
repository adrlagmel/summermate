package services;

import java.util.Collection;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ValoracionPlayaRepository;

import domain.Playa;
import domain.Usuario;
import domain.ValoracionPlaya;

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
		vp.setFecha(new Date());
		vp.setComentario("");
		vp.setPuntuacion(0);
		return vp;
	}
	
	public void save(ValoracionPlaya vp) {   
		Assert.notNull(vp);
		Integer id = vp.getPlaya().getId();
		valoracionPlayaRepository.save(vp);
		vp.getPlaya().setValoracionMedia(valoracionPlayaRepository.valoracionMediaPlaya(id));
	}
	
	public void delete(ValoracionPlaya vPlaya){
		Integer id = vPlaya.getPlaya().getId();
		valoracionPlayaRepository.delete(vPlaya);
		vPlaya.getPlaya().setValoracionMedia(valoracionPlayaRepository.valoracionMediaPlaya(id));

		

	}
	
	
	
	public ValoracionPlaya findOne(int id) {

		ValoracionPlaya vp = valoracionPlayaRepository
				.findOne(id);

		return vp;

	}
	
	public ValoracionPlaya findOneToEdit(int valoracionPlayaId) {
		ValoracionPlaya result = valoracionPlayaRepository.findOne(valoracionPlayaId);
		return result;
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
