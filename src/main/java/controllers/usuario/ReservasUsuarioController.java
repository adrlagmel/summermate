/* ReservasUsuarioController.java
 *
 * Reservas del usuario. Rutas antiguas:
 *   /reserva/usuario/lista.do              -> GET /mis/reservas
 *   /reserva/usuario/create.do?negocioId=  -> GET /negocios/{id}/reservas/nueva y POST /negocios/{id}/reservas
 *   /reserva/usuario/delete.do             -> DELETE /mis/reservas/{id}
 *
 * El negocio sale de la ruta, no de un campo oculto del formulario.
 */

package controllers.usuario;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.Paginacion;
import domain.Negocio;
import forms.ReservaForm;
import services.AgendaService;
import services.NegocioService;
import services.ReservaService;
import utilities.Assert;

@Controller
public class ReservasUsuarioController {

	@Autowired
	private ReservaService				reservaService;

	@Autowired
	private NegocioService				negocioService;


	@Autowired
	private SmartValidator				validator;


	@GetMapping("/mis/reservas")
	public String lista(Paginacion paginacion, Model model) {
		model.addAttribute("reservas", reservaService.findReservasByUsuario(paginacion.reservas()));

		return "reservas/lista";
	}

	@GetMapping("/negocios/{id}/reservas/nueva")
	public String nueva(@PathVariable int id, Model model) {
		Negocio negocio = negocioService.findOneToDisplay(id);
		ReservaForm form = new ReservaForm();

		Assert.isTrue(negocio.getNegocioActivo());
		form.setNegocio(negocio);
		form.setReservaId(0);
		form.setComensales(2);
		form.setFumadores(false);

		return formulario(model, form);
	}

	@PostMapping("/negocios/{id}/reservas")
	public String crear(@PathVariable int id, @ModelAttribute("form") ReservaForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		form.setNegocio(negocioService.findOneToDisplay(id));
		form.setReservaId(0);
		validator.validate(form, binding);
		if (binding.hasErrors())
			return formulario(model, form);

		if (!form.getFecha().isAfter(LocalDateTime.now())) {
			model.addAttribute("message", "booking.dates.error");
			return formulario(model, form);
		}

		try {
			reservaService.save(reservaService.reconstruct(form));
		} catch (AgendaService.ReservaRechazada rechazada) {
			// Se dice por qué no cabe y, si es por aforo, cuántas plazas quedan
			switch (rechazada.motivo) {
				case AFORO -> {
					model.addAttribute("message", "booking.error.aforo");
					model.addAttribute("libres", rechazada.libres);
				}
				case SIN_MESA -> model.addAttribute("message", "reserva.error.sinMesa");
				case CERRADO -> model.addAttribute("message", "reserva.error.cerrado");
				default -> model.addAttribute("message", "booking.commit.error");
			}
			return formulario(model, form);
		}
		redirect.addFlashAttribute("aviso", "fase5.reserva.ok");
		return "redirect:/mis/reservas";
	}

	@DeleteMapping("/mis/reservas/{id}")
	public String borrar(@PathVariable int id, RedirectAttributes redirect) {
		reservaService.delete(reservaService.findOneToEdit(id));
		redirect.addFlashAttribute("aviso", "fase5.borrado");

		return "redirect:/mis/reservas";
	}

	private static String formulario(Model model, ReservaForm form) {
		model.addAttribute("form", form);

		return "reservas/nueva";
	}

}
