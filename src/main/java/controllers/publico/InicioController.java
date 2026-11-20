/* InicioController.java
 *
 * Portada con el mapa de playas (antes /welcome/index.do), formulario de
 * acceso (antes /security/login.do; el POST lo procesa Spring Security) y
 * aviso legal (antes una ventana modal en cada página).
 */

package controllers.publico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import services.PlayaService;

@Controller
public class InicioController {

	@Autowired
	private PlayaService	playaService;


	@GetMapping("/")
	public String inicio(Model model) {
		// Para el mapa solo hacen falta id, nombre y coordenadas (Fase 3)
		model.addAttribute("puntos", playaService.findPuntosMapa());
		model.addAttribute("fondoPlaya", true);

		return "inicio";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("fondoPlaya", true);

		return "login";
	}

	@GetMapping("/terminos")
	public String terminos() {
		return "terminos";
	}

}
