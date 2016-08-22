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

import javax.validation.Valid;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Empresario;
import domain.Usuario;
import forms.ActorEditPasswordForm;
import forms.EmpresarioEditForm;
import forms.UsuarioEditForm;
import services.ActorService;
import services.EmpresarioService;
import services.UsuarioService;

@Controller
@RequestMapping("/perfil")
public class ProfileController extends AbstractController {
	

	// Services -------------------------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private EmpresarioService empresarioService;
	
	// Profile ---------------------------------------------------------------		
		
	
	@RequestMapping(value="/usuario", method = RequestMethod.GET)
	public ModelAndView profileUsuario(){
		ModelAndView result;
		
		Usuario usuario = usuarioService.findByPrincipal();
	
		System.out.println(usuario.getUserAccount().getAuthorities());
		
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
		
		Usuario usuario = usuarioService.findByPrincipal();
		
		result = new ModelAndView("usuario/edit");
				
		result.addObject("actor",usuario);
		result.addObject("isCliente", true);
		result.addObject("isUsuario", true);
		result.addObject("actionURI", "perfil/usuario/edit.do");
			
		return result;
		
	}
	
	@RequestMapping(value="/usuario/edit", method=RequestMethod.POST, params="save")
	public ModelAndView saveUsuario(@ModelAttribute("actor") @Valid UsuarioEditForm usuario, BindingResult binding){
		
		ModelAndView result;
		Usuario userRes;
		
		if(binding.hasErrors()){
				result = createEditModelAndViewUsuario(usuario);
		}else{
			try{
				userRes = usuarioService.reconstructEdit(usuario);
				
				usuarioService.saveEdit(userRes);
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
	public ModelAndView saveEmpresario(@ModelAttribute("actor") @Valid EmpresarioEditForm empresario, BindingResult binding){
		ModelAndView result;
		
		Empresario empresarioRes;
		
		if(binding.hasErrors()){
			
				result = createEditModelAndViewEmpresario(empresario);
		}else{
			try{
				empresarioRes  = empresarioService.reconstructEdit(empresario);
				empresarioService.saveEdit(empresarioRes);
				result = new ModelAndView("redirect:/#");
				
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
		
		result.addObject("actor", empresario);
		result.addObject("isCliente", true);
		result.addObject("isUsuario", false);
		result.addObject("isEmpresario", true);
		result.addObject("requestURI", "perfil/empresario.do");
			
		return result;
		
	}
	
	
	@RequestMapping(value="/actor/editPassword", method = RequestMethod.GET)
	public ModelAndView profileActor(){
		ModelAndView result;
		
		ActorEditPasswordForm form = new ActorEditPasswordForm();
		
		result = createEditModelAndViewActor(form);
			
		return result;
		
	}
	
	@RequestMapping(value="/actor/editPassword", method=RequestMethod.POST, params="save")
	public ModelAndView savePassword(@ModelAttribute("actor") @Valid ActorEditPasswordForm actorForm, BindingResult binding){
		ModelAndView result;
		
		Actor actor = actorService.findByPrincipal();
		String passwordOld = actorForm.getOldPassword();
		
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		passwordOld = encoder.encodePassword(actorForm.getOldPassword(), null);
		
		if(binding.hasErrors()){
				result = createEditModelAndViewActor(actorForm);
		}else{
			
			
			try{
				if (!passwordOld.equals(actor.getUserAccount().getPassword())){
					System.out.println(passwordOld);
					System.out.println(actor.getUserAccount().getPassword());
					result = createEditModelAndViewActor(actorForm, "actor.password.error");
				}
				
				else if (!actorForm.getPassword().equals(actorForm.getVerifyPassword())){
					result = createEditModelAndViewActor(actorForm, "actor.password.error.coincidir");
					
					System.out.println(actorForm.getPassword());
					System.out.println(actorForm.getVerifyPassword());
				}else{
									
					actor  = actorService.reconstructPassword(actorForm);
					actorService.savePassword(actor);
					result = new ModelAndView("redirect:/#");
				}
				
			}catch(Throwable oops){
				result = createEditModelAndViewActor(actorForm, "actor.commit.error");
			}
		}
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewUsuario(UsuarioEditForm usuarioEditForm){
		
		ModelAndView result;
		
		result = createEditModelAndViewUsuario(usuarioEditForm, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewUsuario(UsuarioEditForm usuarioEditForm, String message){
		
		ModelAndView result;
	
		result = new ModelAndView("usuario/edit");
		result.addObject("actor", usuarioEditForm);
		result.addObject("message", message);
		result.addObject("isCliente", true);
		result.addObject("isUsuario", true);
		result.addObject("isEmpresario", false);
		result.addObject("actionURI", "perfil/usuario/edit.do");

		return result;
	}
	
	protected ModelAndView createEditModelAndViewEmpresario(EmpresarioEditForm empresarioEditForm){
		
		ModelAndView result;
		
		result = createEditModelAndViewEmpresario(empresarioEditForm, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewEmpresario(EmpresarioEditForm empresarioEditForm, String message){
		
		ModelAndView result;
	
		result = new ModelAndView("empresario/edit");
		result.addObject("actor", empresarioEditForm);
		result.addObject("message", message);
		result.addObject("isUsuario", false);
		result.addObject("isCliente", true);
		result.addObject("isEmpresario", true);
		result.addObject("actionURI", "perfil/empresario/edit.do");

		return result;
	}
	
	protected ModelAndView createEditModelAndViewActor(ActorEditPasswordForm actorEditPasswordForm){
		ModelAndView result;
		
		result = createEditModelAndViewActor(actorEditPasswordForm, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewActor(ActorEditPasswordForm actorEditPasswordForm, String message){
		
		ModelAndView result;
	
		result = new ModelAndView("actor/editPassword");
		result.addObject("actor", actorEditPasswordForm);
		result.addObject("message", message);
		result.addObject("actionURI", "perfil/actor/editPassword.do");

		return result;
	}
	
}