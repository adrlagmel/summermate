/* EmpresarioController.java
 *
 * Reservas, denuncias y petición de negocio del empresario. Rutas antiguas:
 *   /reserva/empresario/lista.do y search.do?s=      -> GET /empresario/reservas?fecha=
 *   /denunciaValoracion/empresario/list.do           -> GET /empresario/denuncias
 *   /denunciaValoracion/empresario/listDenuncias.do  -> GET /empresario/valoraciones/{id}/denuncias
 *   /denunciaValoracion/empresario/create.do         -> GET /empresario/valoraciones/{id}/denuncias/nueva y POST /empresario/valoraciones/{id}/denuncias
 *   /denunciaValoracion/empresario/edit.do           -> GET /empresario/denuncias/{id}/editar y PUT /empresario/denuncias/{id}
 *   /denunciaValoracion/empresario/borrar.do         -> DELETE /empresario/denuncias/{id}
 *   /peticionNegocio/empresario/list.do y enviar.do  -> GET /empresario/peticion y POST /empresario/peticion
 */

package controllers.empresario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.Paginacion;
import domain.DenunciaValoracion;
import domain.TipoDenuncia;
import forms.DenunciaValoracionForm;
import forms.PeticionNegocioForm;
import services.DenunciaValoracionService;
import services.EmpresarioService;
import services.PeticionNegocioService;
import services.ReservaService;
import services.ValoracionNegocioService;

@Controller
@RequestMapping("/empresario")
public class EmpresarioController {

	@Autowired
	private ReservaService				reservaService;

	@Autowired
	private DenunciaValoracionService	denunciaService;

	@Autowired
	private ValoracionNegocioService	valoracionNegocioService;

	@Autowired
	private PeticionNegocioService		peticionService;

	@Autowired
	private EmpresarioService			empresarioService;

	@Autowired
	private SmartValidator				validator;


	// Reservas --------------------------------------------------------------

	@GetMapping("/reservas")
	public String reservas(@RequestParam(required = false) String fecha, Paginacion paginacion, Model model) {
		if (StringUtils.hasText(fecha))
			model.addAttribute("reservas", reservaService.searchByDate(fecha, paginacion.reservas()));
		else
			model.addAttribute("reservas", reservaService.findReservasPorEmpresario(paginacion.reservas()));
		model.addAttribute("fecha", fecha);
		model.addAttribute("deEmpresario", true);

		return "reservas/lista";
	}

	// Denuncias -------------------------------------------------------------

	@GetMapping("/denuncias")
	public String denuncias(Model model) {
		model.addAttribute("denuncias", denunciaService.denunciasDelEmpresario());

		return "denuncias/lista";
	}

	@GetMapping("/valoraciones/{id}/denuncias")
	public String denunciasDeValoracion(@PathVariable int id, Model model) {
		model.addAttribute("valoracion", valoracionNegocioService.findOne(id));
		model.addAttribute("denuncias", denunciaService.denunciasDeUnaValoracion(id));

		return "denuncias/lista";
	}

	@GetMapping("/valoraciones/{id}/denuncias/nueva")
	public String nuevaDenuncia(@PathVariable int id, Model model) {
		return formulario(model, new DenunciaValoracionForm(denunciaService.create(valoracionNegocioService.findOne(id))));
	}

	@PostMapping("/valoraciones/{id}/denuncias")
	public String crearDenuncia(@PathVariable int id, @ModelAttribute("form") DenunciaValoracionForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		form.setId(0);
		form.setValoracionNegocio(valoracionNegocioService.findOne(id));
		return guardar(form, binding, model, redirect);
	}

	@GetMapping("/denuncias/{id}/editar")
	public String editarDenuncia(@PathVariable int id, Model model) {
		return formulario(model, new DenunciaValoracionForm(denunciaService.findOneToEdit(id)));
	}

	@PutMapping("/denuncias/{id}")
	public String actualizarDenuncia(@PathVariable int id, @ModelAttribute("form") DenunciaValoracionForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		DenunciaValoracion actual = denunciaService.findOneToEdit(id);

		form.setId(id);
		form.setValoracionNegocio(actual.getValoracionNegocio());
		return guardar(form, binding, model, redirect);
	}

	@DeleteMapping("/denuncias/{id}")
	public String borrarDenuncia(@PathVariable int id, RedirectAttributes redirect) {
		denunciaService.delete(denunciaService.findOneToEdit(id));
		redirect.addFlashAttribute("aviso", "fase5.borrado");

		return "redirect:/empresario/denuncias";
	}

	private String guardar(DenunciaValoracionForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		validator.validate(form, binding);
		if (binding.hasErrors())
			return formulario(model, form);

		denunciaService.save(denunciaService.reconstruct(form));
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/empresario/denuncias";
	}

	private static String formulario(Model model, DenunciaValoracionForm form) {
		model.addAttribute("form", form);
		model.addAttribute("tipos", TipoDenuncia.values());

		return "denuncias/formulario";
	}

	// Petición de negocio ---------------------------------------------------

	@GetMapping("/peticion")
	public String peticion(Model model) {
		return vistaPeticion(model, new PeticionNegocioForm());
	}

	@PostMapping("/peticion")
	public String enviarPeticion(@ModelAttribute("form") @Validated PeticionNegocioForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		if (binding.hasErrors())
			return vistaPeticion(model, form);

		peticionService.save(peticionService.reconstruct(form));
		redirect.addFlashAttribute("aviso", "fase5.peticion.enviada");
		return "redirect:/empresario/peticion";
	}

	private String vistaPeticion(Model model, PeticionNegocioForm form) {
		model.addAttribute("peticion", peticionService.findByEmpresarioId(empresarioService.findByPrincipal().getId()));
		model.addAttribute("puedeEnviar", peticionService.canCreatePeticionNegocio());
		model.addAttribute("form", form);

		return "empresario/peticion";
	}

}
