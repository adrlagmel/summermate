/* ValoracionesUsuarioController.java
 *
 * Valoraciones de playas y negocios del usuario. Rutas antiguas:
 *   /valoracionPlaya/usuario/list.do                    -> GET /mis/valoraciones/playas
 *   /valoracionPlaya/usuario/create.do?playaId=         -> GET /playas/{id}/valoraciones/nueva y POST /playas/{id}/valoraciones
 *   /valoracionPlaya/usuario/edit.do?valoracionPlayaId= -> GET /mis/valoraciones/playas/{id}/editar y PUT /mis/valoraciones/playas/{id}
 *   /valoracionPlaya/usuario/borrar.do                  -> DELETE /mis/valoraciones/playas/{id}
 *   /valoracionNegocio/usuario/list.do                  -> GET /mis/valoraciones/negocios
 *   /valoracionNegocio/usuario/create.do?reservaId=     -> GET /mis/reservas/{id}/valoracion/nueva y POST /mis/reservas/{id}/valoracion
 *   /valoracionNegocio/usuario/edit.do                  -> GET /mis/valoraciones/negocios/{id}/editar y PUT /mis/valoraciones/negocios/{id}
 *   /valoracionNegocio/usuario/borrar.do                -> DELETE /mis/valoraciones/negocios/{id}
 *   /valoracionNegocio/usuario/display.do?reservaId=    -> (la valoración se ve en la ficha del negocio)
 *
 * La playa, la reserva y el id de la valoración salen de la ruta; el
 * formulario solo aporta título, comentario, puntuación y versión.
 */

package controllers.usuario;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Playa;
import domain.Reserva;
import domain.ValoracionNegocio;
import domain.ValoracionPlaya;
import forms.ValoracionNegocioForm;
import forms.ValoracionPlayaForm;
import services.PlayaService;
import services.ReservaService;
import services.ValoracionNegocioService;
import services.ValoracionPlayaService;
import utilities.Assert;

@Controller
public class ValoracionesUsuarioController {

	private static final String			VISTA_PLAYA		= "valoraciones/playa";
	private static final String			VISTA_NEGOCIO	= "valoraciones/negocio";

	@Autowired
	private ValoracionPlayaService		valoracionPlayaService;

	@Autowired
	private ValoracionNegocioService	valoracionNegocioService;

	@Autowired
	private PlayaService				playaService;

	@Autowired
	private ReservaService				reservaService;

	@Autowired
	private SmartValidator				validator;


	// Playas ----------------------------------------------------------------

	@GetMapping("/mis/valoraciones/playas")
	public String playas(Model model) {
		model.addAttribute("valoraciones", valoracionPlayaService.valoracionesDeUsuario());

		return "valoraciones/mis-playas";
	}

	@GetMapping("/playas/{id}/valoraciones/nueva")
	public String nuevaPlaya(@PathVariable int id, Model model) {
		Playa playa = playaService.findOne(id);
		Assert.notNull(playa);

		return vistaPlaya(model, new ValoracionPlayaForm(valoracionPlayaService.create(playa)));
	}

	@PostMapping("/playas/{id}/valoraciones")
	public String crearPlaya(@PathVariable int id, @ModelAttribute("form") ValoracionPlayaForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		form.setId(0);
		form.setPlaya(playaService.findOne(id));
		return guardarPlaya(form, binding, model, redirect);
	}

	@GetMapping("/mis/valoraciones/playas/{id}/editar")
	public String editarPlaya(@PathVariable int id, Model model) {
		return vistaPlaya(model, new ValoracionPlayaForm(valoracionPlayaService.findOneToEdit(id)));
	}

	@PutMapping("/mis/valoraciones/playas/{id}")
	public String actualizarPlaya(@PathVariable int id, @ModelAttribute("form") ValoracionPlayaForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		ValoracionPlaya actual = valoracionPlayaService.findOneToEdit(id);

		form.setId(id);
		form.setPlaya(actual.getPlaya());
		return guardarPlaya(form, binding, model, redirect);
	}

	@DeleteMapping("/mis/valoraciones/playas/{id}")
	public String borrarPlaya(@PathVariable int id, RedirectAttributes redirect) {
		valoracionPlayaService.delete(valoracionPlayaService.findOneToEdit(id));
		redirect.addFlashAttribute("aviso", "fase5.borrado");

		return "redirect:/mis/valoraciones/playas";
	}

	private String guardarPlaya(ValoracionPlayaForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		validator.validate(form, binding);
		if (binding.hasErrors())
			return vistaPlaya(model, form);

		valoracionPlayaService.save(valoracionPlayaService.reconstruct(form));
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/mis/valoraciones/playas";
	}

	private static String vistaPlaya(Model model, ValoracionPlayaForm form) {
		model.addAttribute("form", form);

		return VISTA_PLAYA;
	}

	// Negocios --------------------------------------------------------------

	@GetMapping("/mis/valoraciones/negocios")
	public String negocios(Model model) {
		model.addAttribute("valoraciones", valoracionNegocioService.valoracionesNegocioDeUsuario());

		return "valoraciones/mis-negocios";
	}

	@GetMapping("/mis/reservas/{id}/valoracion/nueva")
	public String nuevaNegocio(@PathVariable int id, Model model) {
		Reserva reserva = reservaService.findOneToEdit(id);

		return vistaNegocio(model, new ValoracionNegocioForm(valoracionNegocioService.create(reserva)));
	}

	@PostMapping("/mis/reservas/{id}/valoracion")
	public String crearNegocio(@PathVariable int id, @ModelAttribute("form") ValoracionNegocioForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		form.setId(0);
		form.setReserva(reservaService.findOneToEdit(id));
		return guardarNegocio(form, binding, model, redirect);
	}

	@GetMapping("/mis/valoraciones/negocios/{id}/editar")
	public String editarNegocio(@PathVariable int id, Model model) {
		return vistaNegocio(model, new ValoracionNegocioForm(valoracionNegocioService.findOneToEdit(id)));
	}

	@PutMapping("/mis/valoraciones/negocios/{id}")
	public String actualizarNegocio(@PathVariable int id, @ModelAttribute("form") ValoracionNegocioForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		ValoracionNegocio actual = valoracionNegocioService.findOneToEdit(id);

		form.setId(id);
		form.setReserva(actual.getReserva());
		return guardarNegocio(form, binding, model, redirect);
	}

	@DeleteMapping("/mis/valoraciones/negocios/{id}")
	public String borrarNegocio(@PathVariable int id, RedirectAttributes redirect) {
		valoracionNegocioService.delete(valoracionNegocioService.findOneToEdit(id));
		redirect.addFlashAttribute("aviso", "fase5.borrado");

		return "redirect:/mis/valoraciones/negocios";
	}

	private String guardarNegocio(ValoracionNegocioForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		validator.validate(form, binding);
		if (binding.hasErrors())
			return vistaNegocio(model, form);

		valoracionNegocioService.save(valoracionNegocioService.reconstruct(form));
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/mis/valoraciones/negocios";
	}

	private static String vistaNegocio(Model model, ValoracionNegocioForm form) {
		model.addAttribute("form", form);

		return VISTA_NEGOCIO;
	}

}
