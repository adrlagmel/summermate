package controllers.empresario;

import java.util.Collection;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NegocioService;
import services.ValoracionNegocioService;

import controllers.AbstractController;
import domain.Negocio;
import domain.ValoracionNegocio;


@Controller
@RequestMapping("/valoracionNegocio/empresario")
public class ValoracionNegocioEmpresarioController extends AbstractController {

	
	@Autowired
	private NegocioService negocioService;
	
	@Autowired
	private ValoracionNegocioService valoracionNegocioService;

	public ValoracionNegocioEmpresarioController() {
		super();
	}
	
	
	@RequestMapping(value="/listValoraciones", method = RequestMethod.GET)
	public ModelAndView listValoraciones(@RequestParam int negocioId){
		
		ModelAndView result;
		Negocio negocio = negocioService.findOneToEdit(negocioId);
		Collection<ValoracionNegocio> vNegocios = null;
		
		vNegocios = valoracionNegocioService.valoracionesNegocioDeUnNegocio(negocioId);
		
		result = new ModelAndView("valoracionNegocio/list");

		result.addObject("vNegocios", vNegocios);
		result.addObject("requestURI", "valoracionesNegocio/empresario/listValoraciones.do?negocioId="+negocio.getId());

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

