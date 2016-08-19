package controllers.empresario;

import java.util.Date;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CalendarioNegocioService;
import controllers.AbstractController;
import domain.CalendarioNegocio;

@Controller
@RequestMapping("/calendarioNegocio/empresario")
public class CalendarioNegocioEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------

		@Autowired
		private CalendarioNegocioService calendarioNegocioService;
		
		// Constructors ---------------------------------------------------------------
		
		public CalendarioNegocioEmpresarioController(){
			super();
		}
		
				
	// Create and edition methods -------------------------------------------------
			
		@RequestMapping(value="/deleteDates", method = RequestMethod.GET)
		public ModelAndView deleteDates(@RequestParam int negocioId){
								
			ModelAndView result;
						
				CalendarioNegocio calendarioNegocio = calendarioNegocioService.create(negocioId);
				
				result =  new ModelAndView("calendarioNegocio/deleteDates");
				result.addObject("calendarioNegocio", calendarioNegocio);
				result.addObject("message", null);
				
				return result;
						
		}
		
		
		@RequestMapping(value="/deleteDates", method = RequestMethod.POST)
		public ModelAndView save(@Valid CalendarioNegocio calendarioNegocio, BindingResult binding){
			
		ModelAndView result;
		if(binding.hasErrors()){
			result =  new ModelAndView("calendarioNegocio/deleteDates");
			result.addObject("calendarioNegocio", calendarioNegocio);
			result.addObject("message", null);
			
		}else{
			try{
				if(calendarioNegocio.getFechaInicio().after(new Date()) 
					&& calendarioNegocio.getFechaFin().after(calendarioNegocio.getFechaInicio())){
					calendarioNegocioService.save(calendarioNegocio);
				result = new ModelAndView("redirect:/negocio/empresario/list.do");
				}else{
					result =  new ModelAndView("calendarioNegocio/deleteDates");
					result.addObject("calendarioNegocio", calendarioNegocio);
					result.addObject("message", "calendarioNegocio.dates.error");
				}
				
			}catch(Throwable oops){
				result =  new ModelAndView("calendarioNegocio/deleteDates");
				result.addObject("calendarioNegocio", calendarioNegocio);
				result.addObject("message", "calendarioNegocio.commit.error");
			}		
		}
			
		return result;
	}
	
}
