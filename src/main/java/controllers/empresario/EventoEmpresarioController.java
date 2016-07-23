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
import services.PlayaService;
import controllers.AbstractController;
import domain.Evento;
import domain.Negocio;
import domain.Playa;

@Controller
@RequestMapping("/evento/empresario")
public class EventoEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private EventoService eventoService;
		
		
		
		// Constructors ---------------------------------------------------------------
		
		public EventoEmpresarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		
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
					

		// Ancillary methods ---------------------------------------------------------
		
		protected ModelAndView createEditModelAndView(Evento evento, String selectView){
			ModelAndView result;
				
			result = createEditModelAndView(evento, selectView, null);
				
			return result;
		}
			
		protected ModelAndView createEditModelAndView(Evento evento, String selectView, String message){
			ModelAndView result;
			boolean hasimage = true;
			
			if(evento.getImagen() == null){
				hasimage = false;
			}
			
			result = new ModelAndView("evento/"+selectView);

			result.addObject("evento", evento);
			result.addObject("message", null);
			result.addObject("hasimage", hasimage);
			

			return result;
		}
			
}
