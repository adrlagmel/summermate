package controllers.administrador;

import java.util.Collection;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PeticionNegocioService;
import controllers.AbstractController;
import domain.PeticionNegocio;

@Controller
@RequestMapping("/peticionNegocio/administrador")
public class PeticionNegocioAdministradorController extends AbstractController{
	// Services ---------------------------------------------------------------
	@Autowired
	private PeticionNegocioService peticionNegocioService;
	
	// Constructors -----------------------------------------------------------
	public PeticionNegocioAdministradorController(){
		super();
	}

	// Listing-----------------------------------------------------------------
	@RequestMapping(value = "/listapendiente", method = RequestMethod.GET)
	public ModelAndView list(){
		ModelAndView result;
		Collection<PeticionNegocio> peticionNegocios;
		
		peticionNegocios = peticionNegocioService.findPeticionNegocioPendiente();
		
		result = new ModelAndView("peticionNegocio/administrador/listapendiente");
		
		result.addObject("peticionNegocios", peticionNegocios);
		result.addObject("requestURI", "peticionNegocio/administrador");		
		
		return result;
	}
	
	// Display------------------------------------------------------------------
	@RequestMapping(value="/aceptar", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam int peticionNegocioId){
		
		ModelAndView result;
		PeticionNegocio peticionNegocio;
		
		peticionNegocio = peticionNegocioService.findOneToEdit(peticionNegocioId);
		
		result = new ModelAndView("peticionNegocio/administrador/aceptar");
		result.addObject("peticionNegocio", peticionNegocio);
		
		return result;
		
	}
	
	@RequestMapping(value="/aceptar", method=RequestMethod.POST, params="aceptar")
	public ModelAndView accept(@Valid PeticionNegocio peticionNegocio, BindingResult binding){
		
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = new ModelAndView("peticionNegocio/administrador/aceptar");
			result.addObject("peticionNegocio", peticionNegocio);
		}else{
			try{
				peticionNegocioService.accept(peticionNegocio);
								
				result = new ModelAndView("redirect:listapendiente.do");
			}catch(Throwable oops){
				result = new ModelAndView("peticionNegocio/administrador/aceptar");
				result.addObject("peticionNegocio", peticionNegocio);
				result.addObject("message", "peticionNegocio.commit.error");
			}
		}
		
		return result;
	}
	
	@RequestMapping(value="/rechazar", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam int peticionNegocioId){
		
		ModelAndView result;
		PeticionNegocio peticionNegocio;
		
		peticionNegocio = peticionNegocioService.findOneToEdit(peticionNegocioId);
		
		result = new ModelAndView("peticionNegocio/administrador/rechazar");
		result.addObject("peticionNegocio", peticionNegocio);
		
		return result;
		
	}
	
	@RequestMapping(value="/rechazar", method=RequestMethod.POST, params="rechazar")
	public ModelAndView reject(@Valid PeticionNegocio peticionNegocio, BindingResult binding){
		
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = new ModelAndView("peticionNegocio/administrador/rechazar");
			result.addObject("peticionNegocio", peticionNegocio);
		}else{
			try{
				peticionNegocioService.reject(peticionNegocio);
								
				result = new ModelAndView("redirect:listapendiente.do");
			}catch(Throwable oops){
				result = new ModelAndView("peticionNegocio/administrador/rechazar");
				result.addObject("peticionNegocio", peticionNegocio);
				result.addObject("message", "contract.commit.error");
			}
		}
		
		return result;
	}
	
}