package controllers;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.util.Assert;

import services.PlayaService;

import domain.Playa;

@Controller
@RequestMapping("/playa")
public class PlayaController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	private PlayaService playaService;
	
	

	// Constructors -----------------------------------------------------------

	public PlayaController() {
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
		result.addObject("requestURI", "playa/list.do");
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
		result.addObject("requestURI", "playa/edit.do?playaId="+p.getId());
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
