/* UsuariosController.java
 *
 * Búsqueda de usuarios por apellidos (antes /usuario/list.do y
 * /usuario/search.do?s=): GET /usuarios?apellidos=. El email solo se muestra
 * con sesión iniciada (Fase 1).
 */

package controllers.publico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import controllers.Paginacion;
import services.ActorService;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	private ActorService	actorService;


	@GetMapping
	public String lista(@RequestParam(required = false) String apellidos, Paginacion paginacion, Model model) {
		if (StringUtils.hasText(apellidos))
			model.addAttribute("usuarios", actorService.searchUsuarioForApellido(apellidos.trim(), paginacion.actores()));
		else
			model.addAttribute("usuarios", actorService.findAll(paginacion.actores()));
		model.addAttribute("apellidos", apellidos);

		return "usuarios/lista";
	}

}
