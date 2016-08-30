package controllers.empresario;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.EmpresarioService;
import services.EventoService;
import services.NegocioService;
import services.PeticionNegocioService;
import controllers.AbstractController;
import domain.Empresario;
import domain.Evento;
import domain.Negocio;
import domain.PeticionNegocio;
import domain.Usuario;

@Controller
@RequestMapping("/evento/empresario")
public class EventoEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private EventoService eventoService;
		
		@Autowired
		private NegocioService negocioService;
		
		@Autowired
		private ActorService actorService;
		
		@Autowired
		private EmpresarioService empresarioService;
		
		@Autowired
		private PeticionNegocioService peticionService;
		
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
		
		@RequestMapping(value="/listParticipantes", method = RequestMethod.GET)
		public ModelAndView listParticipantes(@RequestParam int eventoId){
			
			ModelAndView result;
			Collection<Usuario> participantes = null;
			
			participantes = eventoService.findParticipantsByEvento(eventoId);
			
			result = new ModelAndView("evento/listParticipantes");

			result.addObject("participantes", participantes);
			
		//	result.addObject("actionURI","evento/empresario/search.do");
			result.addObject("requestURI", "evento/empresario/listParticipantes.do");

			return result;
			
		}
		
		@RequestMapping(value="/listActivos", method = RequestMethod.GET)
		public ModelAndView listActivos(){
			
			ModelAndView result;
			Collection<Evento> eventos = null;
			
			eventos = eventoService.EventosActivos();
			Date fechaActual	= new Date();
			result = new ModelAndView("evento/list");

			result.addObject("eventos", eventos);
			
			result.addObject("requestURI", "evento/empresario/listActivos.do");

			return result;
			
		}
		@RequestMapping(value="/listInactivos", method = RequestMethod.GET)
		public ModelAndView listInactivos(){
			
			ModelAndView result;
			Collection<Evento> eventos = null;
			
			eventos = eventoService.EventosInactivos();
			Date fechaActual	= new Date();
			result = new ModelAndView("evento/list");

			result.addObject("eventos", eventos);
		
			result.addObject("requestURI", "evento/empresario/listInactivos.do");

			return result;
			
		}
		
		// Create and edition methods -------------------------------------------------
		
			@RequestMapping(value="/register", method = RequestMethod.GET)
			public ModelAndView create(){
				Empresario empresario = empresarioService.findByPrincipal();
				
				if(negocioService.findNegociosActivos(empresario.getId()).isEmpty()){
					ModelAndView result;
										
					result = new ModelAndView("redirect:../../negocio/empresario/list.do");
					result.addObject("showalta", true);
					
					
					return result;
				}
					
				if(actorService.findByPrincipal().equals(empresarioService.findByPrincipal()) && empresarioService.findByPrincipal().getNegocios().isEmpty()){
					
					PeticionNegocio pet = peticionService.findPeticionNegocioPorEmpresario(empresario);
					
					if(pet.getEstado().equals("ACEPTADO")){
					ModelAndView result;
									
					result = new ModelAndView("redirect:../../negocio/empresario/register.do");
					return result;
					}else{
						ModelAndView result;
						
						result = new ModelAndView("redirect:../../peticionNegocio/empresario/list.do");
						return result;
					}
				}else{
					ModelAndView result;
					
					Evento evento = eventoService.create();		
					result 		  = createEditModelAndView(evento, "register");
				
				return result;		
				}
			}
				
			
			@RequestMapping(value="/edit", method = RequestMethod.GET)
			public ModelAndView edit(@RequestParam int eventoId){
				ModelAndView result;
								
				Evento evento = eventoService.findOneToEdit(eventoId);
				
				Assert.isTrue(evento.getNegocio().getNegocioActivo());
				
				result 		  = createEditModelAndView(evento, "edit");
				
				return result;			
			}
	
				
			@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
			public ModelAndView save(@ModelAttribute("evento") @Valid Evento evento, BindingResult binding){
				
			ModelAndView result;
				if(binding.hasErrors()){
					result = createEditModelAndView(evento, "edit");
					
				}else{
					try{
						if(evento.getFechaCelebracion().after(new Date())){
										
							Assert.isTrue(evento.getNegocio().getNegocioActivo());
							
							eventoService.save(evento);
							result = new ModelAndView("redirect:list.do");
						}else
							result = createEditModelAndView(evento, "edit", "evento.dates.error");
												
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
			
			Collection<Negocio> negocios = negocioService.findNegociosActivos(empresarioService.findByPrincipal().getId());
			
			if(evento.getImagen() == null){
				hasimage = false;
			}
			
			result = new ModelAndView("evento/"+selectView);

			result.addObject("evento", evento);
			result.addObject("message", message);
			result.addObject("hasimage", hasimage);
			result.addObject("negocios", negocios);
			
			return result;
		}
			
}
