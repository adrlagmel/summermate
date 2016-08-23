package services;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.NegocioRepository;
import repositories.ValoracionNegocioRepository;
import security.LoginService;
import security.UserAccount;

import domain.DenunciaValoracion;
import domain.Negocio;
import domain.Reserva;
import domain.Usuario;
import domain.ValoracionNegocio;

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
		Assert.isTrue(r.getFecha().compareTo(new Date())<=0);
		
		ValoracionNegocio vn= new ValoracionNegocio();
		Collection<DenunciaValoracion> denuncias = new ArrayList<DenunciaValoracion>();
		
		vn.setDenuncias(denuncias);
		
		vn.setFecha(new Date());
		vn.setReserva(r);
		
		return vn;
	}
	
	public void save(ValoracionNegocio vn) { 
		checkPrincipal(vn);
		Integer idNegocio = vn.getReserva().getNegocio().getId();
		valoracionNegocioRepository.save(vn);
		vn.getReserva().getNegocio().setValoracionMedia(valoracionNegocioRepository.valoracionMediaNegocio(idNegocio));
	}
	
	public void delete(ValoracionNegocio vNegocio){
		checkPrincipal(vNegocio);
		
		Negocio negocio = negocioService.findOneToDisplay(vNegocio.getReserva().getNegocio().getId());
		Double valorMedio = valoracionNegocioRepository.valoracionMediaNegocio(negocio.getId());
				
		negocio.setValoracionMedia(valorMedio);
		
		negocioRepository.save(negocio);
		valoracionNegocioRepository.delete(vNegocio);
		
		vNegocio.getReserva().setValoracionNegocio(null);
	}
	
	public ValoracionNegocio findOne(int id) {
		ValoracionNegocio vn = valoracionNegocioRepository.findOne(id);

		return vn;
	}
	
	public ValoracionNegocio findOneToEdit(int valoracionNegocioId) {
		ValoracionNegocio result = valoracionNegocioRepository.findOne(valoracionNegocioId);
		
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
