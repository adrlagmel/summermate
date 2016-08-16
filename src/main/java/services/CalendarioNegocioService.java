package services;

import java.util.Calendar;



import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CalendarioNegocioRepository;
import domain.CalendarioNegocio;
import domain.Negocio;
import domain.Reserva;

@Service
@Transactional
public class CalendarioNegocioService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private CalendarioNegocioRepository calendarioNegocioRepository;
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private NegocioService negocioService;
	
	// Constructor -----------------------------------------------------------
	
	public CalendarioNegocioService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	
	public CalendarioNegocio create(int negocioId){
		CalendarioNegocio negocioCalendario = new CalendarioNegocio();
		
		Negocio negocio = negocioService.findOneToEdit(negocioId);
		
		negocioCalendario.setNegocio(negocio);
		
		return negocioCalendario;
	}
	
	public CalendarioNegocio findOneToEdit(int sportCenterCalendarId){
		CalendarioNegocio sportCenterCalendar = calendarioNegocioRepository.findOne(sportCenterCalendarId);
		checkPrincipal(sportCenterCalendar);
		
		return sportCenterCalendar;
	}
	
	public void create(Reserva reserva){
		
		CalendarioNegocio result = new CalendarioNegocio();
				
		Calendar sMoment = Calendar.getInstance();
		sMoment.setTime(reserva.getFecha());
		
		Calendar eMoment = Calendar.getInstance();
		eMoment.setTime(reserva.getFecha());
		eMoment.add(Calendar.MINUTE, 59);
		
		Date startMoment = sMoment.getTime();
		Date endMoment = eMoment.getTime();
		
		Calendar sMomentToCheck = Calendar.getInstance();
		sMomentToCheck.setTime(reserva.getFecha());
		sMomentToCheck.set(Calendar.HOUR_OF_DAY, 0);
		sMomentToCheck.set(Calendar.MINUTE, 0);
		sMomentToCheck.set(Calendar.SECOND, 0);
		sMomentToCheck.set(Calendar.MILLISECOND, 0);
		
		Calendar eMomentToCheck = Calendar.getInstance();
		eMomentToCheck.setTime(endMoment);
		eMomentToCheck.set(Calendar.HOUR_OF_DAY, 0);
		eMomentToCheck.set(Calendar.MINUTE, 0);
		eMomentToCheck.set(Calendar.SECOND, 0);
		eMomentToCheck.set(Calendar.MILLISECOND, 0);
		
		Assert.isTrue(sMomentToCheck.equals(eMomentToCheck));
		
		sMomentToCheck.set(Calendar.HOUR_OF_DAY, 12);
		eMomentToCheck.set(Calendar.HOUR_OF_DAY, 24);
		
		Assert.isTrue(sMoment.compareTo(sMomentToCheck)>=0);
		Assert.isTrue(eMoment.compareTo(eMomentToCheck)<=0);
				
		if(findComensalesPorFechaDeReserva(startMoment, endMoment, reserva.getNegocio().getId()) < reserva.getNegocio().getAforo()){
		
			result.setNegocio(reserva.getNegocio());
			result.setFechaInicio(startMoment);
			result.setFechaFin(endMoment);
			result.setAnotacionesReserva(reserva.getCodigo());
			calendarioNegocioRepository.save(result);
		}
		}
		
	public void save(CalendarioNegocio calendarioNegocio){
		checkPrincipal(calendarioNegocio);
		
		Assert.isTrue(calendarioNegocio.getFechaInicio().after(new Date()) 
				&& calendarioNegocio.getFechaFin().after(calendarioNegocio.getFechaInicio()));
				
		Calendar sMoment = Calendar.getInstance();
		sMoment.setTime(calendarioNegocio.getFechaInicio());
		
		Calendar eMoment = Calendar.getInstance();
		eMoment.setTime(calendarioNegocio.getFechaFin());

		
		Calendar sMomentToCheck = Calendar.getInstance();
		sMomentToCheck.setTime(calendarioNegocio.getFechaInicio());
		sMomentToCheck.set(Calendar.HOUR_OF_DAY, 0);
		sMomentToCheck.set(Calendar.MINUTE, 0);
		sMomentToCheck.set(Calendar.SECOND, 0);
		sMomentToCheck.set(Calendar.MILLISECOND, 0);
		
		Calendar eMomentToCheck = Calendar.getInstance();
		eMomentToCheck.setTime(calendarioNegocio.getFechaFin());
		eMomentToCheck.set(Calendar.HOUR_OF_DAY, 0);
		eMomentToCheck.set(Calendar.MINUTE, 0);
		eMomentToCheck.set(Calendar.SECOND, 0);
		eMomentToCheck.set(Calendar.MILLISECOND, 0);
		
		Assert.isTrue(sMomentToCheck.equals(eMomentToCheck));
		
		sMomentToCheck.set(Calendar.HOUR_OF_DAY, 12);
		eMomentToCheck.set(Calendar.HOUR_OF_DAY, 24);

		Assert.isTrue(sMoment.compareTo(sMomentToCheck)>=0 && eMoment.compareTo(eMomentToCheck)<=0);
		
		Collection<CalendarioNegocio> negocioCalendars = findNegocioCalendarByBookingDates(calendarioNegocio.getFechaInicio(), 
				calendarioNegocio.getFechaFin(), calendarioNegocio.getNegocio().getId());
		
		if (negocioCalendars.size() > 0) {
			Integer comensales = findComensalesPorFechaDeReserva(calendarioNegocio.getFechaInicio(), calendarioNegocio.getFechaFin(), calendarioNegocio.getNegocio().getId());
			
			Assert.isTrue(comensales.compareTo(calendarioNegocio.getNegocio().getAforo())<=0);			
		}
	}
	
	// Other business methods ------------------------------------------------
	
	public void checkPrincipal(CalendarioNegocio calendarioNegocio) {
		Assert.notNull(calendarioNegocio);
		
		negocioService.checkPrincipal(calendarioNegocio.getNegocio());
	}
	
	public Collection<CalendarioNegocio> findNegocioCalendarByBookingDates(Date startMoment, Date endMoment, int negocioId){
		Collection<CalendarioNegocio> calendars = calendarioNegocioRepository.findCalendarioNegocioPorFechaDeReserva(startMoment, endMoment, negocioId);
		Assert.notNull(calendars);
		
		return calendars;
	}
	
	public Integer findComensalesPorFechaDeReserva(Date startMoment, Date endMoment, int negocioId){
		Integer comensales = calendarioNegocioRepository.findComensalesPorFechaDeReserva(startMoment, endMoment, negocioId);
		
		return comensales;
	}
	
	public Collection<CalendarioNegocio> findAllByNegocios(int negocioId){
		Collection<CalendarioNegocio> calendars = calendarioNegocioRepository.findByNegocio(negocioId);
		Assert.notNull(calendars);
		
		return calendars;
	}
	
}
