package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Empresario;
import domain.Playa;

import forms.EmpresarioRegistroForm;

import services.EmpresarioService;

@Controller
@RequestMapping("/empresario")
public class EmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private EmpresarioService empresarioService;
		
		// Constructors ---------------------------------------------------------------
		
		public EmpresarioController(){
			super();
		}
		
		
		
		// Create methods --------------------------------------------------------------
		
		@RequestMapping(value="/register", method = RequestMethod.GET)
		public ModelAndView create(){
			
			ModelAndView result;
			EmpresarioRegistroForm form = new EmpresarioRegistroForm();
			result = createEditModelAndView(form);
			
			return result;
			
		}
		
		@RequestMapping(value="/register", method=RequestMethod.POST, params="register")
		public ModelAndView save(@ModelAttribute("form") @Valid EmpresarioRegistroForm form, BindingResult binding){
			
			ModelAndView result;
			Empresario empresario;
			
			if(binding.hasErrors()){
				
				
					result = createEditModelAndView(form);
			}else{
				try{
					empresario = empresarioService.reconstruct(form);
					empresarioService.save(empresario);
					result = new ModelAndView("redirect:/");
					
				}catch(DataIntegrityViolationException exc){
					result = createEditModelAndView(form, "register.duplicated.user");
				}catch(Throwable oops){
					result = createEditModelAndView(form, "register.commit.error");
				}
			}
			
			return result;
		}
		
		
		
		// Ancillary methods ---------------------------------------------------------
		
			protected ModelAndView createEditModelAndView(EmpresarioRegistroForm registroForm){
				
				ModelAndView result;
				
				result = createEditModelAndView(registroForm, null);
				
				return result;
			}
			
			protected ModelAndView createEditModelAndView(EmpresarioRegistroForm registroForm, String message){
				
				ModelAndView result;
			
				result = new ModelAndView("empresario/register");
				result.addObject("form", registroForm);
				result.addObject("message", message);
				result.addObject("isEmpresario", true);
				result.addObject("isAdministrador", false);
				result.addObject("actionURI", "empresario/register.do");

				return result;
			}

}

