package controllers.empresario;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EmpresarioService;
import services.PeticionNegocioService;

import controllers.AbstractController;
import domain.Empresario;
import domain.PeticionNegocio;

@Controller
@RequestMapping("/peticionNegocio/empresario")
public class PeticionNegocioEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	

		@Autowired
		private PeticionNegocioService peticionNegocioService;
		
		@Autowired
		private EmpresarioService empresarioService;
		
		// Constructors ---------------------------------------------------------------
		
		public PeticionNegocioEmpresarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			
			Empresario empresario = empresarioService.findByPrincipal();
			
			PeticionNegocio peticionNegocio = peticionNegocioService.findByEmpresarioId(empresario.getId());
			
			result = new ModelAndView("peticionNegocio/empresario/list");

			result.addObject("peticionNegocio", peticionNegocio);
			
			return result;
			
		}
		
		
		
	// Create and edition methods -------------------------------------------------
			
		@RequestMapping(value="/enviar", method = RequestMethod.GET)
		public ModelAndView send(){
								
			ModelAndView result;
						
				PeticionNegocio peticionNegocio = peticionNegocioService.create();
				
				result =  createEditModelAndView(peticionNegocio);
				
				return result;
						
		}
		
		@RequestMapping(value="/enviar", method = RequestMethod.POST)
		public ModelAndView send(@Valid PeticionNegocio peticionNegocio, BindingResult binding){
			
		ModelAndView result;
		if(binding.hasErrors()){
			result = createEditModelAndView(peticionNegocio);
			
		}else{
			try{
				peticionNegocioService.save(peticionNegocio);
				result = new ModelAndView("redirect:list.do");
				
			}catch(Throwable oops){
				result = createEditModelAndView(peticionNegocio, "peticionNegocio.commit.error");
			}		
		}
			
		return result;
	}
		
		// Ancillary methods ---------------------------------------------------------
		
		protected ModelAndView createEditModelAndView(PeticionNegocio peticionNegocio){
				
			ModelAndView result;
				
			result = createEditModelAndView(peticionNegocio, null);
				
			return result;
		}
			
		protected ModelAndView createEditModelAndView(PeticionNegocio peticionNegocio, String message){

			ModelAndView result;
				
			result = new ModelAndView("peticionNegocio/empresario/enviar");

			result.addObject("peticionNegocio", peticionNegocio);
			result.addObject("message", null);

			return result;
		}
			
			
}
