package controllers.empresario;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NegocioService;
import controllers.AbstractController;
import domain.Negocio;

@Controller
@RequestMapping("/negocio/empresario")
public class NegocioEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private NegocioService negocioService;
		
		// Constructors ---------------------------------------------------------------
		
		public NegocioEmpresarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Negocio> negocios = null;
			
			negocios = negocioService.findByEmpresario();
			
			result = new ModelAndView("negocio/list");

			result.addObject("negocios", negocios);
			result.addObject("actionURI","negocio/empresario/search.do");
			result.addObject("requestURI", "negocio/empresario/list.do");

			return result;
			
		}
		
		
		@RequestMapping(value="/search", method = RequestMethod.GET)
		public ModelAndView search(@RequestParam String s, @RequestParam(required=false) Boolean showError, @RequestParam(required=false) Boolean showSuccess){
			ModelAndView result;
			
			Collection<Negocio> negocios = negocioService.searchByTown(s);
			result = new ModelAndView("negocio/empresario/list");
			result.addObject("negocios", negocios);
			result.addObject("showError", showError);
			result.addObject("showSuccess", showSuccess);
			result.addObject("actionURI","negocio/empresario/search.do");
			result.addObject("requestURI","negocio/empresario/search.do");
			
			return result;
			
		}
		
				
	// Create and edition methods -------------------------------------------------
			
		@RequestMapping(value="/register", method = RequestMethod.GET)
		public ModelAndView create(){
				ModelAndView result;
						
				Negocio negocio = negocioService.create();		
				result 			= createEditModelAndView(negocio, "register");
				
				return result;
						
		}
		
		@RequestMapping(value="/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam int negocioId){
				ModelAndView result;
				
				Negocio negocio = negocioService.findOneToEdit(negocioId);
				result 			= createEditModelAndView(negocio, "edit");
				
				return result;
						
		}
		
		@RequestMapping(value="/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam int negocioId){
				ModelAndView result;
				
				Negocio negocio = negocioService.findOneToDisplay(negocioId);
				result 			= createEditModelAndView(negocio, "display");
				
				return result;
						
		}
		
		@RequestMapping(value="/edit", method = RequestMethod.POST)
		public ModelAndView save(@Valid Negocio negocio, BindingResult binding){
			
		ModelAndView result;
		if(binding.hasErrors()){
			result = createEditModelAndView(negocio, "edit");
			
		}else{
			try{
				negocioService.save(negocio);
				result = new ModelAndView("redirect:list.do");
				
			}catch(Throwable oops){
				result = createEditModelAndView(negocio, "edit", "negocio.commit.error");
			}		
		}
			
		return result;
	}
		
		// Ancillary methods ---------------------------------------------------------
		
		protected ModelAndView createEditModelAndView(Negocio negocio, String selectView){
			ModelAndView result;
				
			result = createEditModelAndView(negocio, selectView, null);
				
			return result;
		}
			
		protected ModelAndView createEditModelAndView(Negocio negocio, String selectView, String message){
			ModelAndView result;
				
			result = new ModelAndView("negocio/"+selectView);

			result.addObject("negocio", negocio);
			result.addObject("message", null);

			return result;
		}
			
}
