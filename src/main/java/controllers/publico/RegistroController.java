/* RegistroController.java
 *
 * Alta de usuarios y empresarios (antes /usuario/register.do y
 * /empresario/register.do). Solo para visitantes sin sesión.
 *
 * Fase 5: además de lo que ya validaban los formularios, se comprueba que la
 * contraseña y su repetición coinciden (antes se aceptaban aunque no).
 */

package controllers.publico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import forms.EmpresarioRegistroForm;
import forms.RegistroForm;
import forms.UsuarioRegistroForm;
import services.EmpresarioService;
import services.UsuarioService;

@Controller
@RequestMapping("/registro")
public class RegistroController {

	@Autowired
	private UsuarioService		usuarioService;

	@Autowired
	private EmpresarioService	empresarioService;


	@GetMapping("/usuario")
	public String usuario(Model model) {
		UsuarioRegistroForm form = new UsuarioRegistroForm();
		form.setRegistroForm(new RegistroForm());

		return vista(model, form, "usuario");
	}

	@PostMapping("/usuario")
	public String registrarUsuario(@ModelAttribute("form") @Validated UsuarioRegistroForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		comprobarContrasenas(form.getRegistroForm(), binding);
		if (binding.hasErrors())
			return vista(model, form, "usuario");

		try {
			usuarioService.save(usuarioService.reconstruct(form));
		} catch (DataIntegrityViolationException duplicado) {
			model.addAttribute("message", "register.duplicated.user");
			return vista(model, form, "usuario");
		}
		redirect.addFlashAttribute("aviso", "fase5.registro.ok");
		return "redirect:/login";
	}

	@GetMapping("/empresario")
	public String empresario(Model model) {
		EmpresarioRegistroForm form = new EmpresarioRegistroForm();
		form.setRegistroForm(new RegistroForm());

		return vista(model, form, "empresario");
	}

	@PostMapping("/empresario")
	public String registrarEmpresario(@ModelAttribute("form") @Validated EmpresarioRegistroForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		comprobarContrasenas(form.getRegistroForm(), binding);
		if (binding.hasErrors())
			return vista(model, form, "empresario");

		try {
			empresarioService.save(empresarioService.reconstruct(form));
		} catch (DataIntegrityViolationException duplicado) {
			model.addAttribute("message", "register.duplicated.user");
			return vista(model, form, "empresario");
		}
		redirect.addFlashAttribute("aviso", "fase5.registro.ok");
		return "redirect:/login";
	}

	static void comprobarContrasenas(RegistroForm form, BindingResult binding) {
		if (form != null && form.getPassword() != null && !form.getPassword().equals(form.getVerifyPassword()))
			binding.rejectValue("registroForm.verifyPassword", "actor.password.error.coincidir");
	}

	private static String vista(Model model, Object form, String tipo) {
		model.addAttribute("form", form);
		model.addAttribute("tipo", tipo);

		return "registro";
	}

}
