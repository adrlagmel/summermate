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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import services.EventoService;
import services.NegocioService;
import controllers.AbstractController;
import domain.Evento;
import domain.Negocio;

@Controller
@RequestMapping("/evento/empresario")
public class EventoEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private EventoService eventoService;
		
		@Autowired
		private NegocioService negocioService;
		
		// Constructors ---------------------------------------------------------------
		
		public EventoEmpresarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		

		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Evento> eventos = null;
			
			eventos = eventoService.findByEmpresario();
			
			result = new ModelAndView("evento/list");

			result.addObject("eventos", eventos);
		//	result.addObject("actionURI","evento/empresario/search.do");
			result.addObject("requestURI", "evento/empresario/list.do");

			return result;
			
		}
		
		// Create and edition methods -------------------------------------------------
		
			@RequestMapping(value="/register", method = RequestMethod.GET)
			public ModelAndView create(@RequestParam int negocioId){
				ModelAndView result;
				Negocio negocio = negocioService.findOneToDisplay(negocioId);
				
				Evento evento = eventoService.create(negocio);		
				result 		  = createEditModelAndView(evento, "register");
				
				return result;		
			}
			
			@RequestMapping(value="/edit", method = RequestMethod.GET)
			public ModelAndView edit(@RequestParam int eventoId){
				ModelAndView result;
				
				Evento evento = eventoService.findOneToEdit(eventoId);
				result 		  = createEditModelAndView(evento, "edit");
				
				return result;			
			}
			
			@RequestMapping(value="/display", method = RequestMethod.GET)
			public ModelAndView display(@RequestParam int eventoId){
				ModelAndView result;
				
				Evento evento = eventoService.findOneToDisplay(eventoId);
				result 		  = createEditModelAndView(evento, "display");
				
				return result;			
			}
				
			@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
			public ModelAndView save(@ModelAttribute("evento") @Valid Evento evento, BindingResult binding){
				
			ModelAndView result;
				if(binding.hasErrors()){
					result = createEditModelAndView(evento, "edit");
					
				}else{
					try{
						eventoService.save(evento);
						result = new ModelAndView("redirect:list.do");
						
					}catch(Throwable oops){
						result = createEditModelAndView(evento, "edit", "evento.commit.error");
					}		
				}
			return result;
			}
				
				
		// Ancillary methods ---------------------------------------------------------
			
			@RequestMapping(value="/uploadImageEvento", method=RequestMethod.GET)
			public ModelAndView uploadImage (@RequestParam int eventoId){
				ModelAndView result;
				Evento e = eventoService.findOneToEdit(eventoId);
				
				boolean hasimage=true;

				if(e.getImagen()==null){
					hasimage=false;
				}
				
				result = createEditModelAndView(e, "uploadImage");
				result.addObject("eventoId", eventoId);
				result.addObject("hasimage",hasimage);
				
				
				return result;
			}
					
					
			@RequestMapping (value="/uploadImageEvento", method=RequestMethod.POST, params="save")
			public ModelAndView addImagenNegocio(@RequestParam int eventoId, @RequestParam("foto") MultipartFile file){
				ModelAndView result;
				Evento e = eventoService.findOneToEdit(eventoId);

				try{
					
					eventoService.addImageToEvento(eventoId, file.getBytes());
					result = new ModelAndView("redirect:../../evento/empresario/list.do");
					
				}catch(Throwable oops){
					boolean hasimage=true;

					if(e.getImagen()==null){
						hasimage=false;
					}
					
					result = createEditModelAndView(e, "uploadImage");
					result.addObject("eventoId", eventoId);
					result.addObject("hasimage", hasimage);
					result.addObject("message", "evento.commit.error");
									
				}
				
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
			Collection<Negocio> negocios = negocioService.findByEmpresario();
			
			if(evento.getImagen() == null){
				hasimage = false;
			}
			
			result = new ModelAndView("evento/"+selectView);

			result.addObject("evento", evento);
			result.addObject("message", null);
			result.addObject("hasimage", hasimage);
			result.addObject("negocios", negocios);
			
			return result;
		}
			
}
