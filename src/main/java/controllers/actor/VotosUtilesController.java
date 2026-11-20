/* VotosUtilesController.java
 *
 * Votos de "comentario útil" (Fase 6):
 *   POST   /valoraciones/negocios/{id}/util   marcar como útil
 *   DELETE /valoraciones/negocios/{id}/util   quitar el voto
 */

package controllers.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import services.ValoracionNegocioService;
import services.VotoUtilService;

@Controller
@RequestMapping("/valoraciones/negocios/{id}/util")
public class VotosUtilesController {

	@Autowired
	private VotoUtilService				votoUtilService;

	@Autowired
	private ValoracionNegocioService	valoracionNegocioService;


	@PostMapping
	public String votar(@PathVariable int id) {
		votoUtilService.votar(id);
		return volver(id);
	}

	@DeleteMapping
	public String quitar(@PathVariable int id) {
		votoUtilService.quitar(id);
		return volver(id);
	}

	private String volver(int id) {
		return "redirect:/negocios/" + valoracionNegocioService.findOne(id).getReserva().getNegocio().getId() + "#valoraciones";
	}

}
