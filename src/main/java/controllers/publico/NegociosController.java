/* NegociosController.java
 *
 * Catálogo público de negocios. Rutas antiguas:
 *   /negocio/list.do y /negocio/search.do?s=   -> GET /negocios?ciudad=
 *   /negocio/empresario/display.do             -> GET /negocios/{id}
 *   /valoracionNegocio/listValoracionesNot.do  -> GET /negocios/{id}/valoraciones
 *
 * Antes /negocio/list.do exigía sesión pero la búsqueda no; ahora el listado
 * es público como el resto del catálogo.
 */

package controllers.publico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import controllers.Paginacion;
import domain.Negocio;
import services.NegocioService;
import services.ValoracionNegocioService;

@Controller
@RequestMapping("/negocios")
public class NegociosController {

	@Autowired
	private NegocioService				negocioService;

	@Autowired
	private ValoracionNegocioService	valoracionNegocioService;


	@GetMapping
	public String lista(@RequestParam(required = false) String ciudad, Paginacion paginacion, Model model) {
		if (StringUtils.hasText(ciudad))
			model.addAttribute("negocios", negocioService.searchByTown(ciudad.trim(), paginacion.negocios()));
		else
			model.addAttribute("negocios", negocioService.findAll(paginacion.negocios()));
		model.addAttribute("ciudad", ciudad);

		return "negocios/lista";
	}

	@Autowired
	private services.VotoUtilService	votoUtilService;

	@GetMapping("/{id}")
	public String ficha(@PathVariable int id, Model model) {
		Negocio negocio = negocioService.findOneToDisplay(id);

		model.addAttribute("negocio", negocio);
		java.util.Collection<domain.ValoracionNegocio> valoraciones = valoracionNegocioService.valoracionesNegocioDeUnNegocio(id);
		model.addAttribute("valoraciones", valoraciones);
		model.addAttribute("votadas", votoUtilService.votadas(valoraciones.stream().map(v -> v.getId()).toList()));

		return "negocios/ficha";
	}

	@GetMapping("/{id}/valoraciones")
	public String valoraciones(@PathVariable int id, Model model) {
		return ficha(id, model);
	}

}
