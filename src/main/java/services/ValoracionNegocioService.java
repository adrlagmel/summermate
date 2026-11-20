package services;

import java.util.ArrayList;

import java.util.Collection;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.Assert;

import repositories.NegocioRepository;
import repositories.ValoracionNegocioRepository;
import security.LoginService;
import security.UserAccount;

import domain.DenunciaValoracion;
import domain.Negocio;
import domain.Reserva;
import domain.Usuario;
import domain.ValoracionNegocio;
import forms.ValoracionNegocioForm;

@Service
@Transactional
public class ValoracionNegocioService {

	
	@Autowired
	private ValoracionNegocioRepository valoracionNegocioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private NegocioService negocioService;
	
	@Autowired
	private NegocioRepository negocioRepository;
	
	public ValoracionNegocioService() {

		super();

	}
	public ValoracionNegocio create(Reserva r){
		Assert.isTrue(!r.getFecha().isAfter(LocalDateTime.now()));
		
		ValoracionNegocio vn= new ValoracionNegocio();
		Collection<DenunciaValoracion> denuncias = new ArrayList<DenunciaValoracion>();
		
		vn.setDenuncias(denuncias);
		
		vn.setFecha(LocalDateTime.now());
		vn.setReserva(r);
		
		return vn;
	}
	
	// Copia en la entidad solo lo que escribe el usuario. save() comprueba que
	// la reserva (y por tanto la valoración) es suya.
	public ValoracionNegocio reconstruct(ValoracionNegocioForm form){
		ValoracionNegocio result;

		if(form.isNew()){
			result = create(form.getReserva());
		}else{
			result = findOneToEdit(form.getId());
			checkPrincipal(result);
			form.checkVersion(result);
		}

		result.setTitulo(form.getTitulo());
		result.setComentario(form.getComentario());
		result.setPuntuacion(form.getPuntuacion());

		return result;
	}

	public void save(ValoracionNegocio vn) { 
		checkPrincipal(vn);
		Integer idNegocio = vn.getReserva().getNegocio().getId();
		valoracionNegocioRepository.save(vn);
		vn.getReserva().getNegocio().setValoracionMedia(valoracionNegocioRepository.valoracionMediaNegocio(idNegocio));
	}
	
	// La media se recalcula después de borrar: antes se calculaba con la
	// valoración todavía dentro y quedaba desfasada.
	public void delete(ValoracionNegocio vNegocio){
		checkPrincipal(vNegocio);

		Negocio negocio = vNegocio.getReserva().getNegocio();

		vNegocio.getReserva().setValoracionNegocio(null);
		valoracionNegocioRepository.delete(vNegocio);
		valoracionNegocioRepository.flush();

		negocio.setValoracionMedia(valoracionNegocioRepository.valoracionMediaNegocio(negocio.getId()));
		negocioRepository.save(negocio);
	}
	
	public ValoracionNegocio findOne(int id) {
		ValoracionNegocio vn = valoracionNegocioRepository.findById(id).orElse(null);

		return vn;
	}
	
	public ValoracionNegocio findOneToEdit(int valoracionNegocioId) {
		ValoracionNegocio result = valoracionNegocioRepository.findById(valoracionNegocioId).orElse(null);
		
		Assert.notNull(result);
		
		return result;
	}

	
	public Collection<ValoracionNegocio> valoracionesNegocioDeUsuario(){
		Usuario u = usuarioService.findByPrincipal();
		int id = u.getId();
		Collection<ValoracionNegocio> result = valoracionNegocioRepository.findValoracionNegocioByUsuario(id);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<ValoracionNegocio> valoracionesNegocioDeUnNegocio(int negocioId){
		
		Collection<ValoracionNegocio> result = valoracionNegocioRepository.findValoracionNegocioByNegocio(negocioId);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public void checkPrincipal(ValoracionNegocio valoracionNegocio) {
		Assert.notNull(valoracionNegocio);
		
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(valoracionNegocio.getReserva().getUsuario().getUserAccount().equals(userAccount));
	}
	

}
