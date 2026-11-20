/* CalendarioController.java
 *
 * Calendario del negocio (Fase 6; antes /calendarioNegocio/empresario/*,
 * que no funcionaba):
 *   GET    /empresario/negocios/{id}/calendario?dia=   ocupación por franjas y reservas del día
 *   PUT    /empresario/negocios/{id}/horario            horario y duración de las reservas
 *   POST   /empresario/negocios/{id}/mesas              añadir mesa
 *   DELETE /empresario/negocios/{id}/mesas/{mesa}       quitar mesa
 *   POST   /empresario/negocios/{id}/cierres            cerrar un día
 *   DELETE /empresario/negocios/{id}/cierres/{cierre}   volver a abrirlo
 */

package controllers.empresario;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Negocio;
import repositories.CierreRepository;
import repositories.MesaRepository;
import services.AgendaService;
import services.CalendarioService;
import services.NegocioService;

@Controller
@RequestMapping("/empresario/negocios/{id}")
public class CalendarioController {

	@Autowired
	private NegocioService		negocioService;

	@Autowired
	private AgendaService		agendaService;

	@Autowired
	private CalendarioService	calendarioService;

	@Autowired
	private MesaRepository		mesaRepository;

	@Autowired
	private CierreRepository	cierreRepository;


	@GetMapping("/calendario")
	public String calendario(@PathVariable int id, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia, Model model) {
		Negocio negocio = negocioService.findOneToEdit(id);
		LocalDate hoy = LocalDate.now();
		if (dia == null)
			dia = hoy;

		model.addAttribute("negocio", negocio);
		model.addAttribute("dia", dia);
		model.addAttribute("hoy", hoy);
		model.addAttribute("cerrado", cierreRepository.cerrado(id, dia));
		model.addAttribute("franjas", agendaService.franjas(negocio, dia));
		model.addAttribute("reservas", agendaService.reservasDeLaJornada(negocio, dia));
		model.addAttribute("mesas", mesaRepository.findByNegocio(id));
		model.addAttribute("cierres", cierreRepository.proximos(id, hoy));

		return "negocios/calendario";
	}

	@PutMapping("/horario")
	public String horario(@PathVariable int id, @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horaApertura,
		@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horaCierre, @RequestParam int duracionReserva, RedirectAttributes redirect) {
		calendarioService.cambiarHorario(id, horaApertura, horaCierre, duracionReserva);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return volver(id, null);
	}

	@PostMapping("/mesas")
	public String anadirMesa(@PathVariable int id, @RequestParam String nombre, @RequestParam int capacidad, RedirectAttributes redirect) {
		calendarioService.anadirMesa(id, nombre, capacidad);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return volver(id, null);
	}

	@DeleteMapping("/mesas/{mesa}")
	public String borrarMesa(@PathVariable int id, @PathVariable int mesa, RedirectAttributes redirect) {
		if (calendarioService.borrarMesa(id, mesa))
			redirect.addFlashAttribute("aviso", "fase5.borrado");
		else
			redirect.addFlashAttribute("message", "calendario.mesa.conReservas");
		return volver(id, null);
	}

	@PostMapping("/cierres")
	public String cerrar(@PathVariable int id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
		@RequestParam(required = false) String motivo, RedirectAttributes redirect) {
		if (calendarioService.cerrar(id, fecha, motivo))
			redirect.addFlashAttribute("aviso", "fase5.guardado");
		else
			redirect.addFlashAttribute("message", "calendario.cierre.conReservas");
		return volver(id, fecha);
	}

	@DeleteMapping("/cierres/{cierre}")
	public String abrir(@PathVariable int id, @PathVariable int cierre, RedirectAttributes redirect) {
		calendarioService.abrir(id, cierre);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return volver(id, null);
	}

	private static String volver(int id, LocalDate dia) {
		return "redirect:/empresario/negocios/" + id + "/calendario" + (dia == null ? "" : "?dia=" + dia);
	}

}
