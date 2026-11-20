/* NegociosEmpresarioController.java
 *
 * Negocios del empresario. Rutas antiguas:
 *   /negocio/empresario/list.do                -> GET /empresario/negocios
 *   /negocio/empresario/register.do            -> GET /empresario/negocios/nuevo y POST /empresario/negocios
 *   /negocio/empresario/edit.do?negocioId=     -> GET /empresario/negocios/{id}/editar y PUT /empresario/negocios/{id}
 *   /negocio/empresario/suspender.do           -> POST /empresario/negocios/{id}/suspension
 *   /negocio/empresario/alta.do                -> DELETE /empresario/negocios/{id}/suspension
 *   /negocio/empresario/uploadImageNegocio.do  -> GET y POST /empresario/negocios/{id}/imagen
 *   /valoracionNegocio/empresario/listValoraciones.do -> GET /empresario/negocios/{id}/valoraciones
 *   /negocio/empresario/search.do              -> GET /negocios?ciudad= (era la misma búsqueda pública)
 *
 * /calendarioNegocio/empresario/deleteDates.do no se migra: ninguna página
 * enlazaba con ella y CalendarioNegocioService.save() no guarda nada. Queda
 * pendiente de la revisión del calendario.
 */

package controllers.empresario;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.Paginacion;
import controllers.SubidaImagen;
import domain.EstadoPeticion;
import domain.Negocio;
import domain.PeticionNegocio;
import forms.NegocioForm;
import services.EmpresarioService;
import services.NegocioService;
import services.PeticionNegocioService;
import services.PlayaService;
import utilities.Assert;

@Controller
@RequestMapping("/empresario/negocios")
public class NegociosEmpresarioController {

	@Autowired
	private NegocioService			negocioService;

	@Autowired
	private EmpresarioService		empresarioService;

	@Autowired
	private PeticionNegocioService	peticionService;

	@Autowired
	private PlayaService			playaService;

	@Autowired
	private SmartValidator			validator;


	@GetMapping
	public String lista(Paginacion paginacion, Model model) {
		model.addAttribute("negocios", negocioService.findByEmpresario(paginacion.negocios()));
		model.addAttribute("mios", true);
		model.addAttribute("puedeCrear", peticionAceptada());

		return "negocios/lista";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model, RedirectAttributes redirect) {
		if (!peticionAceptada()) {
			redirect.addFlashAttribute("message", "negocio.register.necesario");
			return "redirect:/empresario/peticion";
		}
		return formulario(model, new NegocioForm(negocioService.create()));
	}

	@PostMapping
	public String crear(@ModelAttribute("form") NegocioForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		form.setId(0);
		return guardar(form, binding, model, redirect);
	}

	@GetMapping("/{id}/editar")
	public String editar(@PathVariable int id, Model model) {
		return formulario(model, new NegocioForm(negocioService.findOneToEdit(id)));
	}

	@PutMapping("/{id}")
	public String actualizar(@PathVariable int id, @ModelAttribute("form") NegocioForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		negocioService.findOneToEdit(id);
		form.setId(id);
		return guardar(form, binding, model, redirect);
	}

	@PostMapping("/{id}/suspension")
	public String suspender(@PathVariable int id, RedirectAttributes redirect) {
		Negocio negocio = negocioService.findOneToEdit(id);

		Assert.isTrue(negocio.getNegocioActivo());
		negocioService.suspenderNegocio(negocio);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/empresario/negocios";
	}

	@DeleteMapping("/{id}/suspension")
	public String reactivar(@PathVariable int id, RedirectAttributes redirect) {
		Negocio negocio = negocioService.findOneToEdit(id);

		Assert.isTrue(!negocio.getNegocioActivo());
		negocioService.altaNegocio(negocio);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/empresario/negocios";
	}

	@GetMapping("/{id}/imagen")
	public String imagen(@PathVariable int id, Model model) {
		return vistaImagen(model, negocioService.findOneToEdit(id));
	}

	@PostMapping("/{id}/imagen")
	public String subirImagen(@PathVariable int id, @RequestParam("foto") MultipartFile foto, Model model, RedirectAttributes redirect) throws IOException {
		Negocio negocio = negocioService.findOneToEdit(id);

		try {
			negocioService.addImageToNegocio(id, foto.getBytes());
		} catch (IllegalArgumentException formatoNoAdmitido) {
			model.addAttribute("message", "fase5.imagen.formato");
			return vistaImagen(model, negocio);
		}
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/empresario/negocios";
	}

	// Las valoraciones (con el botón de denunciar) están en la ficha del negocio
	@GetMapping("/{id}/valoraciones")
	public String valoraciones(@PathVariable int id) {
		negocioService.findOneToEdit(id);

		return "redirect:/negocios/" + id + "#valoraciones";
	}

	private String guardar(NegocioForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		validator.validate(form, binding);
		if (binding.hasErrors())
			return formulario(model, form);

		negocioService.save(negocioService.reconstruct(form));
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/empresario/negocios";
	}

	private String formulario(Model model, NegocioForm form) {
		model.addAttribute("form", form);
		model.addAttribute("playas", playaService.findAllBeaches());

		return "negocios/formulario";
	}

	private static String vistaImagen(Model model, Negocio negocio) {
		model.addAttribute("imagen", new SubidaImagen(negocio.getNombre(), "/empresario/negocios/" + negocio.getId() + "/imagen", "negocios", negocio.getId(), negocio.getImagen() != null, "/empresario/negocios"));

		return SubidaImagen.VISTA;
	}

	private boolean peticionAceptada() {
		PeticionNegocio peticion = peticionService.findPeticionNegocioPorEmpresario(empresarioService.findByPrincipal());

		return peticion != null && peticion.getEstado() == EstadoPeticion.ACEPTADO;
	}

}
