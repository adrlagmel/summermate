/* AgendaService.java
 *
 * Calendario de un negocio (Fase 6; sustituye a CalendarioNegocio). Decide si
 * cabe una reserva y calcula la ocupación de cada franja de un día.
 *
 * - Horario: se reserva desde horaApertura hasta antes de horaCierre. Si el
 *   cierre es anterior o igual a la apertura (00:00, 02:00...), el negocio
 *   cierra al día siguiente.
 * - Una reserva ocupa duracionReserva minutos desde su hora.
 * - Sin mesas, la suma de comensales que coinciden no puede pasar del aforo.
 * - Con mesas, cada reserva se queda la mesa libre más pequeña en la que cabe.
 * - Los días de Cierre no se reserva.
 */

package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Mesa;
import domain.Negocio;
import domain.Reserva;
import repositories.CierreRepository;
import repositories.MesaRepository;
import repositories.ReservaRepository;

@Service
@Transactional
public class AgendaService {

	public static final int MINUTOS_FRANJA = 30;

	public enum Motivo {
		PASADA, FUERA_DE_HORARIO, CERRADO, AFORO, SIN_MESA
	}

	// Reserva que no cabe. Es IllegalArgumentException para que el resto de
	// la aplicación la trate como cualquier regla de negocio incumplida.
	public static class ReservaRechazada extends IllegalArgumentException {

		private static final long	serialVersionUID	= 1L;

		public final Motivo			motivo;
		public final int			libres;


		public ReservaRechazada(Motivo motivo, int libres) {
			super("Reserva rechazada: " + motivo);
			this.motivo = motivo;
			this.libres = libres;
		}
	}

	// Ocupación de una franja: comensales que coinciden con ella, plazas
	// libres (sin mesas) o mesas libres (con mesas).
	public record Franja(LocalDateTime inicio, int comensales, int plazasLibres, Integer mesasLibres) {

		public boolean llena() {
			return mesasLibres != null ? mesasLibres == 0 : plazasLibres <= 0;
		}
	}


	@Autowired
	private ReservaRepository	reservaRepository;

	@Autowired
	private MesaRepository		mesaRepository;

	@Autowired
	private CierreRepository	cierreRepository;


	// Comprueba la reserva y, si el negocio tiene mesas, le asigna una. El
	// negocio debe estar bloqueado (NegocioService.bloquearParaReservar) para
	// que dos reservas a la vez no vean la misma ocupación.
	public void comprobarYAsignar(Reserva reserva, Negocio negocio) {
		LocalDateTime inicio = reserva.getFecha();
		int duracion = negocio.getDuracionReserva();

		if (inicio.isBefore(LocalDateTime.now()))
			throw new ReservaRechazada(Motivo.PASADA, 0);
		LocalDate jornada = jornada(negocio, inicio);
		if (jornada == null)
			throw new ReservaRechazada(Motivo.FUERA_DE_HORARIO, 0);
		if (cierreRepository.cerrado(negocio.getId(), jornada))
			throw new ReservaRechazada(Motivo.CERRADO, 0);

		LocalDateTime desde = inicio.minusMinutes(duracion);
		LocalDateTime hasta = inicio.plusMinutes(duracion);
		List<Mesa> mesas = mesaRepository.findByNegocio(negocio.getId());

		if (mesas.isEmpty()) {
			int libres = negocio.getAforo() - reservaRepository.comensalesSolapados(negocio.getId(), desde, hasta);
			if (reserva.getComensales() > libres)
				throw new ReservaRechazada(Motivo.AFORO, Math.max(libres, 0));
			reserva.setMesa(null);
		} else {
			Set<Integer> ocupadas = new HashSet<>(reservaRepository.mesasOcupadas(negocio.getId(), desde, hasta));
			Mesa elegida = null;
			for (Mesa m : mesas)
				if (m.getCapacidad() >= reserva.getComensales() && !ocupadas.contains(m.getId())) {
					elegida = m;
					break;
				}
			if (elegida == null)
				throw new ReservaRechazada(Motivo.SIN_MESA, 0);
			reserva.setMesa(elegida);
		}
	}

	// Día de apertura al que pertenece la hora, o null si el negocio está cerrado
	// a esa hora. Las horas de madrugada pueden ser de la jornada anterior.
	public LocalDate jornada(Negocio negocio, LocalDateTime hora) {
		for (LocalDate dia : new LocalDate[] { hora.toLocalDate(), hora.toLocalDate().minusDays(1) }) {
			LocalDateTime abre = abre(negocio, dia);
			if (!hora.isBefore(abre) && hora.isBefore(cierra(negocio, dia)))
				return dia;
		}
		return null;
	}

	public LocalDateTime abre(Negocio negocio, LocalDate dia) {
		return dia.atTime(negocio.getHoraApertura());
	}

	public LocalDateTime cierra(Negocio negocio, LocalDate dia) {
		LocalTime apertura = negocio.getHoraApertura();
		LocalTime cierre = negocio.getHoraCierre();
		return cierre.isAfter(apertura) ? dia.atTime(cierre) : dia.plusDays(1).atTime(cierre);
	}

	// Franjas de media hora de la jornada con su ocupación. Carga las reservas
	// que tocan la jornada en una sola consulta y cuenta en memoria.
	@Transactional(readOnly = true)
	public List<Franja> franjas(Negocio negocio, LocalDate dia) {
		int duracion = negocio.getDuracionReserva();
		LocalDateTime abre = abre(negocio, dia);
		LocalDateTime cierra = cierra(negocio, dia);
		List<Reserva> reservas = reservaRepository.delDia(negocio.getId(), abre.minusMinutes(duracion), cierra);
		int numeroMesas = mesaRepository.findByNegocio(negocio.getId()).size();

		List<Franja> result = new ArrayList<>();
		for (LocalDateTime f = abre; f.isBefore(cierra); f = f.plusMinutes(MINUTOS_FRANJA)) {
			LocalDateTime desde = f.minusMinutes(duracion);
			LocalDateTime hasta = f.plusMinutes(MINUTOS_FRANJA);
			int comensales = 0;
			Set<Integer> mesas = new HashSet<>();
			for (Reserva r : reservas)
				if (r.getFecha().isAfter(desde) && r.getFecha().isBefore(hasta)) {
					comensales += r.getComensales();
					if (r.getMesa() != null)
						mesas.add(r.getMesa().getId());
				}
			result.add(new Franja(f, comensales, negocio.getAforo() - comensales, numeroMesas == 0 ? null : numeroMesas - mesas.size()));
		}
		return result;
	}

	@Transactional(readOnly = true)
	public List<Reserva> reservasDeLaJornada(Negocio negocio, LocalDate dia) {
		return reservaRepository.delDia(negocio.getId(), abre(negocio, dia), cierra(negocio, dia));
	}

}
