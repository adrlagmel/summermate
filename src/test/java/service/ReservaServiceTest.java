package service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import services.NegocioService;
import services.ReservaService;
import utilities.AbstractTest;
import domain.Negocio;
import domain.Reserva;

/*
 * Tests de caracterización de ReservaService sobre los datos de
 * PopulateDatabase.xml. empresario1 tiene "Comida Fast" con reservas el
 * 10 de agosto y el 10 de octubre de 2016 (ojo: en PopulateDatabase.xml las
 * fechas van en formato MM/dd/yyyy; el buscador usa dd/MM/yyyy).
 */
@Transactional
public class ReservaServiceTest extends AbstractTest {

	@Autowired
	private ReservaService reservaService;

	@Autowired
	private NegocioService negocioService;

	@Test
	public void empresarioVeSusReservas() {
		authenticate("empresario1");

		assertEquals(2, reservaService.findReservasPorEmpresario().size());
	}

	@Test
	public void buscarReservasPorFecha() {
		authenticate("empresario1");

		assertEquals(1, reservaService.searchByDate("10/08/2016").size());
		assertEquals(1, reservaService.searchByDate("10/10/2016").size());
		assertTrue(reservaService.searchByDate("11/10/2016").isEmpty());
	}

	@Test
	public void fechaMalEscritaNoDevuelveNada() {
		authenticate("empresario1");

		assertTrue(reservaService.searchByDate("no es una fecha").isEmpty());
		assertTrue(reservaService.searchByDate("32/13/2016").isEmpty());
	}

	@Test
	public void usuarioReservaEnHorarioValido() {
		authenticate("usuario1");

		Reserva saved = reservaService.save(nuevaReserva(dentroDeDias(7, 13), 2));

		assertTrue(saved.getId() > 0);
	}

	@Test
	public void noSePuedeReservarEnElPasado() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("usuario1");

			reservaService.save(nuevaReserva(dentroDeDias(-7, 13), 2));
	
		});
	}

	// Solo se admiten reservas de 12:00 a 24:00.
	@Test
	public void noSePuedeReservarAntesDeMediodia() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("usuario1");

			reservaService.save(nuevaReserva(dentroDeDias(7, 10), 2));
	
		});
	}

	@Test
	public void usuarioNoPuedeVerReservaAjena() {
		assertThrows(IllegalArgumentException.class, () -> {
			authenticate("usuario1");
			int id = reservaService.findReservasByUsuario().iterator().next().getId();
			authenticate("usuario2");

			reservaService.findOneToEdit(id);
	
		});
	}

	private Reserva nuevaReserva(LocalDateTime fecha, int comensales) {
		Reserva reserva = reservaService.create();
		reserva.setNegocio(comidaFast());
		reserva.setFecha(fecha);
		reserva.setComensales(comensales);
		return reserva;
	}

	private Negocio comidaFast() {
		for (Negocio n : negocioService.findAll())
			if (n.getNombre().equals("Comida Fast"))
				return n;
		throw new AssertionError("No existe el negocio Comida Fast");
	}

	private static LocalDateTime dentroDeDias(int dias, int hora) {
		return LocalDate.now().plusDays(dias).atTime(hora, 0);
	}
}
