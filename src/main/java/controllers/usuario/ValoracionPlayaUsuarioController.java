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

import services.PlayaService;
import services.ValoracionPlayaService;

import controllers.AbstractController;
import domain.Playa;
import domain.ValoracionPlaya;



@Controller
@RequestMapping("/valoracionPlaya/usuario")
public class ValoracionPlayaUsuarioController extends AbstractController {

	
	
	@Autowired
	private PlayaService playaService;
	
	@Autowired
	private ValoracionPlayaService valoracionPlayaService;

	public ValoracionPlayaUsuarioController() {
		super();
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(){
		
		ModelAndView result;
		Collection<ValoracionPlaya> vPlayas = null;
				
		vPlayas = valoracionPlayaService.valoracionesDeUsuario();
		
		result = new ModelAndView("valoracionPlaya/list");

		result.addObject("vPlayas", vPlayas);
		result.addObject("requestURI", "valoracionPlaya/usuario/list.do");

		return result;
		
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int playaId) {
		ModelAndView result;
		ValoracionPlaya vp;
		Playa p;

		p = playaService.findOneToEdit(playaId);
		vp = valoracionPlayaService.create(p);
		
		result = createModelAndView(vp);
		result.addObject("playa", p);
		

		return result;
	}

	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int valoracionPlayaId){
		ModelAndView result;
		ValoracionPlaya vPlaya = valoracionPlayaService.findOneToEdit(valoracionPlayaId);
		//vPlaya.setFecha(new Date (System.currentTimeMillis()-1000));
		
		Assert.notNull(vPlaya);
		result = createModelAndView(vPlaya);
		result.addObject("requestURI", "valoracionPlaya/usuario/edit.do?valoracionPlayaId="+vPlaya.getId());
		return result;
	}

	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute ValoracionPlaya vp, BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors()) {
			result = createModelAndView(vp);
		} else {
			try {
				valoracionPlayaService.save(vp);
				result = new ModelAndView("redirect:../../valoracionPlaya/usuario/list.do");
			} catch (Throwable oops) {
				result = createModelAndView(vp, "valoracionPlaya.commit.error");
			}
		}
		return result;
	}
	
	@RequestMapping(value="/borrar", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int valoracionPlayaId){
		
		ModelAndView result;
		
		ValoracionPlaya vPlaya = valoracionPlayaService.findOne(valoracionPlayaId);
		Assert.notNull(vPlaya);
	
		valoracionPlayaService.delete(vPlaya);
		result = new ModelAndView("redirect:../../valoracionPlaya/usuario/list.do");

		return result;
		
	}
	
	protected ModelAndView createModelAndView(ValoracionPlaya vp) {
		ModelAndView result;

		result = createModelAndView(vp, null);

		return result;
	}

	protected ModelAndView createModelAndView(ValoracionPlaya vp, String message) {
		ModelAndView result;
		
		result = new ModelAndView("valoracionPlaya/edit");
		result.addObject("valoracionPlaya", vp);
		result.addObject("message", message);

		return result;
	}

}

