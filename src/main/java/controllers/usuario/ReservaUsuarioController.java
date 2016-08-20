package controllers.usuario;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NegocioService;
import services.ReservaService;
import controllers.AbstractController;
import domain.Negocio;
import domain.Reserva;
import forms.ReservaForm;

@Controller
@RequestMapping("/reserva/usuario")
public class ReservaUsuarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private ReservaService reservaService;
		
		@Autowired
		private NegocioService negocioService;

		
		// Constructors ---------------------------------------------------------------
		
		public ReservaUsuarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		
		@RequestMapping(value="/lista", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Reserva> reservas = reservaService.findReservasByUsuario();
			
			result = new ModelAndView("reserva/lista");

			result.addObject("reservas", reservas);
			result.addObject("requestURI","reserva/usuario/lista.do");
			
						
			return result;
			
		}
		
		
	// Create and edition methods -------------------------------------------------
		
		
		@RequestMapping(value="/create", method = RequestMethod.GET)
		public ModelAndView create(@RequestParam int negocioId){
						
			ModelAndView result;
				
			ReservaForm form = new ReservaForm();
			Negocio negocio = negocioService.findOneToDisplay(negocioId);
			
			form.setNegocio(negocio);
			form.setReservaId(0);
														
			result = createEditModelAndView(form);
				
			return result;
				
		}
		
		@RequestMapping(value="/create", method=RequestMethod.POST, params="save")
		public ModelAndView save(@ModelAttribute("form") @Valid ReservaForm form, BindingResult binding){
				
			ModelAndView result;
			Reserva reserva = null;
			if(binding.hasErrors()){
						result = createEditModelAndView(form);
						
			}else{
				try{
					if(form.getFecha().after(new Date())){
						reserva = reservaService.reconstruct(form);
						
						reservaService.save(reserva);
						result = new ModelAndView("redirect:lista.do");
					}else
						result = createEditModelAndView(form,"booking.dates.error");
					
				}catch(Throwable oops){
						result = createEditModelAndView(form,"booking.commit.error");
				}
			}
				
			return result;
		}
			

		@RequestMapping(value="/delete", method = RequestMethod.GET)
		public ModelAndView delete(@RequestParam int reservaId){
			
			ModelAndView result;
			
			Reserva dv = reservaService.findOneToEdit(reservaId);
			Assert.notNull(dv);
		
			reservaService.delete(dv);
			result = new ModelAndView("redirect:lista.do");

			return result;
			
		}
		
		// Ancillary methods ---------------------------------------------------------
			
		protected ModelAndView createEditModelAndView(ReservaForm form){
				
			ModelAndView result;
				
			result = createEditModelAndView(form, null);
				
			return result;
		}
			
		protected ModelAndView createEditModelAndView(ReservaForm form, String message){

			ModelAndView result;
				
			result = new ModelAndView("reserva/create");
			result.addObject("form", form);
			result.addObject("message", message);

			return result;
		}

}
