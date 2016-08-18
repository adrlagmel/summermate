package controllers.empresario;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ReservaService;
import controllers.AbstractController;
import domain.Reserva;

@Controller
@RequestMapping("/reserva/empresario")
public class ReservaEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private ReservaService reservaService;
		

		// Constructors ---------------------------------------------------------------
		
		public ReservaEmpresarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		
		@RequestMapping(value="/lista", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Reserva> reservas = reservaService.findReservasPorEmpresario();
			
			result = new ModelAndView("reserva/lista");

			result.addObject("reservas", reservas);
			result.addObject("actionURI","reserva/empresario/search.do");
			result.addObject("requestURI","reserva/empresario/lista.do");
			
						
			return result;
			
		}
		
		
		@RequestMapping(value="/search", method = RequestMethod.GET)
		public ModelAndView search(@RequestParam String s, @RequestParam(required=false) Boolean showError, @RequestParam(required=false) Boolean showSuccess){
			ModelAndView result;
			
			Collection<Reserva> reservas = reservaService.searchByDate(s);
			result = new ModelAndView("reserva/lista");
			
			result.addObject("reservas", reservas);
			result.addObject("showError", showError);
			result.addObject("showSuccess", showSuccess);
			result.addObject("actionURI", "reserva/empresario/search.do");
			result.addObject("requestURI","reserva/empresario/search.do");
			
			return result;
			
		}

}
