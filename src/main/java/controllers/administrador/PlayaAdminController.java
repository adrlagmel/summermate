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
		Collection<Playa> playas = playaService.findPlayaForNoAuthenticate();

		result = new ModelAndView("playa/list");
		result.addObject("playas", playas);
		result.addObject("requestURI", "playa/admin/list.do");
		return result;
	}
	
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Playa p;
		p = playaService.create();
		result = createModelAndView(p);
		result.addObject("playa", p);
		result.addObject("createanddelete",true);
		result.addObject("requestURI", "playa/admin/edit.do");
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView create(@ModelAttribute("p") @Valid Playa p, BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = createModelAndView(p);
		} else {
			try {				
				playaService.save(p);
				result = new ModelAndView("redirect:../../playa/admin/list.do");
			} catch (Throwable oops) {

				result= createModelAndView(p,"message.commit.error");
				
			}
		}
		return result;
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView edit(@ModelAttribute("p") @Valid Playa p, BindingResult binding){
		
		ModelAndView result;
		
		
		playaService.delete(p);
		result = new ModelAndView("redirect:../../playa/admin/list.do");
			
		return result;
		
	}
	
		//Consultar detalles
//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
//	public ModelAndView edit2(@RequestParam int playaId){
//		ModelAndView result;
//		Playa p = playaService.findOneToEdit(playaId);
//		
//		Assert.notNull(p);
//		result = createModelAndView(p);
//		result.addObject("createanddelete",false);
//		result.addObject("requestURI", "playa/admin/edit.do?playaId="+p.getId());
//		return result;
//	}
	
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
