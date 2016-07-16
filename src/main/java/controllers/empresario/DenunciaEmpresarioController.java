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
import org.springframework.web.servlet.ModelAndView;

import services.DenunciaValoracionService;
import services.ReservaService;
import services.ValoracionNegocioService;

import controllers.AbstractController;
import domain.DenunciaValoracion;
import domain.Reserva;
import domain.ValoracionNegocio;
import domain.ValoracionPlaya;


@Controller
@RequestMapping("/denunciaValoracion/empresario")
public class DenunciaEmpresarioController extends AbstractController {

	
	@Autowired
	private DenunciaValoracionService denunciaValoracionService;

	public DenunciaEmpresarioController() {
		super();
	}
	
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(){
		
		ModelAndView result;
		Collection<DenunciaValoracion> vDenuncias = null;
				
		vDenuncias = denunciaValoracionService.denunciasDelEmpresario();
		
		result = new ModelAndView("DenunciaValoracion/list");

		result.addObject("vDenuncias", vDenuncias);
		result.addObject("requestURI", "denunciaValoracion/empresario/list.do");

		return result;
		
	}
//	
//	@RequestMapping(value = "/create", method = RequestMethod.GET)
//	public ModelAndView create(@RequestParam int reservaId) {
//		ModelAndView result;
//		ValoracionNegocio vn;
//		Reserva r;
//		
//
//		r = reservaService.findOneToEdit(reservaId);
//		vn = valoracionNegocioService.create(r);
//		
//		result = createModelAndView(vn);
//		result.addObject("reserva", r);
//		
//
//		return result;
//	}
//
//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
//	public ModelAndView edit(@RequestParam int valoracionNegocioId){
//		ModelAndView result;
//		ValoracionNegocio vNegocio = valoracionNegocioService.findOneToEdit(valoracionNegocioId);
//		//vNegocio.setFecha(new Date (System.currentTimeMillis()-1000));
//		
//		Assert.notNull(vNegocio);
//		result = createModelAndView(vNegocio);
//		result.addObject("requestURI", "valoracionNegocio/usuario/edit.do?valoracionNegocioId="+vNegocio.getId());
//		return result;
//	}
//
//	
//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
//	public ModelAndView save(@Valid @ModelAttribute ValoracionNegocio vn, BindingResult bindingResult) {
//		ModelAndView result;
//
//		if (bindingResult.hasErrors()) {
//			result = createModelAndView(vn);
//		} else {
//			try {
//				valoracionNegocioService.save(vn);
//				result = new ModelAndView("redirect:../../valoracionNegocio/usuario/list.do");
//			} catch (Throwable oops) {
//				result = createModelAndView(vn, "valoracionNegocio.commit.error");
//			}
//		}
//		return result;
//	}
//	
//	@RequestMapping(value="/borrar", method = RequestMethod.GET)
//	public ModelAndView delete(@RequestParam int valoracionNegocioId){
//		
//		ModelAndView result;
//		
//		ValoracionNegocio vNegocio = valoracionNegocioService.findOne(valoracionNegocioId);
//		Assert.notNull(vNegocio);
//	
//		valoracionNegocioService.delete(vNegocio);
//		result = new ModelAndView("redirect:../../valoracionNegocio/usuario/list.do");
//
//		return result;
//		
//	}
//	
	protected ModelAndView createModelAndView(DenunciaValoracion dv) {
		ModelAndView result;

		result = createModelAndView(dv, null);

		return result;
	}

	protected ModelAndView createModelAndView(DenunciaValoracion dv, String message) {
		ModelAndView result;
		
		result = new ModelAndView("denunciaValoracion/edit");
		result.addObject("DenunciaValoracion", dv);
		result.addObject("message", message);

		return result;
	}

}

