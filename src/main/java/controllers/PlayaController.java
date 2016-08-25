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
	
	
    @RequestMapping(value="/nearToMe", method = RequestMethod.GET)
	public ModelAndView nearToMe(@RequestParam(required=false) Double lat,
			@RequestParam(required=false) Double lon){
		
		ModelAndView result;
		Boolean mostrarMapa;
		mostrarMapa=true;
		Collection<Playa> playas;
		
		if(lat==null || lon==null){
			mostrarMapa=false;
			result = new ModelAndView("playa/nearToMe");
			result.addObject("mostrarMapa", mostrarMapa);
			
		}else{
			
			playas = playaService.nearToMe(lat,lon);
			result = new ModelAndView("playa/nearToMe");
			result.addObject("playas", playas);
			result.addObject("lat1",lat);
			result.addObject("lon1",lon);
			result.addObject("requestURI", "playa/nearToMe.do");
			result.addObject("tabla",true);
		}
		
		return result;	
	}
    
    @RequestMapping(value="/markMap", method = RequestMethod.GET)
	public ModelAndView markMap(@RequestParam int playaId){
		
		ModelAndView result;
		Boolean mostrarMapa;
		mostrarMapa=true;
		Playa playa = playaService.findOne(playaId);
		
		playaService.checkPrincipal(playa);
		Assert.isTrue(playa.getNegocios().isEmpty());
		
		result = new ModelAndView("playa/markMap");
		result.addObject("mostrarMapa", mostrarMapa);
		result.addObject("playaId", playaId);
		result.addObject("requestURI", "playa/markMap.do");
			
		return result;	
	}
    
    @RequestMapping(value="/coordenates/save", method = RequestMethod.GET)
	public ModelAndView save(@RequestParam Double lat, @RequestParam Double lon, @RequestParam int playaId){
			
		ModelAndView result;
		Boolean mostrarMapa;
		mostrarMapa=true;
		
		Playa playa = playaService.findOne(playaId);
		playaService.checkPrincipal(playa);
		Assert.isTrue(playa.getNegocios().isEmpty());
		
		if(lat==null || lon==null){
			mostrarMapa=false;
			result = new ModelAndView("playa/markMap");
			result.addObject("mostrarMapa", mostrarMapa);
					
		}else{
			try{
				playa.getLocalizacion().setLatitud(lat);
				playa.getLocalizacion().setLongitud(lon);
				
				playaService.save(playa);
				result = new ModelAndView("redirect:../list.do");
		
			}catch(Throwable oops){
				result = new ModelAndView("playa/markMap");
				result.addObject("mostrarMapa", mostrarMapa);
			}
		}
			
		return result;
	}
  
	
	protected ModelAndView createModelAndView(Playa playa) {
		ModelAndView result = createModelAndView(playa, null);

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
