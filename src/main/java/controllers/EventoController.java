package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import services.EventoService;
import services.NegocioService;
import controllers.AbstractController;
import domain.Evento;
import domain.Negocio;

@Controller
@RequestMapping("/evento")
public class EventoController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private EventoService eventoService;
		
		
		// Constructors ---------------------------------------------------------------
		
		public EventoController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		

		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Evento> eventos = null;
			
			eventos = eventoService.findAll();
			
			result = new ModelAndView("evento/list");

			result.addObject("eventos", eventos);
		//	result.addObject("actionURI","evento/usuario/search.do");
			result.addObject("requestURI", "evento/list.do");

			return result;
			
		}
			
}
