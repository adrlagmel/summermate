/* DenunciasController.java
 *
 * Denuncias de valoraciones de negocio. Desde la Fase 6 puede denunciar
 * cualquier cliente (usuario o empresario), no solo el dueño del negocio.
 *   GET    /denuncias                       mis denuncias
 *   GET    /denuncias/nueva?valoracion={id} formulario
 *   POST   /denuncias                       crear
 *   GET    /denuncias/{id}/editar           formulario
 *   PUT    /denuncias/{id}                  guardar cambios
 *   DELETE /denuncias/{id}                  retirar
 * Antes vivían en /empresario/denuncias y /empresario/valoraciones/{id}/denuncias/nueva.
 */

package controllers.actor;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.DenunciaValoracion;
import domain.TipoDenuncia;
import domain.ValoracionNegocio;
import forms.DenunciaValoracionForm;
import services.DenunciaValoracionService;
import services.ValoracionNegocioService;

@Controller
@RequestMapping("/denuncias")
public class DenunciasController {

	@Autowired
	private DenunciaValoracionService	denunciaService;

	@Autowired
	private ValoracionNegocioService	valoracionNegocioService;

	@Autowired
	private SmartValidator				validator;


	@GetMapping
	public String lista(Model model) {
		model.addAttribute("denuncias", denunciaService.misDenuncias());

		return "denuncias/lista";
	}

	@GetMapping("/nueva")
	public String nueva(@RequestParam int valoracion, Model model) {
		return formulario(model, new DenunciaValoracionForm(denunciaService.create(valoracionNegocioService.findOne(valoracion))));
	}

	@PostMapping
	public String crear(@ModelAttribute("form") DenunciaValoracionForm form, BindingResult binding, @RequestParam int valoracion, Model model, RedirectAttributes redirect) {
		ValoracionNegocio vn = valoracionNegocioService.findOne(valoracion);

		form.setId(0);
		form.setValoracionNegocio(vn);
		return guardar(form, binding, model, redirect);
	}

	@GetMapping("/{id}/editar")
	public String editar(@PathVariable int id, Model model) {
		return formulario(model, new DenunciaValoracionForm(denunciaService.findOneToEdit(id)));
	}

	@PutMapping("/{id}")
	public String actualizar(@PathVariable int id, @ModelAttribute("form") DenunciaValoracionForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		DenunciaValoracion actual = denunciaService.findOneToEdit(id);

		form.setId(id);
		form.setValoracionNegocio(actual.getValoracionNegocio());
		return guardar(form, binding, model, redirect);
	}

	@DeleteMapping("/{id}")
	public String borrar(@PathVariable int id, RedirectAttributes redirect) {
		denunciaService.delete(denunciaService.findOneToEdit(id));
		redirect.addFlashAttribute("aviso", "fase5.borrado");

		return "redirect:/denuncias";
	}

	private String guardar(DenunciaValoracionForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		validator.validate(form, binding);
		if (binding.hasErrors())
			return formulario(model, form);

		denunciaService.save(denunciaService.reconstruct(form));
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/denuncias";
	}

	private static String formulario(Model model, DenunciaValoracionForm form) {
		model.addAttribute("form", form);
		model.addAttribute("tipos", TipoDenuncia.values());

		return "denuncias/formulario";
	}

}
