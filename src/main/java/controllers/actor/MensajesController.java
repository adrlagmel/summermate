/* MensajesController.java
 *
 * Mensajería entre actores. Rutas antiguas:
 *   /carpeta/actor/lista.do              -> GET /carpetas
 *   /mensaje/actor/lista.do?carpetaId=   -> GET /carpetas/{id}
 *   /mensaje/actor/mostrar.do?mensajeId= -> GET /mensajes/{id}
 *   /mensaje/actor/enviar.do             -> GET /mensajes/nuevo y POST /mensajes
 *   /mensaje/actor/responder.do          -> GET /mensajes/{id}/respuesta
 *   /mensaje/actor/borrar.do             -> DELETE /mensajes/{id}
 *
 * Los servicios ya comprueban que la carpeta o el mensaje son del actor
 * conectado (remitente o destinatario).
 */

package controllers.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Actor;
import domain.Mensaje;
import forms.MensajeForm;
import services.ActorService;
import services.CarpetaService;
import services.MensajeService;

@Controller
public class MensajesController {

	@Autowired
	private MensajeService	mensajeService;

	@Autowired
	private CarpetaService	carpetaService;

	@Autowired
	private ActorService	actorService;


	@GetMapping("/carpetas")
	public String carpetas(Model model) {
		model.addAttribute("carpetas", carpetaService.findCarpetasByActor(actorService.findByPrincipal()));

		return "mensajes/carpetas";
	}

	@GetMapping("/carpetas/{id}")
	public String carpeta(@PathVariable int id, Model model) {
		model.addAttribute("carpeta", carpetaService.findOne(id));

		return "mensajes/carpeta";
	}

	@GetMapping("/mensajes/{id}")
	public String mensaje(@PathVariable int id, Model model) {
		model.addAttribute("mensaje", mensajeService.findOne(id));

		return "mensajes/mensaje";
	}

	// ?para= preselecciona el destinatario (desde la búsqueda de usuarios)
	@GetMapping("/mensajes/nuevo")
	public String nuevo(@RequestParam(required = false) Actor para, Model model) {
		MensajeForm form = new MensajeForm();

		form.setBeneficiario(para);
		return formulario(model, form);
	}

	@GetMapping("/mensajes/{id}/respuesta")
	public String responder(@PathVariable int id, Model model) {
		Mensaje original = mensajeService.findOne(id);
		MensajeForm form = new MensajeForm();

		form.setBeneficiario(original.getRemitente());
		form.setAsunto("Re: " + original.getAsunto());
		form.setCuerpo("\n\n-----------------\n" + original.getRemitente().getNombre() + ", " + original.getFechaEnvio() + ":\n" + original.getCuerpo());

		return formulario(model, form);
	}

	@PostMapping("/mensajes")
	public String enviar(@ModelAttribute("form") @Validated MensajeForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		if (binding.hasErrors())
			return formulario(model, form);

		mensajeService.save(mensajeService.reconstruct(form));
		redirect.addFlashAttribute("aviso", "fase5.mensaje.enviado");
		return "redirect:/carpetas";
	}

	@DeleteMapping("/mensajes/{id}")
	public String borrar(@PathVariable int id, RedirectAttributes redirect) {
		mensajeService.delete(mensajeService.findOne(id));
		redirect.addFlashAttribute("aviso", "fase5.borrado");

		return "redirect:/carpetas";
	}

	private String formulario(Model model, MensajeForm form) {
		model.addAttribute("form", form);
		model.addAttribute("actores", actorService.findAllMessages());

		return "mensajes/nuevo";
	}

}
