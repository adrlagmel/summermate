/* ProfileController.java
 *
 * Copyright (C) 2013 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers;

import java.util.ArrayList;
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
import domain.Usuario;
import security.Authority;
import services.EmpresarioService;
import services.UsuarioService;

@Controller
@RequestMapping("/perfil")
public class ProfileController extends AbstractController {
	

	// Services -------------------------------------------------------------------
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EmpresarioService empresarioService;
	
	// Profile ---------------------------------------------------------------		
		
	
	@RequestMapping(value="/usuario", method = RequestMethod.GET)
	public ModelAndView profileUsuario(){
		ModelAndView result;
		
		Usuario usuario = usuarioService.findByPrincipal();
	
		result = new ModelAndView("usuario");
		
		result.addObject("actor",usuario);
		result.addObject("isCliente", true);
		result.addObject("isUsuario", true);
		result.addObject("requestURI", "perfil/usuario.do");
			
		return result;
		
	}
	
	@RequestMapping(value="/usuario/edit", method = RequestMethod.GET)
	public ModelAndView profileUsuarioEdit(){
		ModelAndView result;
		
		Usuario usuario = usuarioService.findOneToEdit(usuarioService.findByPrincipal().getId());
		
		result = new ModelAndView("usuario/edit");
		Collection<Authority> authorities = new ArrayList<Authority>();
		authorities = usuario.getUserAccount().getAuthorities();
		
		System.out.println(usuario.getUserAccount().getAuthorities());
		
		result.addObject("actor",usuario);
		result.addObject("authorities", authorities);
		result.addObject("isCliente", true);
		result.addObject("isUsuario", true);
		result.addObject("actionURI", "perfil/usuario/edit.do");
			
		return result;
		
	}
	
	@RequestMapping(value="/usuario/edit", method=RequestMethod.POST, params="save")
	public ModelAndView saveUsuario(@ModelAttribute("actor") @Valid Usuario usuario, BindingResult binding){
		
		ModelAndView result;
		
		System.out.println(usuario.getUserAccount().getAuthorities());
		
		if(binding.hasErrors()){
				result = createEditModelAndViewUsuario(usuario);
		}else{
			try{
				usuarioService.save(usuario);
				result = new ModelAndView("redirect:/#");
				
			}catch(DataIntegrityViolationException exc){
				result = new ModelAndView("perfil/usuario/edit");
				createEditModelAndViewUsuario(usuario, "usuario.duplicated.user");
			}catch(Throwable oops){
				
				result = createEditModelAndViewUsuario(usuario, "usuario.commit.error");
			}
		}
		
		return result;
	}
	
	@RequestMapping(value="/empresario/edit", method = RequestMethod.GET)
	public ModelAndView profileEmpresarioEdit(){
		ModelAndView result;
		
		Empresario empresario = empresarioService.findByPrincipal();
	
		result = new ModelAndView("empresario/edit");
		
		result.addObject("actor",empresario);
		result.addObject("isCliente", true);
		result.addObject("isUsuario", false);
		result.addObject("isEmpresario", true);
		result.addObject("actionURI", "perfil/empresario/edit.do");
			
		return result;
		
	}
	
	@RequestMapping(value="/empresario/edit", method=RequestMethod.POST, params="save")
	public ModelAndView saveEmpresario(@ModelAttribute("actor") @Valid Empresario empresario, BindingResult binding){
		
		ModelAndView result;
		
		if(binding.hasErrors()){
			
				result = createEditModelAndViewEmpresario(empresario);
		}else{
			try{
				empresarioService.save(empresario);
				result = new ModelAndView("redirect:/summermate");
				
			}catch(DataIntegrityViolationException exc){
				result = new ModelAndView("perfil/empresario/edit");
				createEditModelAndViewEmpresario(empresario, "empresario.duplicated.user");
			}catch(Throwable oops){
				
				result = createEditModelAndViewEmpresario(empresario, "empresario.commit.error");
			}
		}
		
		return result;
	}
	
	@RequestMapping(value="/empresario", method = RequestMethod.GET)
	public ModelAndView profileEmpresario(){
		ModelAndView result;
		
		Empresario empresario = empresarioService.findByPrincipal();
	
		result = new ModelAndView("empresario");
		
		result.addObject("actor",empresario);
		result.addObject("isCliente", true);
		result.addObject("isUsuario", false);
		result.addObject("isEmpresario", true);
		result.addObject("requestURI", "perfil/empresario.do");
			
		return result;
		
	}
	
	protected ModelAndView createEditModelAndViewUsuario(Usuario usuario){
		
		ModelAndView result;
		
		result = createEditModelAndViewUsuario(usuario, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewUsuario(Usuario usuario, String message){
		
		ModelAndView result;
	
		result = new ModelAndView("usuario/edit");
		result.addObject("actor", usuario);
		result.addObject("message", message);
		result.addObject("isCliente", true);
		result.addObject("isUsuario", true);
		result.addObject("isEmpresario", false);

		return result;
	}
	
	protected ModelAndView createEditModelAndViewEmpresario(Empresario empresario){
		
		ModelAndView result;
		
		result = createEditModelAndViewEmpresario(empresario, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewEmpresario(Empresario empresario, String message){
		
		ModelAndView result;
	
		result = new ModelAndView("empresario/edit");
		result.addObject("actor", empresario);
		result.addObject("message", message);
		result.addObject("isUsuario", false);
		result.addObject("isCliente", true);
		result.addObject("isEmpresario", true);

		return result;
	}
	
}