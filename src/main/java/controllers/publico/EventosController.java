/* EventosController.java
 *
 * Eventos públicos. Rutas antiguas:
 *   /evento/list.do, /evento/usuario/list.do            -> GET /eventos
 *   /evento/usuario/listActivos.do e listInactivos.do   -> GET /eventos?cuando=proximos|pasados
 *   /evento/display.do                                  -> GET /eventos/{id}
 *
 * Si quien mira es un usuario, la lista marca en qué eventos está inscrito
 * (con una sola consulta, en vez de cargar los participantes de cada fila).
 */

package controllers.publico;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import controllers.Paginacion;
import domain.Evento;
import security.Authority;
import services.EventoService;

@Controller
@RequestMapping("/eventos")
public class EventosController {

	@Autowired
	private EventoService	eventoService;


	@GetMapping
	public String lista(@RequestParam(required = false) String cuando, Paginacion paginacion, Authentication auth, Model model) {
		Page<Evento> eventos;

		if ("proximos".equals(cuando))
			eventos = eventoService.EventosActivos(paginacion.eventos());
		else if ("pasados".equals(cuando))
			eventos = eventoService.EventosInactivos(paginacion.eventos());
		else
			eventos = eventoService.findAll(paginacion.eventos());

		model.addAttribute("eventos", eventos);
		model.addAttribute("cuando", cuando);
		model.addAttribute("inscritos", inscritos(auth));

		return "eventos/lista";
	}

	@GetMapping("/{id}")
	public String ficha(@PathVariable int id, Authentication auth, Model model) {
		model.addAttribute("evento", eventoService.findOneToDisplay(id));
		model.addAttribute("inscritos", inscritos(auth));

		return "eventos/ficha";
	}

	// Ids de los eventos en los que está inscrito el usuario conectado
	Set<Integer> inscritos(Authentication auth) {
		if (auth == null || auth.getAuthorities().stream().noneMatch(a -> Authority.USUARIO.equals(a.getAuthority())))
			return Collections.emptySet();

		return eventoService.findByUsuario().stream().map(Evento::getId).collect(Collectors.toSet());
	}

}
