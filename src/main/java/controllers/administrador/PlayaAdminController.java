package controllers.administrador;

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

import services.PlayaService;
import controllers.AbstractController;
import controllers.PaginaDisplaytag;
import controllers.Paginacion;
import domain.Playa;
import forms.PlayaForm;

@Controller
@RequestMapping("/playa/admin")
public class PlayaAdminController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	private PlayaService playaService;
	
	// Constructors -----------------------------------------------------------

	public PlayaAdminController() {
		super();
	}

	// Index ------------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(Paginacion paginacion){

		ModelAndView result;
		PaginaDisplaytag<Playa> playas = new PaginaDisplaytag<>(playaService.findAll(paginacion.playas()));

		result = new ModelAndView("playa/list");
		result.addObject("playas", playas);
		result.addObject("puntosMapa", playaService.findPuntosMapa());
		result.addObject("requestURI", "playa/admin/list.do");
		return result;
	}
	
	
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create(){
							
		ModelAndView result;
					
			Playa playa = playaService.create();
			
			result =  createEditModelAndView(new PlayaForm(playa), "create");
			
			return result;
	}
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int playaId){
							
			ModelAndView result;
			
			Playa playa 	 = playaService.findOne(playaId);
			
			result = createEditModelAndView(playa, "display");
	
			return result;
					
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int playaId){
							
			ModelAndView result;
			
			Playa playa = playaService.findOneToEdit(playaId);
			
			result = createEditModelAndView(new PlayaForm(playa), "edit");
			
			return result;
					
	}
	
	@RequestMapping(value="/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("playa") @Valid PlayaForm form, BindingResult binding){
		
	ModelAndView result;
	String view = form.isNew() ? "create" : "edit";

		if(binding.hasErrors()){
			result = createEditModelAndView(form, view);
			
		}else{
			try{
				playaService.save(playaService.reconstruct(form));
				result = new ModelAndView("redirect:../list.do");
				
			}catch(Throwable oops){
				
				result = createEditModelAndView(form, view, "playa.commit.error");
			}		
		}
			
	return result;
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam int playaId){
		
		ModelAndView result;
		
		Playa dv = playaService.findOneToEdit(playaId);
		Assert.notNull(dv);
	
		playaService.delete(dv);
		result = new ModelAndView("redirect:../list.do");

		return result;
		
	}

	// Ancillary methods ---------------------------------------------------------
			@RequestMapping(value="/uploadImage", method=RequestMethod.GET)
			public ModelAndView uploadImage (@RequestParam int playaId){
				ModelAndView result;
				Playa p = playaService.findOne(playaId);
				
				boolean hasimage=true;

				if(p.getImagen()==null){
					hasimage=false;
				}
				
				result = createEditModelAndView(p, "uploadImage");
				result.addObject("playaId", playaId);
				result.addObject("hasimage",hasimage);
				
				
				return result;
			}
			
	
			  @RequestMapping(value="/uploadCoordenates", method = RequestMethod.GET)
				public ModelAndView updateCoordenates(@RequestParam(required=false) Double lat, @RequestParam(required=false) Double lon){
					
					ModelAndView result;
					Boolean mostrarMapa;
					mostrarMapa=true;
					Collection<Playa> playas;
					
					if(lat==null || lon==null){
						mostrarMapa=false;
						result = new ModelAndView("playa/uploadCoordenates");
						result.addObject("mostrarMapa", mostrarMapa);
						
					}else{
						
						playas = playaService.nearToMe(lat,lon);
						result = new ModelAndView("playa/uploadCoordenates");
						result.addObject("playas", playas);
						result.addObject("lat1",lat);
						result.addObject("lon1",lon);
						result.addObject("requestURI", "playa/admin/uploadCoordenates.do");
						result.addObject("tabla",true);
					}
					
					return result;	
				}
			
			
			@RequestMapping (value="/uploadImage", method=RequestMethod.POST, params="save")
			public ModelAndView addFotoIncidence(@RequestParam int playaId, @RequestParam("foto") MultipartFile file){
				ModelAndView result;
				Playa p = playaService.findOne(playaId);

				try{
					
					playaService.addImageToFoto(playaId, file.getBytes());
					result = new ModelAndView("redirect:../list.do");
					
				}catch(Throwable oops){
					boolean hasimage=true;

					if(p.getImagen()==null){
						hasimage=false;
					}
					
					result = createEditModelAndView(p, "uploadImage");
					result.addObject("playaId", playaId);
					result.addObject("hasimage", hasimage);
					result.addObject("message", "playa.commit.error");
									
				}
				
				return result;
			}
			

	
	// Formularios de alta y edición: trabajan con PlayaForm, no con la entidad.
	protected ModelAndView createEditModelAndView(PlayaForm form, String selectView){
		return createEditModelAndView(form, selectView, null);
	}

	protected ModelAndView createEditModelAndView(PlayaForm form, String selectView, String message){
		ModelAndView result;
		boolean hasimage = !form.isNew() && playaService.findOne(form.getId()).getImagen() != null;

		result = new ModelAndView("playa/"+selectView);
		result.addObject("playa", form);
		result.addObject("message", message);
		result.addObject("hasimage", hasimage);

		return result;
	}

	protected ModelAndView createEditModelAndView(Playa playa, String selectView){
		
		ModelAndView result;
			
		result = createEditModelAndView(playa, selectView, null);
			
		return result;
	}
		
	protected ModelAndView createEditModelAndView(Playa playa, String selectView, String message){

		ModelAndView result;
		boolean hasimage = true;
		
		if(playa.getImagen() == null){
			hasimage = false;
		}
		
		result = new ModelAndView("playa/"+selectView);

		result.addObject("playa", playa);
		result.addObject("message", null);
		result.addObject("hasimage", hasimage);

		return result;
	}	
	
}
