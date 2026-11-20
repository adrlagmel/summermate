/* PerfilController.java
 *
 * Perfil del actor conectado. Rutas antiguas:
 *   /perfil/usuario.do, /perfil/empresario.do       -> GET /perfil
 *   /perfil/usuario/edit.do, /perfil/empresario/... -> GET /perfil/editar y PUT /perfil
 *   /perfil/actor/editPassword.do                   -> GET /perfil/contrasena y PUT /perfil/contrasena
 *   /usuario/uploadImageUsuario.do                  -> GET /perfil/foto y POST /perfil/foto
 *
 * Antes la foto se subía a /usuario/uploadImageUsuario.do?usuarioId=, con el
 * id en la URL; ahora siempre es la del usuario conectado.
 */

package controllers.actor;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.SubidaImagen;
import domain.Actor;
import domain.Empresario;
import domain.Usuario;
import forms.ActorEditPasswordForm;
import forms.EmpresarioEditForm;
import forms.UsuarioEditForm;
import security.Authority;
import services.ActorService;
import services.EmpresarioService;
import services.UsuarioService;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UsuarioService		usuarioService;

	@Autowired
	private EmpresarioService	empresarioService;

	@Autowired
	private PasswordEncoder		passwordEncoder;


	@GetMapping
	public String ver(Model model) {
		Actor actor = actorService.findByPrincipal();

		model.addAttribute("actor", actor);
		model.addAttribute("esCliente", actor instanceof domain.Cliente);
		model.addAttribute("esUsuario", actor instanceof Usuario);
		model.addAttribute("esEmpresario", actor instanceof Empresario);
		if (actor instanceof Usuario)
			model.addAttribute("colaboracion", usuarioService.colaboracion(actor.getId()));

		return "perfil/ver";
	}

	@GetMapping("/editar")
	public String editar(Authentication auth, Model model) {
		model.addAttribute("esUsuario", tiene(auth, Authority.USUARIO));
		if (tiene(auth, Authority.USUARIO))
			model.addAttribute("form", formulario(usuarioService.findByPrincipal()));
		else if (tiene(auth, Authority.EMPRESARIO))
			model.addAttribute("form", formulario(empresarioService.findByPrincipal()));
		else
			return "redirect:/perfil";

		return "perfil/editar";
	}

	// Un mismo PUT /perfil para usuarios y empresarios: cada uno se enlaza a su
	// formulario (el del otro rol tiene campos distintos).
	@PutMapping(params = "tipo=usuario")
	public String guardarUsuario(@ModelAttribute("form") @Validated UsuarioEditForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		model.addAttribute("esUsuario", true);
		if (binding.hasErrors())
			return "perfil/editar";
		try {
			usuarioService.saveEdit(usuarioService.reconstructEdit(form));
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("message", "usuario.duplicated.user");
			return "perfil/editar";
		}
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/perfil";
	}

	@PutMapping(params = "tipo=empresario")
	public String guardarEmpresario(@ModelAttribute("form") @Validated EmpresarioEditForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		model.addAttribute("esUsuario", false);
		if (binding.hasErrors())
			return "perfil/editar";
		try {
			empresarioService.saveEdit(empresarioService.reconstructEdit(form));
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("message", "empresario.duplicated.user");
			return "perfil/editar";
		}
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/perfil";
	}

	@GetMapping("/contrasena")
	public String contrasena(Model model) {
		model.addAttribute("form", new ActorEditPasswordForm());

		return "perfil/contrasena";
	}

	@PutMapping("/contrasena")
	public String cambiarContrasena(@ModelAttribute("form") @Validated ActorEditPasswordForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		Actor actor = actorService.findByPrincipal();

		if (!binding.hasFieldErrors("oldPassword") && !passwordEncoder.matches(form.getOldPassword(), actor.getUserAccount().getPassword()))
			binding.rejectValue("oldPassword", "actor.password.error");
		if (form.getPassword() != null && !form.getPassword().equals(form.getVerifyPassword()))
			binding.rejectValue("verifyPassword", "actor.password.error.coincidir");
		if (binding.hasErrors())
			return "perfil/contrasena";

		actorService.savePassword(actorService.reconstructPassword(form));
		redirect.addFlashAttribute("aviso", "fase5.contrasena.ok");
		return "redirect:/perfil";
	}

	@GetMapping("/foto")
	public String foto(Model model) {
		return vistaFoto(model, usuarioService.findByPrincipal());
	}

	@PostMapping("/foto")
	public String subirFoto(@RequestParam("foto") MultipartFile foto, Model model, RedirectAttributes redirect) throws IOException {
		Usuario usuario = usuarioService.findByPrincipal();

		try {
			usuarioService.addImageToUsuario(usuario.getId(), foto.getBytes());
		} catch (IllegalArgumentException formatoNoAdmitido) {
			model.addAttribute("message", "fase5.imagen.formato");
			return vistaFoto(model, usuario);
		}
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/perfil";
	}

	private static String vistaFoto(Model model, Usuario usuario) {
		model.addAttribute("imagen", new SubidaImagen(usuario.getNombre() + " " + usuario.getApellidos(), "/perfil/foto", "usuarios", usuario.getId(), usuario.getImagen() != null, "/perfil"));

		return SubidaImagen.VISTA;
	}

	private static UsuarioEditForm formulario(Usuario u) {
		UsuarioEditForm form = new UsuarioEditForm();

		form.setNombre(u.getNombre());
		form.setApellidos(u.getApellidos());
		form.setEmail(u.getEmail());
		form.setTelefono(u.getTelefono());
		form.setFechaNacimiento(u.getFechaNacimiento());
		form.setNacionalidad(u.getNacionalidad());
		form.setDireccion(u.getDireccion());
		form.setSexo(u.getSexo());
		form.setEstadoActual(u.getEstadoActual());

		return form;
	}

	private static EmpresarioEditForm formulario(Empresario e) {
		EmpresarioEditForm form = new EmpresarioEditForm();

		form.setNombre(e.getNombre());
		form.setApellidos(e.getApellidos());
		form.setEmail(e.getEmail());
		form.setTelefono(e.getTelefono());
		form.setFechaNacimiento(e.getFechaNacimiento());
		form.setNacionalidad(e.getNacionalidad());
		form.setDireccion(e.getDireccion());
		form.setSexo(e.getSexo());
		form.setCif(e.getCif());

		return form;
	}

	private static boolean tiene(Authentication auth, String rol) {
		return auth.getAuthorities().stream().anyMatch(a -> rol.equals(a.getAuthority()));
	}

}
