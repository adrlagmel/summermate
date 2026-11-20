package service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import services.NegocioService;
import services.ReservaService;
import utilities.AbstractTest;
import domain.Negocio;
import domain.Reserva;

/*
 * Reservas simultáneas contra el aforo. A diferencia del resto, este test no
 * es @Transactional: cada hilo hace su propia transacción real, como dos
 * usuarios a la vez. Se deja "Comida Fast" con aforo 20; cinco reservas
 * simultáneas de 10 comensales (el máximo) para la misma hora: solo caben dos.
 */
public class AforoConcurrenteTest extends AbstractTest {

	private static final int			HILOS		= 5;
	private static final int			COMENSALES	= 10;

	@Autowired
	private ReservaService				reservaService;

	@Autowired
	private NegocioService				negocioService;

	@Autowired
	private PlatformTransactionManager	transactionManager;

	@Test
	public void reservasSimultaneasNoSuperanElAforo() throws Exception {
		final TransactionTemplate tx = new TransactionTemplate(transactionManager);
		final int negocioId = tx.execute(status -> conAforo(20).getId());
		final LocalDateTime fecha = dentroDeUnaSemanaALas(14);
		final CountDownLatch salida = new CountDownLatch(1);
		ExecutorService pool = Executors.newFixedThreadPool(HILOS);
		List<Future<Boolean>> resultados = new ArrayList<Future<Boolean>>();

		for (int i = 0; i < HILOS; i++)
			resultados.add(pool.submit(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					authenticate("usuario1");
					salida.await();
					try {
						tx.execute(status -> {
							Reserva reserva = reservaService.create();
							reserva.setNegocio(negocioService.findOneToDisplay(negocioId));
							reserva.setFecha(fecha);
							reserva.setComensales(COMENSALES);
							return reservaService.save(reserva);
						});
						return true;
					} catch (IllegalArgumentException aforoCompleto) {
						return false;
					}
				}
			}));

		salida.countDown();
		int aceptadas = 0;
		for (Future<Boolean> r : resultados)
			if (r.get())
				aceptadas++;
		pool.shutdown();

		assertEquals(2, aceptadas);
	}

	// Una reserva sola tampoco puede superar el aforo (antes la primera de
	// cada franja no se comprobaba).
	@Test
	public void unaSolaReservaNoPuedeSuperarElAforo() {
		assertThrows(IllegalArgumentException.class, () -> {
			TransactionTemplate tx = new TransactionTemplate(transactionManager);
			authenticate("usuario1");

			tx.execute(status -> {
				Reserva reserva = reservaService.create();
				reserva.setNegocio(conAforo(8));
				reserva.setFecha(dentroDeUnaSemanaALas(15));
				reserva.setComensales(10);
				return reservaService.save(reserva);
			});
	
		});
	}

	private Negocio conAforo(int aforo) {
		Negocio negocio = comidaFast();
		negocio.setAforo(aforo);
		return negocio;
	}

	private Negocio comidaFast() {
		for (Negocio n : negocioService.findAll())
			if (n.getNombre().equals("Comida Fast"))
				return n;
		throw new AssertionError("No existe Comida Fast");
	}

	private static LocalDateTime dentroDeUnaSemanaALas(int hora) {
		return LocalDate.now().plusDays(7).atTime(hora, 0);
	}
}
