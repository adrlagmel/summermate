/* EventosEmpresarioController.java
 *
 * Eventos del empresario. Rutas antiguas:
 *   /evento/empresario/list.do                -> GET /empresario/eventos
 *   /evento/empresario/listActivos.do, ...    -> GET /eventos?cuando=proximos|pasados (eran los públicos)
 *   /evento/empresario/register.do            -> GET /empresario/eventos/nuevo y POST /empresario/eventos
 *   /evento/empresario/edit.do?eventoId=      -> GET /empresario/eventos/{id}/editar y PUT /empresario/eventos/{id}
 *   /evento/empresario/uploadImageEvento.do   -> GET y POST /empresario/eventos/{id}/imagen
 *   /evento/empresario/listParticipantes.do   -> GET /empresario/eventos/{id}/participantes
 */

package controllers.empresario;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
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
import domain.Evento;
import domain.Negocio;
import forms.EventoForm;
import services.EmpresarioService;
import services.EventoService;
import services.NegocioService;
import utilities.Assert;

@Controller
@RequestMapping("/empresario/eventos")
public class EventosEmpresarioController {

	@Autowired
	private EventoService		eventoService;

	@Autowired
	private NegocioService		negocioService;

	@Autowired
	private EmpresarioService	empresarioService;

	@Autowired
	private SmartValidator		validator;


	@GetMapping
	public String lista(Paginacion paginacion, Model model) {
		model.addAttribute("eventos", eventoService.findByEmpresario(paginacion.eventos()));
		model.addAttribute("inscritos", Collections.emptySet());
		model.addAttribute("mios", true);
		model.addAttribute("sinFiltro", true);
		model.addAttribute("titulo", "master.page.eventos.mylist");

		return "eventos/lista";
	}

	// Como antes: sin ningún negocio activo no se pueden crear eventos
	@GetMapping("/nuevo")
	public String nuevo(Model model, RedirectAttributes redirect) {
		if (negociosActivos().isEmpty()) {
			redirect.addFlashAttribute("message", "fase5.evento.sinnegocio");
			return "redirect:/empresario/negocios";
		}
		return formulario(model, new EventoForm(eventoService.create()));
	}

	@PostMapping
	public String crear(@ModelAttribute("form") EventoForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		form.setId(0);
		return guardar(form, binding, model, redirect);
	}

	@GetMapping("/{id}/editar")
	public String editar(@PathVariable int id, Model model) {
		Evento evento = eventoService.findOneToEdit(id);

		Assert.isTrue(evento.getNegocio().getNegocioActivo());
		return formulario(model, new EventoForm(evento));
	}

	@PutMapping("/{id}")
	public String actualizar(@PathVariable int id, @ModelAttribute("form") EventoForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		eventoService.findOneToEdit(id);
		form.setId(id);
		return guardar(form, binding, model, redirect);
	}

	@GetMapping("/{id}/participantes")
	public String participantes(@PathVariable int id, Model model) {
		// findParticipantsByEvento comprueba que el evento es del empresario;
		// a diferencia de editar, vale también para eventos ya celebrados.
		model.addAttribute("participantes", eventoService.findParticipantsByEvento(id));
		model.addAttribute("evento", eventoService.findOneToDisplay(id));

		return "eventos/participantes";
	}

	@GetMapping("/{id}/imagen")
	public String imagen(@PathVariable int id, Model model) {
		return vistaImagen(model, eventoService.findOneToEdit(id));
	}

	@PostMapping("/{id}/imagen")
	public String subirImagen(@PathVariable int id, @RequestParam("foto") MultipartFile foto, Model model, RedirectAttributes redirect) throws IOException {
		Evento evento = eventoService.findOneToEdit(id);

		try {
			eventoService.addImageToEvento(id, foto.getBytes());
		} catch (IllegalArgumentException formatoNoAdmitido) {
			model.addAttribute("message", "fase5.imagen.formato");
			return vistaImagen(model, evento);
		}
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/empresario/eventos";
	}

	private String guardar(EventoForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		validator.validate(form, binding);
		if (binding.hasErrors())
			return formulario(model, form);

		Evento evento = eventoService.reconstruct(form);
		// El negocio elegido tiene que ser uno activo del propio empresario
		Assert.isTrue(negociosActivos().contains(evento.getNegocio()));
		try {
			eventoService.save(evento);
		} catch (IllegalArgumentException fechaPasada) {
			model.addAttribute("message", "evento.dates.error");
			return formulario(model, form);
		}
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/empresario/eventos";
	}

	private String formulario(Model model, EventoForm form) {
		model.addAttribute("form", form);
		model.addAttribute("negocios", negociosActivos());

		return "eventos/formulario";
	}

	private Collection<Negocio> negociosActivos() {
		return negocioService.findNegociosActivos(empresarioService.findByPrincipal().getId());
	}

	private static String vistaImagen(Model model, Evento evento) {
		model.addAttribute("imagen", new SubidaImagen(evento.getNombre(), "/empresario/eventos/" + evento.getId() + "/imagen", "eventos", evento.getId(), evento.getImagen() != null, "/empresario/eventos"));

		return SubidaImagen.VISTA;
	}

}
