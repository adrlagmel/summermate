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

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Empresario;
import domain.Usuario;

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
	
}