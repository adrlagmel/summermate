package controllers.actor;

import java.util.Collection;





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

import controllers.AbstractController;
import domain.Actor;
import domain.Carpeta;
import domain.Mensaje;
import forms.MensajeForm;

import services.ActorService;
import services.CarpetaService;
import services.MensajeService;
@Controller
@RequestMapping("/mensaje/actor")
public class MensajeActorController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private MensajeService mensajeService;
		
		@Autowired
		private ActorService actorService;
		
		@Autowired 
		private CarpetaService carpetaService;
		
		// Constructors ---------------------------------------------------------------
		
		public MensajeActorController(){
			super();
		}
		
		
		// Listing methods -----------------------------------------------------------
		@RequestMapping(value="/lista", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam int carpetaId){
			
			ModelAndView result;

			Carpeta carpeta = carpetaService.findOne(carpetaId);
			
			result = new ModelAndView("mensaje/actor/lista");
			result.addObject("carpeta", carpeta);
			
			return result;
			
		}
		
		@RequestMapping(value="/mostrar", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam int mensajeId){

			ModelAndView result;
			Mensaje mensaje;

			mensaje = mensajeService.findOne(mensajeId);
			result = new ModelAndView("mensaje/actor/mostrar");
			result.addObject("mostrarMensaje", mensaje);

			return result;
		}
		
		// Creation and edition methods ------------------------------------
		
		@RequestMapping(value="/enviar", method = RequestMethod.GET)
		public ModelAndView create(){
			
			ModelAndView result;
			
			result= createNewModelAndView(new MensajeForm());
			
			return result;
			
		}
		@RequestMapping(value = "/enviar", method = RequestMethod.POST, params = "save")
		public ModelAndView send(@ModelAttribute("m") @Valid MensajeForm m, BindingResult binding) {
			ModelAndView result;
			if (binding.hasErrors()) {
				result = createNewModelAndView(m);
			} else {
				try {				
					mensajeService.save(mensajeService.reconstruct(m));
					result = new ModelAndView("redirect:../../carpeta/actor/lista.do");
				} catch (Throwable oops) {

					result= createNewModelAndView(m,"mensaje.commit.error");
					
				}
			}
			return result;
		}
		
		@RequestMapping(value="/borrar", method = RequestMethod.POST)
		public ModelAndView delete(@RequestParam int mensajeId){
			
			ModelAndView result;
			
			Mensaje mensaje;
			mensaje=mensajeService.findOne(mensajeId);
			Assert.notNull(mensaje);
		
			mensajeService.delete(mensaje);
			result = new ModelAndView("redirect:../../carpeta/actor/lista.do");
	
			return result;
			
		}
		
	
		@RequestMapping(value="/responder", method = RequestMethod.GET)
		public ModelAndView reply(@RequestParam int mensajeId){
			
			ModelAndView result;
			
			Mensaje aux;
			MensajeForm mensaje = new MensajeForm();
			aux 	= mensajeService.findOne(mensajeId);
			Assert.notNull(aux);
			
			mensaje.setBeneficiario(aux.getRemitente());
			mensaje.setAsunto("Responder a:\""+aux.getAsunto()+"\"");
			mensaje.setCuerpo("\n-----------------\nRemitente: "+aux.getRemitente().getNombre()+"\n Beneficiario: " +
			aux.getBeneficiario().getNombre()+"\n Fecha Envio: "+aux.getFechaEnvio()+"\n Asunto: "+
					aux.getAsunto() + "\n Cuerpo: " + aux.getCuerpo()+"\"\"");
			
			
			result= createReplyModelAndView(mensaje);
			
			return result;
			
		}
		
		
		protected ModelAndView createNewModelAndView(MensajeForm m) {
			ModelAndView result;
			result = createNewModelAndView(m, null);
			return result;
		}

		protected ModelAndView createNewModelAndView(MensajeForm m,	String mensaje) {
			ModelAndView result;
			
			result = new ModelAndView("mensaje/actor/enviar");
			
			Collection<Actor> actors = actorService.findAllMessages();
			
			result.addObject("actors",actors);
			result.addObject("mensaje", mensaje);
			result.addObject("m", m);
			return result;
		}
		
		protected ModelAndView createReplyModelAndView(MensajeForm m) {
			ModelAndView result;
			result = createReplyModelAndView(m, null);
			return result;
		}

		protected ModelAndView createReplyModelAndView(MensajeForm m, String mensaje) {
			ModelAndView result;
			result = new ModelAndView("mensaje/actor/responder");
			
			result.addObject("mensaje", mensaje);
			result.addObject("m", m);
			result.addObject("m", m);
			return result;
		}
		
}