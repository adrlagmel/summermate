package controllers.administrador;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


import services.ActorService;
import services.NegocioService;
import services.UsuarioService;

import services.PlayaService;

import controllers.AbstractController;



import domain.Negocio;
import domain.Playa;
import domain.Usuario;


@Controller
@RequestMapping("/administrador")
public class DashboardAdministratorController extends AbstractController {

	
	// Services -------------------------------------------------------------------
	

	
	@Autowired
	private PlayaService playaService;
	
	@Autowired
	private NegocioService negocioService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	

	
	
	
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
			Collection<Playa> playaConMasValoraciones = playaService.findPlayaMasComentarios();
			Integer masValoraciones = playaService.masValoraciones();
			Collection<Negocio> negocioMejorValorado = negocioService.findNegocioMejorValorado();
			Collection<Negocio> negocioMasReservas = negocioService.negocioConMasReservas();
			Collection<Usuario> usuariosTotales = usuarioService.usuariosTotales();
			result = new ModelAndView("administrator/dashboard");
			
			
			result.addObject("playaMejorValorada", playaMejorValorada);
			result.addObject("playaConMasValoraciones", playaConMasValoraciones);
			result.addObject("masValoraciones", masValoraciones);
			result.addObject("negocioMejorValorado", negocioMejorValorado);
			result.addObject("negocioMasReservas", negocioMasReservas);
			result.addObject("usuariosTotales", usuariosTotales);
			
			result.addObject("requestURI", "administrador/dashboard.do");
			
			return result;
		}

}
