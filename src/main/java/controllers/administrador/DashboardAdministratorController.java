package controllers.administrador;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import services.ActorService;

import services.PlayaService;

import controllers.AbstractController;



import domain.Playa;


@Controller
@RequestMapping("/administrador")
public class DashboardAdministratorController extends AbstractController {

	
	// Services -------------------------------------------------------------------
	

	
	@Autowired
	private PlayaService playaService;
	

	
	
	
	// Constructors ---------------------------------------------------------------
	
		public DashboardAdministratorController(){
			super();
		}
		
		// Listing methods -----------------------------------------------------------
		
		@RequestMapping(value="/dashboard", method = RequestMethod.GET)
		public ModelAndView dashboard(){
			
			ModelAndView result;
			
			//Lista de Consultas
			Collection<Playa> playaMejorValorada = playaService.playaMejorValorada();
			result = new ModelAndView("administrator/dashboard");
			
			
			result.addObject("playaMejorValorada", playaMejorValorada);
			
			result.addObject("requestURI", "administrador/dashboard.do");
			
			return result;
		}

}
