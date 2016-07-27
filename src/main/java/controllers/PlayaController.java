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
import services.ValoracionPlayaService;

import domain.Playa;
import domain.ValoracionPlaya;

@Controller
@RequestMapping("/playa")
public class PlayaController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	private PlayaService playaService;
	@Autowired
	private ValoracionPlayaService valoracionPlayaService;

	// Constructors -----------------------------------------------------------

	public PlayaController() {
		super();
	}

	// Index ------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Playa> playas = playaService.findAllBeaches();

		result = new ModelAndView("playa/list");
		result.addObject("playas", playas);
		result.addObject("requestURI", "playa/list.do");
		return result;
	}
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int playaId){
							
			ModelAndView result;
			
			Playa playa 	 = playaService.findOne(playaId);
			boolean hasimage = true;
			
			if(playa.getImagen() == null){
				hasimage = false;
			}
			
			result = createEditModelAndView(playa, "display");
			result.addObject("playa", playa);
			result.addObject("hasimage", hasimage);
			
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
	
	protected ModelAndView createEditModelAndView(Playa playa, String selectView){
		
		ModelAndView result;
			
		result = createEditModelAndView(playa, selectView, null);
			
		return result;
	}
		
	protected ModelAndView createEditModelAndView(Playa playa, String selectView, String message){

		ModelAndView result;
		Collection<ValoracionPlaya> valoracionesPlayas;
		valoracionesPlayas = valoracionPlayaService.findValoracionPlayaByPlaya(playa.getId());
		result = new ModelAndView("playa/"+selectView);
		
		result.addObject("vPlayas", valoracionesPlayas);
		result.addObject("playa", playa);
		result.addObject("message", null);

		return result;
	}	
	

}
