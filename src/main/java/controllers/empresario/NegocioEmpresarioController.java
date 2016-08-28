package controllers.empresario;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import services.EmpresarioService;
import services.NegocioService;
import services.PeticionNegocioService;
import services.PlayaService;
import controllers.AbstractController;
import domain.Empresario;
import domain.Negocio;
import domain.PeticionNegocio;
import domain.Playa;

@Controller
@RequestMapping("/negocio/empresario")
public class NegocioEmpresarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
		@Autowired
		private NegocioService negocioService;
		
		@Autowired
		private PlayaService playaService;
		
		@Autowired
		private EmpresarioService empresarioService;
		
		@Autowired
		private PeticionNegocioService peticionService;
		// Constructors ---------------------------------------------------------------
		
		public NegocioEmpresarioController(){
			super();
		}
		
		// Listing methods ---------------------------------------------------------------
		
		@RequestMapping(value="/list", method = RequestMethod.GET)
		public ModelAndView list(){
			
			ModelAndView result;
			Collection<Negocio> negocios = null;
			
			Empresario empresario = empresarioService.findByPrincipal();
			PeticionNegocio pet = peticionService.findPeticionNegocioPorEmpresario(empresario);
			
			boolean estado = false;
			
			if (pet == null){
				result = new ModelAndView("peticionNegocio/empresario/list");

				result.addObject("estado", estado);
				result.addObject("negocios", negocios);
				
			}else{
			
				if(pet.getEstado().equals("ACEPTADO")){
					estado = true;
				}
				
				negocios = negocioService.findByEmpresario();
				
				result = new ModelAndView("negocio/list");
	
				result.addObject("estado", estado);
				result.addObject("negocios", negocios);
				result.addObject("actionURI","negocio/empresario/search.do");
				result.addObject("requestURI", "negocio/empresario/list.do");
			}
			
			return result;
			
		}
		
		@RequestMapping(value="/search", method = RequestMethod.GET)
		public ModelAndView search(@RequestParam String s, @RequestParam(required=false) Boolean showError, @RequestParam(required=false) Boolean showSuccess){
			ModelAndView result;
			
			Collection<Negocio> negocios = negocioService.searchByTown(s);
			result = new ModelAndView("negocio/list");
			result.addObject("negocios", negocios);
			result.addObject("showError", showError);
			result.addObject("showSuccess", showSuccess);
			result.addObject("actionURI","negocio/empresario/search.do");
			result.addObject("requestURI","negocio/empresario/search.do");
			
			return result;
			
		}
		
				
	// Create and edition methods -------------------------------------------------
			
		@RequestMapping(value="/register", method = RequestMethod.GET)
		public ModelAndView create(){
			ModelAndView result;
					
			Negocio negocio = negocioService.create();		
			result 			= createEditModelAndView(negocio, "register");
			
			return result;
						
		}
		
		@RequestMapping(value="/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam int negocioId){
			ModelAndView result;
			
			Negocio negocio = negocioService.findOneToEdit(negocioId);
			result 			= createEditModelAndView(negocio, "edit");
			
			return result;
						
		}
		
		@RequestMapping(value="/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam int negocioId){
				ModelAndView result;
				
				Negocio negocio = negocioService.findOneToDisplay(negocioId);
				result 			= createEditModelAndView(negocio, "display");
				
				return result;
						
		}
		
		@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView save(@ModelAttribute("negocio") @Valid Negocio negocio, BindingResult binding){
			
		ModelAndView result;
		if(binding.hasErrors()){
			result = createEditModelAndView(negocio, "edit");
			
		}else{
			try{
				negocioService.save(negocio);
				result = new ModelAndView("redirect:list.do");
				
			}catch(Throwable oops){
				result = createEditModelAndView(negocio, "edit", "negocio.commit.error");
			}		
		}
			
		return result;
	}
		
		@RequestMapping(value="/suspender", method = RequestMethod.GET)
		public ModelAndView suspender(@RequestParam int negocioId){
			
			ModelAndView result;
			
			Negocio negocio = negocioService.findOneToEdit(negocioId);
			Assert.notNull(negocio);
			Assert.isTrue(negocio.getNegocioActivo());
		
			negocioService.suspenderNegocio(negocio);
			result = new ModelAndView("redirect:list.do");

			return result;
			
		}
		
		@RequestMapping(value="/alta", method = RequestMethod.GET)
		public ModelAndView alta(@RequestParam int negocioId){
			
			ModelAndView result;
			
			Negocio negocio = negocioService.findOneToEdit(negocioId);
			Assert.notNull(negocio);
			Assert.isTrue(!negocio.getNegocioActivo());
		
			negocioService.altaNegocio(negocio);
			result = new ModelAndView("redirect:list.do");

			return result;
		}
		
		// Ancillary methods ---------------------------------------------------------
		@RequestMapping(value="/uploadImageNegocio", method=RequestMethod.GET)
		public ModelAndView uploadImage (@RequestParam int negocioId){
			ModelAndView result;
			Negocio n = negocioService.findOneToEdit(negocioId);
			
			boolean hasimage=true;

			if(n.getImagen()==null){
				hasimage=false;
			}
			
			result = createEditModelAndView(n, "uploadImage");
			result.addObject("negocioId", negocioId);
			result.addObject("hasimage",hasimage);
			
			
			return result;
		}
		
		
		@RequestMapping (value="/uploadImageNegocio", method=RequestMethod.POST, params="save")
		public ModelAndView addImagenNegocio(@RequestParam int negocioId, @RequestParam("foto") MultipartFile file){
			ModelAndView result;
			Negocio n = negocioService.findOneToEdit(negocioId);

			try{
				
				negocioService.addImageToNegocio(negocioId, file.getBytes());
				result = new ModelAndView("redirect:../../negocio/empresario/list.do");
				
			}catch(Throwable oops){
				boolean hasimage=true;

				if(n.getImagen()==null){
					hasimage=false;
				}
				
				result = createEditModelAndView(n, "uploadImage");
				result.addObject("negocioId", negocioId);
				result.addObject("hasimage", hasimage);
				result.addObject("message", "negocio.commit.error");
								
			}
			
			return result;
		}
					

		// Ancillary methods ---------------------------------------------------------
		
		protected ModelAndView createEditModelAndView(Negocio negocio, String selectView){
			ModelAndView result;
				
			result = createEditModelAndView(negocio, selectView, null);
				
			return result;
		}
			
		protected ModelAndView createEditModelAndView(Negocio negocio, String selectView, String message){
			ModelAndView result;
			Collection<Playa> playas = playaService.findAllBeaches();
			boolean hasimage = true;
			
			if(negocio.getImagen() == null){
				hasimage = false;
			}
			
			result = new ModelAndView("negocio/"+selectView);

			result.addObject("negocio", negocio);
			result.addObject("playas", playas);
			result.addObject("message", null);
			result.addObject("hasimage", hasimage);
			

			return result;
		}
			
}
