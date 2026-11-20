package controllers;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;

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

	// Radios (km) que se ofrecen en "Playas cerca de mí"
	private static final List<Integer>	RADIOS_KM	= Arrays.asList(5, 10, 25, 50, 100, 200);


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
	public ModelAndView list(Paginacion paginacion){

		ModelAndView result;
		PaginaDisplaytag<Playa> playas = new PaginaDisplaytag<>(playaService.findAll(paginacion.playas()));

		result = new ModelAndView("playa/list");
		result.addObject("playas", playas);
		result.addObject("puntosMapa", playaService.findPuntosMapa());
		result.addObject("requestURI", "playa/list.do");
		return result;
	}
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int playaId){
							
		ModelAndView result;
		Playa playa 	 = playaService.findOne(playaId);
		Collection<Playa> playas = playaService.findAllBeaches();
		
		playas.removeAll(playas);
		playas.add(playa);
		
		boolean hasimage = true;
		
		if(playa.getImagen() == null){
			hasimage = false;
		}
		
		result = createEditModelAndView(playa, "display");
		result.addObject("playa", playa);
		result.addObject("playas", playas);
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
			@RequestParam(required=false) Double lon,
			@RequestParam(required=false) Integer radio){
		
		ModelAndView result;
		Boolean mostrarMapa;
		mostrarMapa=true;
		Collection<Playa> playas;
		
		if(lat==null || lon==null){
			mostrarMapa=false;
			result = new ModelAndView("playa/nearToMe");
			result.addObject("mostrarMapa", mostrarMapa);
			
		}else{
			
			// Solo se admiten los radios del desplegable
			if(radio == null || !RADIOS_KM.contains(radio))
				radio = (int) PlayaService.RADIO_POR_DEFECTO_KM;

			playas = playaService.nearToMe(lat, lon, radio);
			result = new ModelAndView("playa/nearToMe");
			result.addObject("playas", playas);
			result.addObject("lat1",lat);
			result.addObject("lon1",lon);
			result.addObject("requestURI", "playa/nearToMe.do");
			result.addObject("tabla",true);
			result.addObject("radio", radio);
			result.addObject("radios", RADIOS_KM);
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
