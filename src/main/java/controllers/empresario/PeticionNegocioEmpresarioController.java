package controllers.empresario;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EmpresarioService;
import services.PeticionNegocioService;

import controllers.AbstractController;
import domain.Empresario;
import domain.PeticionNegocio;
import forms.PeticionNegocioForm;

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
			// create() comprueba que el empresario puede enviar una petición
			peticionNegocioService.create();

			return createEditModelAndView(new PeticionNegocioForm());
		}

		@RequestMapping(value="/enviar", method = RequestMethod.POST)
		public ModelAndView send(@ModelAttribute("peticionNegocio") @Valid PeticionNegocioForm form, BindingResult binding){
			ModelAndView result;

			if(binding.hasErrors()){
				result = createEditModelAndView(form);
			}else{
				try{
					peticionNegocioService.save(peticionNegocioService.reconstruct(form));
					result = new ModelAndView("redirect:list.do");
				}catch(Throwable oops){
					result = createEditModelAndView(form, "peticionNegocio.commit.error");
				}
			}

			return result;
		}

		protected ModelAndView createEditModelAndView(PeticionNegocioForm form){
			return createEditModelAndView(form, null);
		}

		protected ModelAndView createEditModelAndView(PeticionNegocioForm form, String message){
			ModelAndView result;

			result = new ModelAndView("peticionNegocio/empresario/enviar");
			result.addObject("peticionNegocio", form);
			result.addObject("message", message);

			return result;
		}
}
