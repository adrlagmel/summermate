package controllers.usuario;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PlayaService;
import controllers.AbstractController;
import domain.Playa;

@Controller
@RequestMapping("/playa/usuario")
public class PlayaUsuarioController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	private PlayaService playaService;
	
	

	// Constructors -----------------------------------------------------------

	public PlayaUsuarioController() {
		super();
	}

	// Index ------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Playa> playas = playaService
				.findPlayaForNoAuthenticate();

		result = new ModelAndView("playa/list");
		result.addObject("playas", playas);
		result.addObject("requestURI", "playa/usuario/list.do");
		return result;
	}
	
	
		//Consultar detalles
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit2(@RequestParam int playaId){
		ModelAndView result;
		Playa p = playaService.findOneToEdit(playaId);
		
		Assert.notNull(p);
		result = createModelAndView(p);
		result.addObject("createanddelete",false);
		result.addObject("requestURI", "playa/usuario/edit.do?playaId="+p.getId());
		return result;
	}
	
	protected ModelAndView createModelAndView(Playa playa) {
		ModelAndView result;

		result = createModelAndView(playa, null);

		return result;
	}

	protected ModelAndView createModelAndView(Playa playa, String message) {
		ModelAndView result;
		result = new ModelAndView("playa/edit");
		result.addObject("playa", playa);

		
		result.addObject("message", message);
	
		return result;
		}
	

}
