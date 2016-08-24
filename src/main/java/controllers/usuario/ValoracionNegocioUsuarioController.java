package controllers.usuario;

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

import services.ReservaService;
import services.ValoracionNegocioService;

import controllers.AbstractController;
import domain.Reserva;
import domain.ValoracionNegocio;

@Controller
@RequestMapping("/valoracionNegocio/usuario")
public class ValoracionNegocioUsuarioController extends AbstractController {
	
	@Autowired
	private ReservaService reservaService;
	
	@Autowired
	private ValoracionNegocioService valoracionNegocioService;

	public ValoracionNegocioUsuarioController() {
		super();
	}
	
	@RequestMapping(value="/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int reservaId){
		Reserva reserva = reservaService.findOne(reservaId);
		
		ValoracionNegocio valoracionNegocio = valoracionNegocioService.findOne(reserva.getValoracionNegocio().getId());
		
		ModelAndView result = new ModelAndView("valoracionNegocio/display");
		result.addObject("valoracionNegocio", valoracionNegocio);
		
		return result;
	}
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(){
		
		ModelAndView result;
		Collection<ValoracionNegocio> vNegocios = null;
				
		vNegocios = valoracionNegocioService.valoracionesNegocioDeUsuario();
		
		result = new ModelAndView("valoracionNegocio/list");

		result.addObject("vNegocios", vNegocios);
		result.addObject("requestURI", "valoracionNegocio/usuario/list.do");

		return result;
		
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int reservaId) {
		ModelAndView result;
		
		Reserva r = reservaService.findOneToEdit(reservaId);
		ValoracionNegocio vn = valoracionNegocioService.create(r);
		
		result = createModelAndView(vn);
		result.addObject("reserva", r);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int valoracionNegocioId){
		ModelAndView result;
		ValoracionNegocio vNegocio = valoracionNegocioService.findOneToEdit(valoracionNegocioId);
		//vNegocio.setFecha(new Date (System.currentTimeMillis()-1000));
		
		result = createModelAndView(vNegocio);
		result.addObject("requestURI", "valoracionNegocio/usuario/edit.do?valoracionNegocioId="+vNegocio.getId());
		return result;
	}

	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute ValoracionNegocio vn, BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors()) {
			result = createModelAndView(vn);
		} else {
			try {
				valoracionNegocioService.save(vn);
				result = new ModelAndView("redirect:../../valoracionNegocio/usuario/list.do");
			} catch (Throwable oops) {
				result = createModelAndView(vn, "valoracionNegocio.commit.error");
			}
		}
		return result;
	}
	
	@RequestMapping(value= "/borrar", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int valoracionNegocioId){
		ModelAndView result;
		
		ValoracionNegocio vNegocio = valoracionNegocioService.findOne(valoracionNegocioId);
		//Assert.notNull(vNegocio);
		valoracionNegocioService.delete(vNegocio);
		
		result = new ModelAndView("redirect:list.do");

		return result;
		
	}
	
	protected ModelAndView createModelAndView(ValoracionNegocio vn) {
		ModelAndView result;

		result = createModelAndView(vn, null);

		return result;
	}

	protected ModelAndView createModelAndView(ValoracionNegocio vn, String message) {
		ModelAndView result;
		
		result = new ModelAndView("valoracionNegocio/edit");
		result.addObject("valoracionNegocio", vn);
		result.addObject("message", message);

		return result;
	}

}

