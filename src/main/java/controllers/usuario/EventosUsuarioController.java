/* EventosUsuarioController.java
 *
 * Inscripción en eventos. Rutas antiguas:
 *   /evento/usuario/signup.do?eventoId=      -> POST /eventos/{id}/inscripcion
 *   /evento/usuario/unregister.do?eventoId=  -> DELETE /eventos/{id}/inscripcion
 * Nuevo: GET /mis/eventos con los eventos en los que está inscrito.
 */

package controllers.usuario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Evento;
import services.EventoService;
import services.UsuarioService;

@Controller
public class EventosUsuarioController {

	@Autowired
	private EventoService	eventoService;

	@Autowired
	private UsuarioService	usuarioService;


	@GetMapping("/mis/eventos")
	public String misEventos(Model model) {
		List<Evento> eventos = new ArrayList<>(eventoService.findByUsuario());
		Set<Integer> ids = eventos.stream().map(Evento::getId).collect(Collectors.toSet());

		eventos.sort(Comparator.comparing(Evento::getFechaCelebracion).reversed());
		model.addAttribute("eventos", new PageImpl<>(eventos));
		model.addAttribute("inscritos", ids);
		model.addAttribute("titulo", "fase5.mis.eventos");
		model.addAttribute("sinFiltro", true);

		return "eventos/lista";
	}

	@PostMapping("/eventos/{id}/inscripcion")
	public String inscribirse(@PathVariable int id, @RequestHeader(value = "Referer", required = false) String origen, RedirectAttributes redirect) {
		eventoService.participate(eventoService.findOneToDisplay(id), usuarioService.findByPrincipal());
		redirect.addFlashAttribute("aviso", "fase5.inscrito");

		return volver(origen, id);
	}

	@DeleteMapping("/eventos/{id}/inscripcion")
	public String borrarse(@PathVariable int id, @RequestHeader(value = "Referer", required = false) String origen, RedirectAttributes redirect) {
		eventoService.unregister(eventoService.findOneToDisplay(id), usuarioService.findByPrincipal());
		redirect.addFlashAttribute("aviso", "fase5.desinscrito");

		return volver(origen, id);
	}

	// Vuelve a la lista o a la ficha desde la que se pulsó (solo rutas propias)
	private static String volver(String origen, int id) {
		if (origen != null) {
			String ruta = org.springframework.web.util.UriComponentsBuilder.fromUriString(origen).build().getPath();
			if ("/eventos".equals(ruta) || "/mis/eventos".equals(ruta))
				return "redirect:" + ruta;
		}
		return "redirect:/eventos/" + id;
	}

}
