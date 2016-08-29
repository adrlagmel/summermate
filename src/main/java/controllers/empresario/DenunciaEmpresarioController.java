package controllers.empresario;

import java.util.ArrayList;
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
import services.ValoracionNegocioService;

import controllers.AbstractController;
import domain.DenunciaValoracion;
import domain.ValoracionNegocio;


@Controller
@RequestMapping("/denunciaValoracion/empresario")
public class DenunciaEmpresarioController extends AbstractController {

	
	@Autowired
	private DenunciaValoracionService denunciaValoracionService;
	
	@Autowired
	private ValoracionNegocioService valoracionNegocioService;

	public DenunciaEmpresarioController() {
		super();
	}
	
	
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public ModelAndView list(){
		
		ModelAndView result;
		Collection<DenunciaValoracion> vDenuncias = null;
				
		vDenuncias = denunciaValoracionService.denunciasDelEmpresario();
		
		result = new ModelAndView("denunciaValoracion/list");

		result.addObject("vDenuncias", vDenuncias);
		result.addObject("requestURI", "denunciaValoracion/empresario/list.do");

		return result;
		
	}
	@RequestMapping(value="/listDenuncias", method = RequestMethod.GET)
	public ModelAndView listDenunciasValoracion(@RequestParam int valoracionNegocioId){
		
		ModelAndView result;
		Collection<DenunciaValoracion> vDenuncias = null;
				
		vDenuncias = denunciaValoracionService.denunciasDeUnaValoracion(valoracionNegocioId);
		
		result = new ModelAndView("denunciaValoracion/list");

		result.addObject("vDenuncias", vDenuncias);
		result.addObject("requestURI", "denunciaValoracion/empresario/listDenuncias.do");

		return result;
		
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int valoracionNegocioId) {
		ModelAndView result;
		DenunciaValoracion dv;
		ValoracionNegocio vn;
		

		vn = valoracionNegocioService.findOneToEdit(valoracionNegocioId);
		dv = denunciaValoracionService.create(vn);
		
		result = createModelAndView(dv);
		result.addObject("reserva", vn);
		

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int denunciaValoracionId){
		ModelAndView result;
		DenunciaValoracion dv = denunciaValoracionService.findOneToEdit(denunciaValoracionId);
		//vNegocio.setFecha(new Date (System.currentTimeMillis()-1000));
		
		Assert.notNull(dv);
		result = createModelAndView(dv);
		result.addObject("requestURI", "denunciaValoracion/empresario/edit.do?denunciaValoracionId="+dv.getId());
		return result;
	}

	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute DenunciaValoracion dv, BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors()) {
			result = createModelAndView(dv);
		} else {
			try {
				denunciaValoracionService.save(dv);
				result = new ModelAndView("redirect:../../denunciaValoracion/empresario/list.do");
			} catch (Throwable oops) {
				result = createModelAndView(dv, "denunciaValoracion.commit.error");
			}
		}
		return result;
	}
	
	@RequestMapping(value="/borrar", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int denunciaValoracionId){
		
		ModelAndView result;
		
		DenunciaValoracion dv = denunciaValoracionService.findOne(denunciaValoracionId);
		Assert.notNull(dv);
	
		denunciaValoracionService.delete(dv);
		result = new ModelAndView("redirect:../../denunciaValoracion/empresario/list.do");

		return result;
		
	}
	
	protected ModelAndView createModelAndView(DenunciaValoracion dv) {
		ModelAndView result;

		result = createModelAndView(dv, null);

		return result;
	}

	protected ModelAndView createModelAndView(DenunciaValoracion dv, String message) {
		ModelAndView result;
		
		ArrayList<String> tiposDenuncia = new ArrayList<String>();
		tiposDenuncia.add("FALSEDAD"); 
		tiposDenuncia.add("RECHAZO");
		tiposDenuncia.add("BURLA");
		
		result = new ModelAndView("denunciaValoracion/edit");
		result.addObject("tiposDenuncia", tiposDenuncia);
		result.addObject("denunciaValoracion", dv);
		result.addObject("message", message);

		return result;
	}

}

