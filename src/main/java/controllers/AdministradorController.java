/* AdministradorController.java
 *
 * Copyright (C) 2013 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

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

import services.AdministradorService;

import domain.Administrador;
import forms.AdministradorRegistroForm;

@Controller
@RequestMapping("/administrador")
public class AdministradorController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
			@Autowired
			private AdministradorService administradorService;
			
						
			// Constructors ---------------------------------------------------------------
			
			public AdministradorController(){
				super();
			}
			
			
			// Listing methods -----------------------------------------------------------
			
			
			
			// Create methods --------------------------------------------------------------
			
			
			@RequestMapping(value="/register", method = RequestMethod.GET)
			public ModelAndView create(){
				
				ModelAndView result;
				AdministradorRegistroForm form = new AdministradorRegistroForm();
				result = createEditModelAndView(form);
				
				return result;
				
			}
			
			@RequestMapping(value="/register", method=RequestMethod.POST, params="register")
			public ModelAndView save(@ModelAttribute("form") @Valid AdministradorRegistroForm form, BindingResult binding){
				
				ModelAndView result;
				Administrador admin;
				
				if(binding.hasErrors()){
					
						result = createEditModelAndView(form);
				}else{
					try{
						admin = administradorService.reconstruct(form);
						administradorService.save(admin);
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
			
				protected ModelAndView createEditModelAndView(AdministradorRegistroForm registroForm){
					
					ModelAndView result;
					
					result = createEditModelAndView(registroForm, null);
					
					return result;
				}
				
				protected ModelAndView createEditModelAndView(AdministradorRegistroForm registroForm, String message){
					
					ModelAndView result;
				
					result = new ModelAndView("administrador/register");
					result.addObject("form", registroForm);
					result.addObject("message", message);
					result.addObject("isUsuario", false);
					result.addObject("isEmpresario", false);
					result.addObject("isAdministrador", true);
					result.addObject("actionURI", "administrador/register.do");

					return result;
				}

	}