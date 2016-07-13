package controllers.usuario;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int reservaId) {
		ModelAndView result;
		ValoracionNegocio vn;
		Reserva r;
		

		r = reservaService.findOneToEdit(reservaId);
		vn = valoracionNegocioService.create(r);
		
		result = createModelAndView(vn);
		result.addObject("reserva", r);
		

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
				result = new ModelAndView("redirect:../../reserva/usuario/lista.do");
			} catch (Throwable oops) {
				result = createModelAndView(vn, "valoracionNegocio.commit.error");
			}
		}
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

