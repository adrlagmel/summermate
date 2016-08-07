package controllers;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.EventoService;
import controllers.AbstractController;
import domain.Evento;

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
		
		@RequestMapping(value="/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam int eventoId){
			ModelAndView result;
			
			Evento evento = eventoService.findOneToDisplay(eventoId);
			result 		  = createEditModelAndView(evento, "display");
			
			return result;			
		}
		
		protected ModelAndView createEditModelAndView(Evento evento, String selectView){
			ModelAndView result;
				
			result = createEditModelAndView(evento, selectView, null);
				
			return result;
		}
			
		protected ModelAndView createEditModelAndView(Evento evento, String selectView, String message){
			ModelAndView result;
			boolean hasimage = true;
			
			if(evento.getImagen() == null){
				hasimage = false;
			}
			
			result = new ModelAndView("evento/"+selectView);

			result.addObject("evento", evento);
			result.addObject("message", null);
			result.addObject("hasimage", hasimage);
			
			return result;
		}
			
}
