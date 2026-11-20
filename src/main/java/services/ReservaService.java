package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import utilities.Assert;

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

	private static final DateTimeFormatter DIA_ES = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
	
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
		RandomString rs = new RandomString(10);
		String randomStr = rs.nextString();
		
		reserva.setUsuario(athlete);
		reserva.setCodigo(randomStr);
		reserva.setFechaCreacion(LocalDateTime.now());
				
		return reserva;
	}
	
	public Reserva save(Reserva reserva){
		checkPrincipal(reserva);

		if(reserva.getId()==0){
			reserva.setFechaCreacion(LocalDateTime.now().minusSeconds(1));
		}
		
		// Fase 3: se bloquea el negocio antes de contar los comensales. Sin
		// esto, dos reservas a la vez leían la misma ocupación y ambas pasaban
		// la comprobación aunque juntas superasen el aforo. La segunda espera
		// a que la primera termine y ya la ve.
		Negocio negocio = negocioService.bloquearParaReservar(reserva.getNegocio().getId());
		Assert.isTrue(negocio.getNegocioActivo());
		
		LocalDateTime startMoment = reserva.getFecha();
		LocalDateTime endMoment = startMoment.plusHours(1);
		
		// Antes solo se comprobaba si ya había reservas en esa franja, así que
		// la primera podía superar el aforo ella sola.
		Integer comensales = calendarioNegocioService.findComensalesPorFechaDeReserva(startMoment, endMoment, negocio.getId());
		comensales = comensales + reserva.getComensales();
		Assert.isTrue(comensales.compareTo(negocio.getAforo())<=0);
		
		Assert.isTrue(!reserva.getFecha().isBefore(LocalDateTime.now()));
		
		// Solo de 12:00 a 24:00
		LocalDateTime hora = reserva.getFecha().truncatedTo(ChronoUnit.MINUTES);
		LocalDate dia = hora.toLocalDate();
		Assert.isTrue(!hora.isBefore(dia.atTime(12, 0)));
		Assert.isTrue(!hora.isAfter(dia.plusDays(1).atStartOfDay()));
		
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
		LocalDateTime[] dia = diaCompleto(fecha);

		// Una fecha mal escrita nunca coincidía con ninguna reserva.
		if(dia == null)
			return new ArrayList<Reserva>();

		Collection<Reserva> result = reservaRepository.search(empresario.getId(), dia[0], dia[1]);
		Assert.notNull(result);

		return result;
	}

	// [inicio del día, inicio del día siguiente) para una fecha en formato
	// dd/MM/aaaa o aaaa-MM-dd (el de <input type="date">), o null si no es
	// una fecha válida.
	private static LocalDateTime[] diaCompleto(String fecha){
		LocalDate dia = null;

		if(fecha == null)
			return null;
		for(DateTimeFormatter formato : new DateTimeFormatter[] { DIA_ES, DateTimeFormatter.ISO_LOCAL_DATE }){
			try {
				dia = LocalDate.parse(fecha.trim(), formato);
				break;
			} catch (DateTimeParseException e) {
				// se prueba el siguiente formato
			}
		}
		if(dia == null)
			return null;

		return new LocalDateTime[] { dia.atStartOfDay(), dia.plusDays(1).atStartOfDay() };
	}
	
	// Other business methods ------------------------------------------------
	
	
	public Reserva findOneToEdit(int reservaId){
		Reserva result = reservaRepository.findById(reservaId).orElse(null);
		checkPrincipal(result);
		
		return result;
	}
	
	public Reserva findOne(int reservaId){
		Reserva result = reservaRepository.findById(reservaId).orElse(null);
		
		return result;
	}
	
	public Reserva findOneToEditMesa(int reservaId){
		
		Reserva result = reservaRepository.findById(reservaId).orElse(null);
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

	// Fase 3: listados paginados en la base de datos ---------------------

	public Page<Reserva> findReservasPorEmpresario(Pageable pageable){
		int empresarioId = empresarioService.findByPrincipal().getId();

		return reservaRepository.findReservasPorEmpresario(empresarioId, pageable);
	}

	public Page<Reserva> findReservasByUsuario(Pageable pageable){
		Usuario usuario = usuarioService.findByPrincipal();

		return reservaRepository.findReservasPorUsuario(usuario.getId(), pageable);
	}

	public Page<Reserva> searchByDate(String fecha, Pageable pageable){
		Empresario empresario = empresarioService.findByPrincipal();
		LocalDateTime[] dia = diaCompleto(fecha);

		if(dia == null)
			return Page.empty(pageable);

		return reservaRepository.search(empresario.getId(), dia[0], dia[1], pageable);
	}

}
