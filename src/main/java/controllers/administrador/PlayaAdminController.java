package controllers.administrador;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PlayaService;
import controllers.AbstractController;
import domain.Playa;

@Controller
@RequestMapping("/playa/admin")
public class PlayaAdminController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	private PlayaService playaService;
	
	// Constructors -----------------------------------------------------------

	public PlayaAdminController() {
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
	
	
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(){
							
		ModelAndView result;
					
			Playa playa = playaService.create();
			
			result =  createEditModelAndView(playa, "create");
			
			return result;
	}
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int playaId){
							
			ModelAndView result;
			
			Playa playa = playaService.findOne(playaId);
			
			result = createEditModelAndView(playa, "display");
			
			return result;
					
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int playaId){
							
			ModelAndView result;
			
			Playa playa = playaService.findOneToEdit(playaId);
			
			result = createEditModelAndView(playa, "edit");
			
			return result;
					
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("playa") @Valid Playa playa, BindingResult binding){
		
	ModelAndView result;
		if(binding.hasErrors()){
			result = createEditModelAndView(playa, "edit");
			
		}else{
			try{
				playaService.save(playa);
				result = new ModelAndView("redirect:../../playa/list.do");
				
			}catch(Throwable oops){
				result = createEditModelAndView(playa, "edit", "playa.commit.error");
			}		
		}
			
	return result;
	}
	

	@RequestMapping(value="/borrar", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int playaId){
		
		ModelAndView result;
		
		Playa p = playaService.findOne(playaId);
		Assert.notNull(p);
	
		playaService.delete(p);
		result = new ModelAndView("redirect:../../playa/list.do");

		return result;
		
	}
	

	
	
	protected ModelAndView createEditModelAndView(Playa playa, String selectView){
		
		ModelAndView result;
			
		result = createEditModelAndView(playa, selectView, null);
			
		return result;
	}
		
	protected ModelAndView createEditModelAndView(Playa playa, String selectView, String message){

		ModelAndView result;
			
		result = new ModelAndView("playa/"+selectView);

		result.addObject("playa", playa);
		result.addObject("message", null);

		return result;
	}	
	
	

}
