package controllers.administrador;

import java.util.Collection;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PeticionNegocioService;
import controllers.AbstractController;
import domain.PeticionNegocio;
import forms.PeticionNegocioResolucionForm;

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
		PeticionNegocio peticionNegocio = peticionNegocioService.findOneToEdit(peticionNegocioId);

		return resolucionModelAndView("aceptar", new PeticionNegocioResolucionForm(peticionNegocio), null);
	}

	@RequestMapping(value="/aceptar", method=RequestMethod.POST, params="aceptar")
	public ModelAndView accept(@ModelAttribute("peticionNegocio") @Valid PeticionNegocioResolucionForm form, BindingResult binding){
		ModelAndView result;

		if(binding.hasErrors()){
			result = resolucionModelAndView("aceptar", form, null);
		}else{
			try{
				peticionNegocioService.accept(peticionNegocioService.reconstruct(form));
				result = new ModelAndView("redirect:listapendiente.do");
			}catch(Throwable oops){
				result = resolucionModelAndView("aceptar", form, "peticionNegocio.commit.error");
			}
		}

		return result;
	}

	@RequestMapping(value="/rechazar", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam int peticionNegocioId){
		PeticionNegocio peticionNegocio = peticionNegocioService.findOneToEdit(peticionNegocioId);

		return resolucionModelAndView("rechazar", new PeticionNegocioResolucionForm(peticionNegocio), null);
	}

	@RequestMapping(value="/rechazar", method=RequestMethod.POST, params="rechazar")
	public ModelAndView reject(@ModelAttribute("peticionNegocio") @Valid PeticionNegocioResolucionForm form, BindingResult binding){
		ModelAndView result;

		if(binding.hasErrors()){
			result = resolucionModelAndView("rechazar", form, null);
		}else{
			try{
				peticionNegocioService.reject(peticionNegocioService.reconstruct(form));
				result = new ModelAndView("redirect:listapendiente.do");
			}catch(Throwable oops){
				result = resolucionModelAndView("rechazar", form, "contract.commit.error");
			}
		}

		return result;
	}

	// El formulario solo lleva los comentarios; la petición se pasa aparte
	// ("peticion") para mostrar sus datos en modo solo lectura.
	protected ModelAndView resolucionModelAndView(String view, PeticionNegocioResolucionForm form, String message){
		ModelAndView result;

		result = new ModelAndView("peticionNegocio/administrador/" + view);
		result.addObject("peticionNegocio", form);
		result.addObject("peticion", peticionNegocioService.findOneToEdit(form.getId()));
		result.addObject("message", message);

		return result;
	}
}