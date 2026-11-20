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
		public ModelAndView list(Paginacion paginacion){
			
			ModelAndView result;
			PaginaDisplaytag<Negocio> negocios = new PaginaDisplaytag<>(negocioService.findAll(paginacion.negocios()));
			
			result = new ModelAndView("negocio/list");

			result.addObject("negocios", negocios);
			result.addObject("actionURI","negocio/search.do");
			result.addObject("requestURI", "negocio/list.do");

			return result;
			
		}
		
		@RequestMapping(value="/listNegocios", method = RequestMethod.GET)
		public ModelAndView listNegocios(@RequestParam int playaId, Paginacion paginacion){
			
			ModelAndView result;
			PaginaDisplaytag<Negocio> negocios = new PaginaDisplaytag<>(negocioService.findNegociosDePlaya(playaId, paginacion.negocios()));
			
			result = new ModelAndView("negocio/list");

			result.addObject("negocios", negocios);
			result.addObject("actionURI","negocio/search.do");
			// Antes apuntaba a list.do y al cambiar de página se perdía la playa
			result.addObject("requestURI", "negocio/listNegocios.do");

			return result;
			
		}
		
		
		@RequestMapping(value="/search", method = RequestMethod.GET)
		public ModelAndView search(@RequestParam String s,@RequestParam(required=false) Boolean showError, @RequestParam(required=false) Boolean showSuccess, Paginacion paginacion){
			ModelAndView result;
			
			PaginaDisplaytag<Negocio> negocios = new PaginaDisplaytag<>(negocioService.searchByTown(s, paginacion.negocios()));
			result = new ModelAndView("negocio/list");
			result.addObject("negocios", negocios);
			result.addObject("showError", showError);
			result.addObject("showSuccess", showSuccess);
			result.addObject("actionURI","negocio/search.do");
			result.addObject("requestURI","negocio/search.do");
			
			return result;
			
		}
		
		
			
}
