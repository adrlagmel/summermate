package controllers.actor;

import java.util.Collection;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CarpetaService;

import controllers.AbstractController;
import domain.Actor;
import domain.Carpeta;


@Controller
@RequestMapping("/carpeta/actor")
public class FolderActorController extends AbstractController{
	
	// Services -------------------------------------------------------------------
		@Autowired
		private CarpetaService folderService;
		
		@Autowired
		private ActorService actorService;	
		
		// Constructors ---------------------------------------------------------------
		
		public FolderActorController(){
			super();
		}
		
		// Listing methods -----------------------------------------------------------
		
		@RequestMapping(value="/lista", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Carpeta> carpetas;
			Actor actor = actorService.findByPrincipal();
			
			carpetas = folderService.findCarpetasByActor(actor);
						
			result = new ModelAndView("carpeta/actor/lista");
			result.addObject("carpetas", carpetas);
			result.addObject("requestURI", "carpeta/actor/lista.do");
			
			return result;
		}
		
		// Ancillary methods ---------------------------------------------------------
		
		protected ModelAndView createEditModelAndView(Carpeta carpeta, String selectView){
			
			ModelAndView result;
			
			result = createEditModelAndView(carpeta, selectView, null);
			
			return result;
		}
		
		protected ModelAndView createEditModelAndView(Carpeta carpeta, String selectView, String message){
			
			ModelAndView result;
			
			Actor actor = actorService.findByPrincipal();
			Collection<Carpeta> carpetas = folderService.findCarpetasByActor(actor);
			carpetas.remove(carpeta);
						
			result = new ModelAndView("carpeta/actor/" + selectView);
			result.addObject("carpeta", carpeta);
			result.addObject("carpetas", carpetas);
			result.addObject("message", message);

			return result;
		}
		
}
