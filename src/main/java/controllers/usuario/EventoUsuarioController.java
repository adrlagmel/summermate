package controllers.usuario;

import java.util.Collection;
import java.util.Date;


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
			Date fechaActual	= new Date();
			
			eventos = eventoService.findAll();
			Usuario usuario = usuarioService.findByPrincipal();
			
			result = new ModelAndView("evento/list");

			result.addObject("usuario", usuario);
			result.addObject("eventos", eventos);
			result.addObject("fechaActual", fechaActual);
		//	result.addObject("actionURI","evento/usuario/search.do");
			result.addObject("requestURI", "evento/usuario/list.do");

			return result;
			
		}
		
		@RequestMapping(value="/listActivos", method = RequestMethod.GET)
		public ModelAndView listActivos(){
			
			ModelAndView result;
			Collection<Evento> eventos = null;
			
			eventos = eventoService.EventosActivos();
			Usuario usuario = usuarioService.findByPrincipal();
			Date fechaActual	= new Date();
			result = new ModelAndView("evento/list");

			result.addObject("usuario", usuario);
			result.addObject("eventos", eventos);
			result.addObject("fechaActual", fechaActual);
			result.addObject("requestURI", "evento/usuario/listActivos.do");

			return result;
			
		}
		@RequestMapping(value="/listInactivos", method = RequestMethod.GET)
		public ModelAndView listInactivos(){
			
			ModelAndView result;
			Collection<Evento> eventos = null;
			
			eventos = eventoService.EventosInactivos();
			Usuario usuario = usuarioService.findByPrincipal();
			Date fechaActual	= new Date();
			result = new ModelAndView("evento/list");

			result.addObject("usuario", usuario);
			result.addObject("eventos", eventos);
			result.addObject("fechaActual", fechaActual);
			result.addObject("requestURI", "evento/usuario/listInactivos.do");

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
		public ModelAndView signup(@RequestParam int eventoId){
						
			ModelAndView result;
			
			Evento evento 	= eventoService.findOneToDisplay(eventoId);
			Usuario usuario = usuarioService.findByPrincipal();

			eventoService.participate(evento, usuario);
			
			result = new ModelAndView("redirect:list.do");
			
			return result;
				
		}
		
		@RequestMapping(value="/unregister", method = RequestMethod.GET)
		public ModelAndView unregister(@RequestParam int eventoId){
						
			ModelAndView result;
			
			Evento evento 	= eventoService.findOneToDisplay(eventoId);
			Usuario usuario = usuarioService.findByPrincipal();

			eventoService.unregister(evento, usuario);
			
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
