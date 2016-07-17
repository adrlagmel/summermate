package controllers.empresario;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MesaService;
import controllers.AbstractController;
import domain.Mesa;

@Controller
@RequestMapping("/mesa/empresario")
public class MesaEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private MesaService mesaService;
		
		// Constructors ---------------------------------------------------------------
		
		public MesaEmpresarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Mesa> mesas = null;
			
			mesas = mesaService.findMesasByEmpresario();
			
			result = new ModelAndView("mesa/list");

			result.addObject("mesas", mesas);
			result.addObject("requestURI","mesa/empresario/list.do");

			return result;
			
		}
				
				
	// Create and edition methods -------------------------------------------------
			
		@RequestMapping(value="/register", method = RequestMethod.GET)
		public ModelAndView create(@RequestParam int negocioId){
								
			ModelAndView result;
						
				Mesa mesa = mesaService.create(negocioId);
				
				result =  createEditModelAndView(mesa, "register");
				
				return result;
						
		}
		
		@RequestMapping(value="/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam int mesaId){
								
				ModelAndView result;
				
				Mesa mesa = mesaService.findOneToEdit(mesaId);
				
				result = createEditModelAndView(mesa, "edit");
				
				return result;
						
		}
		
		@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView save(@ModelAttribute("mesa") @Valid Mesa mesa, BindingResult binding){
			
		ModelAndView result;
		if(binding.hasErrors()){
			result = createEditModelAndView(mesa, "edit");
			
		}else{
			try{
				mesaService.save(mesa);
				result = new ModelAndView("redirect:list.do");
				
			}catch(Throwable oops){
				result = createEditModelAndView(mesa, "edit", "mesa.commit.error");
			}		
		}
			
		return result;
	}
		
		// Ancillary methods ---------------------------------------------------------
		
		protected ModelAndView createEditModelAndView(Mesa mesa, String selectView){
				
			ModelAndView result;
				
			result = createEditModelAndView(mesa, selectView, null);
				
			return result;
		}
			
		protected ModelAndView createEditModelAndView(Mesa mesa, String selectView, String message){

			ModelAndView result;
				
			result = new ModelAndView("mesa/"+selectView);

			result.addObject("mesa", mesa);
			result.addObject("message", null);

			return result;
		}	
			
}
