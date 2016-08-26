package services;

import java.util.Calendar;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ReservaRepository;
import security.LoginService;
import security.UserAccount;
import utilities.validators.RandomString;
import domain.CalendarioNegocio;
import domain.Empresario;
import domain.Negocio;
import domain.Reserva;
import domain.Usuario;
import forms.ReservaForm;

@Service
@Transactional
public class ReservaService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EmpresarioService empresarioService;
	
	@Autowired
	private CalendarioNegocioService calendarioNegocioService;
	
	@Autowired
	private NegocioService negocioService;
	
	// Constructor -----------------------------------------------------------
	
	public ReservaService(){
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Reserva create(){
		
		Reserva reserva = new Reserva();
		
		Usuario athlete = usuarioService.findByPrincipal();
		RandomString rs = new RandomString(6);
		String randomStr = rs.nextString();
		
		reserva.setUsuario(athlete);
		reserva.setCodigo(randomStr);
		reserva.setFechaCreacion(new Date());
				
		return reserva;
	}
	
	public Reserva save(Reserva reserva){
		checkPrincipal(reserva);

		if(reserva.getId()==0){
			reserva.setFechaCreacion(new Date(System.currentTimeMillis()-1000));
		}
		
		Assert.isTrue(reserva.getNegocio().getNegocioActivo());
		
		Calendar stMoment = Calendar.getInstance();
		stMoment.setTime(reserva.getFecha());
		
		Calendar enMoment = Calendar.getInstance();
		enMoment.setTime(reserva.getFecha());
		enMoment.add(Calendar.HOUR_OF_DAY, 1);
		
		Date startMoment = stMoment.getTime();
		Date endMoment = enMoment.getTime();
		
		Collection<CalendarioNegocio> negocioCalendars = calendarioNegocioService.findNegocioCalendarByBookingDates(startMoment, endMoment, reserva.getNegocio().getId());
		
		if (negocioCalendars.size() > 0) {
			Integer comensales = calendarioNegocioService.findComensalesPorFechaDeReserva(startMoment, endMoment, reserva.getNegocio().getId());
			comensales = comensales + reserva.getComensales();
			
			Assert.isTrue(comensales.compareTo(reserva.getNegocio().getAforo())<=0);		
		}
		
		Assert.isTrue(reserva.getFecha().compareTo(new Date()) >= 0);
		
		Calendar sMoment = Calendar.getInstance();
		sMoment.setTime(reserva.getFecha());
		sMoment.set(Calendar.SECOND, 0);
		sMoment.set(Calendar.MILLISECOND, 0);
		
		Calendar sMomentToCheck = Calendar.getInstance();
		sMomentToCheck.setTime(reserva.getFecha());
			
		sMomentToCheck.set(Calendar.HOUR_OF_DAY, 12);
		sMomentToCheck.set(Calendar.MINUTE, 0);
		sMomentToCheck.set(Calendar.SECOND, 0);
		sMomentToCheck.set(Calendar.MILLISECOND, 0);
		
		Assert.isTrue(sMoment.compareTo(sMomentToCheck)>=0);
		sMomentToCheck.set(Calendar.HOUR_OF_DAY, 24);
		Assert.isTrue(sMoment.compareTo(sMomentToCheck)<=0);
		
		Reserva saved = reservaRepository.save(reserva);
		calendarioNegocioService.create(saved);
		
		return saved;
	}
	
	public void delete(Reserva reserva){
		Assert.notNull(reserva);
		
		calendarioNegocioService.delete(reserva.getCodigo());
		reservaRepository.delete(reserva);
		
	}
	
	public Collection<Reserva> searchByDate(String fecha){
		Empresario empresario = empresarioService.findByPrincipal();
		
		Collection<Reserva> result = reservaRepository.search(empresario.getId(), fecha);
		Assert.notNull(result);
		
		return result;
		
	}
	
	// Other business methods ------------------------------------------------
	
	
	public Reserva findOneToEdit(int reservaId){
		Reserva result = reservaRepository.findOne(reservaId);
		checkPrincipal(result);
		
		return result;
	}
	
	public Reserva findOne(int reservaId){
		Reserva result = reservaRepository.findOne(reservaId);
		
		return result;
	}
	
	public Reserva findOneToEditMesa(int reservaId){
		
		Reserva result = reservaRepository.findOne(reservaId);
		Assert.isTrue(result.getFecha() == null);
		checkPrincipal(result);
		
		return result;
	}

	
	public Collection<Reserva> findReservasPorNegocio(int negocioId){
		Negocio negocio = negocioService.findOneToDisplay(negocioId);
		negocioService.checkPrincipal(negocio);
		
		Collection<Reserva> result = reservaRepository.findReservasPorNegocio(negocioId);
		Assert.notNull(result);
		
		return result;
	}
	
	public Collection<Reserva> findReservasPorEmpresario(){
		int empresarioId = empresarioService.findByPrincipal().getId();
		
		Collection<Reserva> result = reservaRepository.findReservasPorEmpresario(empresarioId);
		Assert.notNull(result);
		
		return result;
	}
	
	public Collection<Reserva> findReservasByUsuario(){
		Usuario athlete = usuarioService.findByPrincipal();
		
		Collection<Reserva> result = reservaRepository.findReservasPorUsuario(athlete.getId());
		Assert.notNull(result);
		
		return result;
	}
	
	public Reserva reconstruct(ReservaForm form){
		Reserva reserva = null;
		
		if(form.getReservaId() == 0){
		
			reserva = create();
			
			reserva.setFecha(form.getFecha());
			reserva.setComentarios(form.getComentarios());
			reserva.setComensales(form.getComensales());
			reserva.setFumadores(form.getFumadores());
			reserva.setNegocio(form.getNegocio());
			
			reserva.setId(0);	
						
		}else{
			
			reserva = findOneToEdit(form.getReservaId());
			
			reserva.setFecha(form.getFecha());
			reserva.setComensales(form.getComensales());
			reserva.setFumadores(form.getFumadores());
			reserva.setComentarios(form.getComentarios());
		}
		
		return reserva;
	}
	
	public void checkPrincipal(Reserva reserva){
		Assert.notNull(reserva);
		
		UserAccount userAccount = LoginService.getPrincipal();
		
		Assert.isTrue(reserva.getUsuario().getUserAccount().equals(userAccount) || reserva.getNegocio().getEmpresario().getUserAccount().equals(userAccount));
		
	}

}
