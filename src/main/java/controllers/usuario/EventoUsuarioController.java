package controllers.usuario;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.EventoService;
import services.PlayaService;
import services.UsuarioService;
import controllers.AbstractController;
import domain.Evento;
import domain.Playa;
import domain.Usuario;

@Controller
@RequestMapping("/evento/usuario")
public class EventoUsuarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private EventoService eventoService;
		
		@Autowired
		private PlayaService playaService;
		
		@Autowired
		private UsuarioService usuarioService;
		
		// Constructors ---------------------------------------------------------------
		
		public EventoUsuarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Evento> eventos = null;
			
			eventos = eventoService.findByUsuario();
			
			result = new ModelAndView("evento/list");

			result.addObject("eventos", eventos);
		//	result.addObject("actionURI","evento/usuario/search.do");
			result.addObject("requestURI", "evento/usuario/list.do");

			return result;
			
		}
		
		
//		@RequestMapping(value="/search", method = RequestMethod.GET)
//		public ModelAndView search(@RequestParam String s, @RequestParam(required=false) Boolean showError, @RequestParam(required=false) Boolean showSuccess){
//			ModelAndView result;
//			
//			Collection<Evento> eventos = eventoService.searchByTown(s);
//			result = new ModelAndView("evento/usuario/list");
//			result.addObject("eventos", eventos);
//			result.addObject("showError", showError);
//			result.addObject("showSuccess", showSuccess);
//			result.addObject("actionURI","evento/usuario/search.do");
//			result.addObject("requestURI","evento/usuario/search.do");
//			
//			return result;
//			
//		}
		
				
	// Create and edition methods -------------------------------------------------
			
		
		@RequestMapping(value="/signup", method = RequestMethod.GET)
		public ModelAndView signup(@RequestParam int eventoId,@RequestParam int usuarioId){
						
			ModelAndView result;
			Evento evento = eventoService.findOneToEdit(eventoId);
			Usuario usuario = usuarioService.findOne(usuarioId);

			eventoService.participate(evento, usuario);
			
			result = new ModelAndView("redirect:list.do");
			
			return result;
				
		}
		
		
		// Ancillary methods ---------------------------------------------------------
		
		protected ModelAndView createEditModelAndView(Evento evento, String selectView){
			ModelAndView result;
				
			result = createEditModelAndView(evento, selectView, null);
				
			return result;
		}
			
		protected ModelAndView createEditModelAndView(Evento evento, String selectView, String message){
			ModelAndView result;
			Collection<Playa> playas = playaService.findAllBeaches();
				
			result = new ModelAndView("evento/"+selectView);

			result.addObject("evento", evento);
			result.addObject("playas", playas);
			result.addObject("message", null);

			return result;
		}
			
}
