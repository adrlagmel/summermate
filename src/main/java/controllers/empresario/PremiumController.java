/* PremiumController.java
 *
 * Cuenta premium del empresario (Fase 6):
 *   GET  /empresario/premium                       estado, planes y pagos
 *   POST /empresario/premium?plan=                 contratar: lleva a la pasarela
 *   GET  /empresario/premium/pagos/{ref}           página de pago (pasarela simulada)
 *   POST /empresario/premium/pagos/{ref}           pagar (pasarela simulada)
 *   DELETE /empresario/premium/pagos/{ref}         cancelar el pago
 */

package controllers.empresario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.PagoPremium;
import services.EmpresarioService;
import services.PremiumService;

@Controller
@RequestMapping("/empresario/premium")
public class PremiumController {

	@Autowired
	private PremiumService		premiumService;

	@Autowired
	private EmpresarioService	empresarioService;


	@GetMapping
	public String estado(Model model) {
		model.addAttribute("empresario", empresarioService.findByPrincipal());
		model.addAttribute("planes", PagoPremium.Plan.values());
		model.addAttribute("pagos", premiumService.misPagos());

		return "empresario/premium";
	}

	@PostMapping
	public String contratar(@RequestParam PagoPremium.Plan plan) {
		return "redirect:" + premiumService.contratar(plan);
	}

	@GetMapping("/pagos/{referencia}")
	public String pasarela(@PathVariable String referencia, Model model) {
		model.addAttribute("pago", premiumService.pagoPropio(referencia));

		return "empresario/pasarela";
	}

	@PostMapping("/pagos/{referencia}")
	public String pagar(@PathVariable String referencia, RedirectAttributes redirect) {
		premiumService.confirmar(referencia);
		redirect.addFlashAttribute("aviso", "premium.pagado");
		return "redirect:/empresario/premium";
	}

	@DeleteMapping("/pagos/{referencia}")
	public String cancelar(@PathVariable String referencia, RedirectAttributes redirect) {
		premiumService.cancelar(referencia);
		redirect.addFlashAttribute("aviso", "premium.cancelado");
		return "redirect:/empresario/premium";
	}

}
