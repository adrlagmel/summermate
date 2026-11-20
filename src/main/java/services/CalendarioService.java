package services;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Cierre;
import domain.Mesa;
import domain.Negocio;
import repositories.CierreRepository;
import repositories.MesaRepository;
import repositories.NegocioRepository;
import repositories.ReservaRepository;
import utilities.Assert;

/*
 * Lo que el empresario configura del calendario de su negocio: horario,
 * mesas y días cerrados. Cada método comprueba que el negocio es suyo.
 */
@Service
@Transactional
public class CalendarioService {

	@Autowired
	private NegocioService		negocioService;

	@Autowired
	private NegocioRepository	negocioRepository;

	@Autowired
	private MesaRepository		mesaRepository;

	@Autowired
	private CierreRepository	cierreRepository;

	@Autowired
	private ReservaRepository	reservaRepository;

	@Autowired
	private AgendaService		agendaService;


	public void cambiarHorario(int negocioId, LocalTime apertura, LocalTime cierre, int duracion) {
		Negocio negocio = negocioService.findOneToEdit(negocioId);
		Assert.notNull(apertura);
		Assert.notNull(cierre);
		Assert.isTrue(!apertura.equals(cierre) || cierre.equals(LocalTime.MIDNIGHT));
		Assert.isTrue(duracion >= 15 && duracion <= 240);

		negocio.setHoraApertura(apertura);
		negocio.setHoraCierre(cierre);
		negocio.setDuracionReserva(duracion);
		negocioRepository.save(negocio);
	}

	public Mesa anadirMesa(int negocioId, String nombre, int capacidad) {
		Negocio negocio = negocioService.findOneToEdit(negocioId);
		Assert.isTrue(nombre != null && !nombre.isBlank() && nombre.length() <= 40);
		Assert.isTrue(capacidad >= 1 && capacidad <= 20);

		Mesa mesa = new Mesa();
		mesa.setNegocio(negocio);
		mesa.setNombre(nombre.trim());
		mesa.setCapacidad(capacidad);
		return mesaRepository.save(mesa);
	}

	// Una mesa con reservas pendientes no se quita: esas reservas la perderían.
	public boolean borrarMesa(int negocioId, int mesaId) {
		Negocio negocio = negocioService.findOneToEdit(negocioId);
		Mesa mesa = mesaRepository.findById(mesaId).orElse(null);
		Assert.notNull(mesa);
		Assert.isTrue(mesa.getNegocio().getId() == negocio.getId());

		boolean conReservas = negocio.getReservas().stream()
			.anyMatch(r -> r.getMesa() != null && r.getMesa().getId() == mesaId && r.getFecha().isAfter(java.time.LocalDateTime.now()));
		if (conReservas)
			return false;
		mesaRepository.delete(mesa);
		return true;
	}

	// No se cierra un día que ya tiene reservas: habría que avisar y anularlas.
	public boolean cerrar(int negocioId, LocalDate fecha, String motivo) {
		Negocio negocio = negocioService.findOneToEdit(negocioId);
		Assert.notNull(fecha);
		Assert.isTrue(!fecha.isBefore(LocalDate.now()));
		Assert.isTrue(motivo == null || motivo.length() <= 120);

		if (cierreRepository.cerrado(negocioId, fecha))
			return true;
		if (!agendaService.reservasDeLaJornada(negocio, fecha).isEmpty())
			return false;

		Cierre cierre = new Cierre();
		cierre.setNegocio(negocio);
		cierre.setFecha(fecha);
		cierre.setMotivo(motivo == null || motivo.isBlank() ? null : motivo.trim());
		cierreRepository.save(cierre);
		return true;
	}

	public void abrir(int negocioId, int cierreId) {
		Negocio negocio = negocioService.findOneToEdit(negocioId);
		Cierre cierre = cierreRepository.findById(cierreId).orElse(null);
		Assert.notNull(cierre);
		Assert.isTrue(cierre.getNegocio().getId() == negocio.getId());

		cierreRepository.delete(cierre);
	}

}
