package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import domain.Playa;
import domain.Usuario;
import forms.ActorEditPasswordForm;
import forms.UsuarioRegistroForm;

import services.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController extends AbstractController{
	
	// Services -------------------------------------------------------------------
	
			@Autowired
			private UsuarioService usuarioService;
			
						
			// Constructors ---------------------------------------------------------------
			
			public UsuarioController(){
				super();
			}
			
			
			// Listing methods -----------------------------------------------------------
			
			
			
			// Create methods --------------------------------------------------------------
			
			
			@RequestMapping(value="/register", method = RequestMethod.GET)
			public ModelAndView create(){
				
				ModelAndView result;
				UsuarioRegistroForm form = new UsuarioRegistroForm();
				result = createEditModelAndView(form);
				
				return result;
				
			}
			
			@RequestMapping(value="/register", method=RequestMethod.POST, params="register")
			public ModelAndView save(@ModelAttribute("form") @Valid UsuarioRegistroForm form, BindingResult binding){
				
				ModelAndView result;
				Usuario usuario;
				
				if(binding.hasErrors()){
					
						result = createEditModelAndView(form);
				}else{
					try{
						usuario = usuarioService.reconstruct(form);
						usuarioService.save(usuario);
						result = new ModelAndView("redirect:/#");
						
					}catch(DataIntegrityViolationException exc){
						result = createEditModelAndView(form, "register.duplicated.user");
					}catch(Throwable oops){
						result = createEditModelAndView(form, "register.commit.error");
					}
				}
				
				return result;
			}
			
			@RequestMapping(value = "/edit", method = RequestMethod.GET)
			public ModelAndView edit() {
				
		    	ModelAndView result;
				
				Usuario us2 = usuarioService.findByPrincipal();
				Usuario us = usuarioService.findOne(us2.getId());
				
				Assert.notNull(us);
				result = createEditModelAndView2(us);
				result.addObject("updateProfile", true);
				
				result.addObject("actionURI", "usuario/edit.do");

				return result;
		    }
			
			
			// Ancillary methods ---------------------------------------------------------
			
			@RequestMapping(value="/uploadImageUsuario", method=RequestMethod.GET)
			public ModelAndView uploadImage(@RequestParam int usuarioId){
				ModelAndView result;
				Usuario usuario = usuarioService.findOne(usuarioId);
				
				boolean hasimage=true;

				if(usuario.getImagen()==null){
					hasimage=false;
				}
				
				result = createEditModelAndView3(usuario, "uploadImageUsuario");
				
				result.addObject("usuarioId", usuarioId);
				result.addObject("hasimage",hasimage);
				
				
				return result;
			}
			
			
			@RequestMapping (value="/uploadImageUsuario", method=RequestMethod.POST, params="save")
			public ModelAndView addImagenUsuario(@RequestParam int usuarioId, @RequestParam("foto") MultipartFile file){
				ModelAndView result;
				Usuario u = usuarioService.findOneToEdit(usuarioId);
				try{
					
					usuarioService.addImageToNegocio(usuarioId, file.getBytes());
					result = new ModelAndView("redirect:/#");
					
				}catch(Throwable oops){
					boolean hasimage=true;

					if(u.getImagen()==null){
						hasimage=false;
					}
					
					result = createEditModelAndView3(u,  "uploadImageUsuario");
					result.addObject("usuario", u);
					result.addObject("hasimage", hasimage);
					result.addObject("message", "usuario.commit.error");
									
				}
				
				return result;
			}
			
			
			
			// Ancillary methods ---------------------------------------------------------
			
				protected ModelAndView createEditModelAndView(UsuarioRegistroForm registroForm){
					
					ModelAndView result;
					
					result = createEditModelAndView(registroForm, null);
					
					return result;
				}
				
				protected ModelAndView createEditModelAndView(UsuarioRegistroForm registroForm, String message){
					
					ModelAndView result;
				
					result = new ModelAndView("usuario/register");
					result.addObject("form", registroForm);
					result.addObject("message", message);
					result.addObject("isUsuario", true);
					result.addObject("isAdministrador", false);
					result.addObject("actionURI", "usuario/register.do");

					return result;
				}
				
				protected ModelAndView createEditModelAndView2(Usuario usuario){
					
					ModelAndView result;
					
					result = createEditModelAndView2(usuario, null);
					
					return result;
				}
				
				protected ModelAndView createEditModelAndView2(Usuario usuario, String message){
					
					ModelAndView result;

					
					result = new ModelAndView("usuario/uploadImage");
					
					result.addObject("usuario", usuario);
					result.addObject("message", message);
					
					return result;
				}
				
				protected ModelAndView createEditModelAndView3(Usuario u, String selectView){
					
					ModelAndView result;
						
					result = createEditModelAndView3(u, selectView, null);
						
					return result;
				}
					
				protected ModelAndView createEditModelAndView3(Usuario u, String selectView, String message){

					ModelAndView result;
					boolean hasimage = true;
					
					if(u.getImagen() == null){
						hasimage = false;
					}
					
					result = new ModelAndView("usuario/"+selectView);

					result.addObject("usuario", u);
					result.addObject("message", null);
					result.addObject("hasimage", hasimage);

					return result;
				}	

	}
