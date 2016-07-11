package controllers;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NegocioService;
import controllers.AbstractController;
import domain.Negocio;

@Controller
@RequestMapping("/negocio")
public class NegocioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private NegocioService negocioService;
		
		// Constructors ---------------------------------------------------------------
		
		public NegocioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Negocio> negocios = null;
			
			negocios = negocioService.findAll();
			
			result = new ModelAndView("negocio/list");

			result.addObject("negocios", negocios);
			result.addObject("actionURI","negocio/search.do");
			result.addObject("requestURI", "negocio/list.do");

			return result;
			
		}
		
		
		@RequestMapping(value="/search", method = RequestMethod.GET)
		public ModelAndView search(@RequestParam String s,@RequestParam(required=false) Boolean showError, @RequestParam(required=false) Boolean showSuccess){
			ModelAndView result;
			
			Collection<Negocio> negocios = negocioService.searchByTown(s);
			result = new ModelAndView("negocio/list");
			result.addObject("negocios", negocios);
			result.addObject("showError", showError);
			result.addObject("showSuccess", showSuccess);
			result.addObject("actionURI","negocio/search.do");
			result.addObject("requestURI","negocio/search.do");
			
			return result;
			
		}
		
			
}
