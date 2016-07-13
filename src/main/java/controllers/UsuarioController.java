package controllers;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Usuario;
import forms.UsuarioRegistroForm;

import services.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
			@Autowired
			private UsuarioService usuarioService;
			
						
			// Constructors ---------------------------------------------------------------
			
			public UsuarioController(){
				super();
			}
			
			
			// Listing methods -----------------------------------------------------------
			
			
			
			// Create methods --------------------------------------------------------------
			
			
			@RequestMapping(value="/register", method = RequestMethod.GET)
			public ModelAndView create(){
				
				ModelAndView result;
				UsuarioRegistroForm form = new UsuarioRegistroForm();
				result = createEditModelAndView(form);
				
				return result;
				
			}
			
			@RequestMapping(value="/register", method=RequestMethod.POST, params="register")
			public ModelAndView save(@ModelAttribute("form") @Valid UsuarioRegistroForm form, BindingResult binding){
				
				ModelAndView result;
				Usuario usuario;
				
				if(binding.hasErrors()){
					
						result = createEditModelAndView(form);
				}else{
					try{
						usuario = usuarioService.reconstruct(form);
						usuarioService.save(usuario);
						result = new ModelAndView("redirect:/summermate");
						
					}catch(DataIntegrityViolationException exc){
						result = createEditModelAndView(form, "register.duplicated.user");
					}catch(Throwable oops){
						result = createEditModelAndView(form, "register.commit.error");
					}
				}
				
				return result;
			}
			
			
			
			// Ancillary methods ---------------------------------------------------------
			
				protected ModelAndView createEditModelAndView(UsuarioRegistroForm registroForm){
					
					ModelAndView result;
					
					result = createEditModelAndView(registroForm, null);
					
					return result;
				}
				
				protected ModelAndView createEditModelAndView(UsuarioRegistroForm registroForm, String message){
					
					ModelAndView result;
				
					result = new ModelAndView("usuario/register");
					result.addObject("form", registroForm);
					result.addObject("message", message);
					result.addObject("isUsuario", true);
					result.addObject("isAdministrador", false);
					result.addObject("actionURI", "usuario/register.do");

					return result;
				}

	}
