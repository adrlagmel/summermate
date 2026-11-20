package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.Assert;

import repositories.CalendarioNegocioRepository;
import domain.CalendarioNegocio;
import domain.Negocio;
import domain.Reserva;

// Franjas ocupadas de cada negocio. Cada reserva crea una franja de una hora
// (create(Reserva)); el aforo se comprueba sumando los comensales de las
// franjas que se solapan.
//
// Fase 4: java.util.Date/Calendar pasan a java.time sin cambiar la lógica.
// La parte que gestiona el empresario (save) sigue igual que antes: valida
// pero no guarda nada. Se arreglará junto con el resto del calendario.
@Service
@Transactional
public class CalendarioNegocioService {

	// Las reservas se admiten desde las 12:00 hasta la medianoche
	private static final LocalTime	APERTURA	= LocalTime.NOON;

	@Autowired
	private CalendarioNegocioRepository	calendarioNegocioRepository;

	@Autowired
	private NegocioService				negocioService;


	public CalendarioNegocio create(int negocioId) {
		CalendarioNegocio negocioCalendario = new CalendarioNegocio();
		Negocio negocio = negocioService.findOneToEdit(negocioId);

		negocioCalendario.setNegocio(negocio);

		return negocioCalendario;
	}

	// Franja de 59 minutos a partir de la hora de la reserva, el mismo día y
	// dentro del horario.
	public void create(Reserva reserva) {
		CalendarioNegocio result = new CalendarioNegocio();
		LocalDateTime inicio = reserva.getFecha();
		LocalDateTime fin = inicio.plusMinutes(59);

		Assert.isTrue(inicio.toLocalDate().equals(fin.toLocalDate()));
		Assert.isTrue(!inicio.isBefore(apertura(inicio.toLocalDate())));
		Assert.isTrue(!fin.isAfter(cierre(fin.toLocalDate())));

		result.setCodigoNegocio(reserva.getCodigo());

		if (findComensalesPorFechaDeReserva(inicio, fin, reserva.getNegocio().getId()) < reserva.getNegocio().getAforo()) {
			result.setNegocio(reserva.getNegocio());
			result.setFechaInicio(inicio);
			result.setFechaFin(fin);
			result.setAnotacionesReserva(reserva.getCodigo());
			calendarioNegocioRepository.save(result);
		}
	}

	public void save(CalendarioNegocio calendarioNegocio) {
		LocalDateTime inicio, fin;

		checkPrincipal(calendarioNegocio);
		inicio = calendarioNegocio.getFechaInicio();
		fin = calendarioNegocio.getFechaFin();

		Assert.isTrue(inicio.isAfter(LocalDateTime.now()) && fin.isAfter(inicio));
		Assert.isTrue(inicio.toLocalDate().equals(fin.toLocalDate()));
		Assert.isTrue(!inicio.isBefore(apertura(inicio.toLocalDate())) && !fin.isAfter(cierre(fin.toLocalDate())));

		Collection<CalendarioNegocio> negocioCalendars = findNegocioCalendarByBookingDates(inicio, fin, calendarioNegocio.getNegocio().getId());

		if (negocioCalendars.size() > 0) {
			Integer comensales = findComensalesPorFechaDeReserva(inicio, fin, calendarioNegocio.getNegocio().getId());
			Assert.isTrue(comensales.compareTo(calendarioNegocio.getNegocio().getAforo()) <= 0);
		}
	}

	public CalendarioNegocio findOneToEdit(int calendarioId) {
		CalendarioNegocio calendario = calendarioNegocioRepository.findById(calendarioId).orElse(null);

		checkPrincipal(calendario);

		return calendario;
	}

	public void checkPrincipal(CalendarioNegocio calendarioNegocio) {
		Assert.notNull(calendarioNegocio);
		negocioService.checkPrincipal(calendarioNegocio.getNegocio());
	}

	public Collection<CalendarioNegocio> findNegocioCalendarByBookingDates(LocalDateTime inicio, LocalDateTime fin, int negocioId) {
		Collection<CalendarioNegocio> calendars = calendarioNegocioRepository.findCalendarioNegocioPorFechaDeReserva(inicio, fin, negocioId);
		Assert.notNull(calendars);

		return calendars;
	}

	public Integer findComensalesPorFechaDeReserva(LocalDateTime inicio, LocalDateTime fin, int negocioId) {
		return calendarioNegocioRepository.findComensalesPorFechaDeReserva(inicio, fin, negocioId);
	}

	public CalendarioNegocio findByCodigoNegocio(String codigoId) {
		return calendarioNegocioRepository.findByCodigoNegocio(codigoId);
	}

	public void delete(String codigoId) {
		CalendarioNegocio calendarioNegocio = findByCodigoNegocio(codigoId);
		Assert.notNull(calendarioNegocio);

		calendarioNegocioRepository.delete(calendarioNegocio);
	}

	public Collection<CalendarioNegocio> findAllByNegocios(int negocioId) {
		Collection<CalendarioNegocio> calendars = calendarioNegocioRepository.findByNegocio(negocioId);
		Assert.notNull(calendars);

		return calendars;
	}

	private static LocalDateTime apertura(LocalDate dia) {
		return dia.atTime(APERTURA);
	}

	private static LocalDateTime cierre(LocalDate dia) {
		return dia.plusDays(1).atStartOfDay();
	}

}
