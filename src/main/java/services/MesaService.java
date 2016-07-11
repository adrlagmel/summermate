package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MesaRepository;
import security.LoginService;
import security.UserAccount;
import domain.CalendarioNegocio;
import domain.Empresario;
import domain.Negocio;
import domain.Mesa;
import forms.MesaForm;

@Service
@Transactional
public class MesaService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private MesaRepository mesaRepository;
	
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private EmpresarioService empresarioService;
	
	@Autowired
	private NegocioService negocioService;
	
	// Constructor -----------------------------------------------------------
	
	public MesaService(){
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Mesa create(int negocioId){
		
		Mesa mesa 		= new Mesa();
		Negocio negocio = negocioService.findOneToDisplay(negocioId);
		Collection<CalendarioNegocio> calendarioNegocios = new ArrayList<CalendarioNegocio>();
		
		mesa.setNegocio(negocio);
		mesa.setCalendarioNegocios(calendarioNegocios);
				
		return mesa;
	}
	
	public Mesa save(Mesa mesa){
		checkPrincipal(mesa);

		return mesaRepository.save(mesa);
	}
	
	// Other business methods ------------------------------------------------
	
	
	public Mesa findOneToEdit(int mesaId){
		
		Mesa result = mesaRepository.findOne(mesaId);
		checkPrincipal(result);
		
		return result;
	}

	
	public Collection<Mesa> findMesasByNegocio(int negocioId){
		Negocio negocio = negocioService.findOneToDisplay(negocioId);
		negocioService.checkPrincipal(negocio);
		
		Collection<Mesa> result = mesaRepository.findByNegocio(negocioId);
		Assert.notNull(result);
		
		return result;
	}
	
	public Collection<Mesa> findMesasByEmpresario(){
		Empresario empresario = empresarioService.findByPrincipal();
		
		Collection<Mesa> result = mesaRepository.findMesasPorEmpresario(empresario.getId());
		Assert.notNull(result);
		
		return result;
	}
	
	public Mesa reconstruct(MesaForm form, int negocioId){
		
		Mesa mesa = null;
		
		if(form.getMesaId() == 0){
		
			mesa = create(negocioId);
			
			mesa.setNombre(form.getNombre());
			mesa.setDisponible(form.getDisponible());
			mesa.setFumadores(form.getFumadores());
			mesa.setPersonas(form.getPersonas());
			mesa.setId(0);
			
						
		}else{
			
			mesa = findOneToEdit(form.getMesaId());
			
			mesa.setNombre(form.getNombre());
			mesa.setDisponible(form.getDisponible());
			mesa.setFumadores(form.getFumadores());
			mesa.setPersonas(form.getPersonas());
	
		}
		
		return mesa;
	}
	
	
	public void checkPrincipal(Mesa mesa){
		Assert.notNull(mesa);
		
		UserAccount userAccount = LoginService.getPrincipal();
		
		Assert.isTrue(mesa.getNegocio().getEmpresario().getUserAccount().equals(userAccount)
				|| mesa.getNegocio().getEmpresario().getUserAccount().equals(userAccount));
		
	}

}
