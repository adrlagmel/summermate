/* PlayasController.java
 *
 * Catálogo público de playas. Rutas antiguas:
 *   /playa/list.do            -> GET /playas
 *   /playa/display.do         -> GET /playas/{id}
 *   /playa/nearToMe.do        -> GET /playas/cercanas
 *   /negocio/listNegocios.do  -> GET /playas/{id}/negocios
 */

package controllers.publico;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import controllers.Paginacion;
import domain.Playa;
import services.NegocioService;
import services.PlayaService;
import services.PuntoMapa;
import services.ValoracionPlayaService;
import utilities.Assert;

@Controller
@RequestMapping("/playas")
public class PlayasController {

	// Radios (km) que se ofrecen en "Playas cerca de mí" (Fase 3)
	static final List<Integer>		RADIOS_KM	= Arrays.asList(5, 10, 25, 50, 100, 200);

	@Autowired
	private PlayaService			playaService;

	@Autowired
	private NegocioService			negocioService;

	@Autowired
	private ValoracionPlayaService	valoracionPlayaService;


	@GetMapping
	public String lista(Paginacion paginacion, Model model) {
		org.springframework.data.domain.Page<Playa> playas = playaService.findAll(paginacion.playas());

		model.addAttribute("playas", playas);
		// Una consulta para saber qué playas tienen negocios (no se pueden mover ni borrar)
		model.addAttribute("conNegocios", negocioService.playasConNegocios(playas.map(Playa::getId).getContent()));
		model.addAttribute("puntos", playaService.findPuntosMapa());

		return "playas/lista";
	}

	@GetMapping("/{id}")
	public String ficha(@PathVariable int id, Model model) {
		Playa playa = playaService.findOne(id);
		Assert.notNull(playa);

		model.addAttribute("playa", playa);
		model.addAttribute("valoraciones", valoracionPlayaService.findValoracionPlayaByPlaya(id));
		model.addAttribute("puntos", List.of(punto(playa)));

		return "playas/ficha";
	}

	@GetMapping("/{id}/negocios")
	public String negocios(@PathVariable int id, Paginacion paginacion, Model model) {
		Playa playa = playaService.findOne(id);
		Assert.notNull(playa);

		model.addAttribute("playa", playa);
		model.addAttribute("negocios", negocioService.findNegociosDePlaya(id, paginacion.negocios()));

		return "negocios/lista";
	}

	// Sin coordenadas, la página pide la ubicación al navegador y se recarga
	// con ?lat=..&lon=..
	@GetMapping("/cercanas")
	public String cercanas(@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon, @RequestParam(required = false) Integer radio, Model model) {
		if (radio == null || !RADIOS_KM.contains(radio))
			radio = (int) PlayaService.RADIO_POR_DEFECTO_KM;

		model.addAttribute("radio", radio);
		model.addAttribute("radios", RADIOS_KM);
		if (lat != null && lon != null) {
			model.addAttribute("lat", lat);
			model.addAttribute("lon", lon);
			List<Playa> playas = playaService.nearToMe(lat, lon, radio);
			model.addAttribute("playas", playas);
			model.addAttribute("puntos", playas.stream().map(PlayasController::punto).toList());
		}

		return "playas/cercanas";
	}

	static PuntoMapa punto(Playa playa) {
		return new PuntoMapa(playa.getId(), playa.getNombre(), playa.getLocalizacion().getLatitud(), playa.getLocalizacion().getLongitud());
	}

}
