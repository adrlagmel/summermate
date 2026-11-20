/* AdminController.java
 *
 * Zona del administrador. Rutas antiguas:
 *   /administrador/dashboard.do                      -> GET /admin/dashboard
 *   /playa/admin/list.do                             -> GET /playas (la lista pública muestra las acciones)
 *   /playa/admin/create.do                           -> GET /admin/playas/nueva y POST /admin/playas
 *   /playa/admin/edit.do?playaId=                    -> GET /admin/playas/{id}/editar y PUT /admin/playas/{id}
 *   /playa/admin/delete.do                           -> DELETE /admin/playas/{id}
 *   /playa/admin/uploadImage.do                      -> GET y POST /admin/playas/{id}/imagen
 *   /playa/markMap.do, /playa/admin/uploadCoordenates.do,
 *   /playa/coordenates/save.do (¡era un GET!)        -> GET /admin/playas/{id}/ubicacion y POST /admin/playas/{id}/ubicacion
 *   /peticionNegocio/administrador/listapendiente.do -> GET /admin/peticiones
 *   /peticionNegocio/administrador/aceptar.do        -> GET /admin/peticiones/{id}/aceptacion y POST /admin/peticiones/{id}/aceptacion
 *   /peticionNegocio/administrador/rechazar.do       -> GET /admin/peticiones/{id}/rechazo y POST /admin/peticiones/{id}/rechazo
 *   /administrador/register.do                       -> GET /admin/administradores/nuevo y POST /admin/administradores
 *
 * Ubicación de una playa: guardar coordenadas cambiaba datos con un GET (sin
 * CSRF); ahora es un POST con el token, y se comprueba que la playa es del
 * administrador conectado. Se mantiene la regla de que solo se puede mover
 * una playa que no tenga negocios (no es un cambio de modelo; está pendiente
 * de decidir tras revisar la memoria).
 */

package controllers.administrador;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import controllers.Paginacion;
import controllers.SubidaImagen;
import domain.PeticionNegocio;
import domain.Playa;
import forms.AdministradorRegistroForm;
import forms.PeticionNegocioResolucionForm;
import forms.PlayaCoordinatesForm;
import forms.PlayaForm;
import services.AdministradorService;
import services.NegocioService;
import services.PeticionNegocioService;
import services.PlayaService;
import services.UsuarioService;
import utilities.Assert;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private PlayaService			playaService;

	@Autowired
	private NegocioService			negocioService;

	@Autowired
	private UsuarioService			usuarioService;

	@Autowired
	private PeticionNegocioService	peticionService;

	@Autowired
	private AdministradorService	administradorService;

	@Autowired
	private services.DenunciaValoracionService	denunciaService;

	@Autowired
	private services.ValoracionNegocioService	valoracionNegocioService;

	@Autowired
	private SmartValidator			validator;


	// Panel de control ------------------------------------------------------

	@GetMapping("/dashboard")
	public String dashboard(Paginacion paginacion, Model model) {
		model.addAttribute("playaMejorValorada", playaService.playaMejorValorada());
		model.addAttribute("playaConMasValoraciones", playaService.findPlayaMasComentarios());
		model.addAttribute("masValoraciones", playaService.masValoraciones());
		model.addAttribute("negocioMejorValorado", negocioService.findNegocioMejorValorado());
		model.addAttribute("negocioMasReservas", negocioService.negocioConMasReservas());
		model.addAttribute("usuarios", usuarioService.usuariosTotales(paginacion.actores()));

		return "admin/dashboard";
	}

	// Moderación (Fase 6) ---------------------------------------------------

	@GetMapping("/negocios")
	public String negocios(Paginacion paginacion, Model model) {
		model.addAttribute("negocios", negocioService.findAll(paginacion.negocios()));

		return "admin/negocios";
	}

	@PostMapping("/negocios/{id}/suspension")
	public String suspenderNegocio(@PathVariable int id, RedirectAttributes redirect) {
		negocioService.moderar(negocioService.findOneToDisplay(id), false);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/admin/negocios";
	}

	@DeleteMapping("/negocios/{id}/suspension")
	public String reactivarNegocio(@PathVariable int id, RedirectAttributes redirect) {
		negocioService.moderar(negocioService.findOneToDisplay(id), true);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/admin/negocios";
	}

	@GetMapping("/denuncias")
	public String denuncias(Model model) {
		model.addAttribute("denuncias", denunciaService.findAllParaModerar());

		return "admin/denuncias";
	}

	// Descarta la denuncia y deja la valoración
	@DeleteMapping("/denuncias/{id}")
	public String descartarDenuncia(@PathVariable int id, RedirectAttributes redirect) {
		denunciaService.descartar(denunciaService.findOne(id));
		redirect.addFlashAttribute("aviso", "denuncia.descartada");
		return "redirect:/admin/denuncias";
	}

	// Retira la valoración denunciada (y con ella sus denuncias)
	@DeleteMapping("/valoraciones/negocios/{id}")
	public String retirarValoracion(@PathVariable int id, RedirectAttributes redirect) {
		valoracionNegocioService.retirarPorModeracion(valoracionNegocioService.findOne(id));
		redirect.addFlashAttribute("aviso", "denuncia.valoracionRetirada");
		return "redirect:/admin/denuncias";
	}

	// Playas ----------------------------------------------------------------

	@GetMapping("/playas/nueva")
	public String nuevaPlaya(Model model) {
		return formularioPlaya(model, new PlayaForm(playaService.create()));
	}

	@PostMapping("/playas")
	public String crearPlaya(@ModelAttribute("form") PlayaForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		form.setId(0);
		return guardarPlaya(form, binding, model, redirect);
	}

	@GetMapping("/playas/{id}/editar")
	public String editarPlaya(@PathVariable int id, Model model) {
		return formularioPlaya(model, new PlayaForm(playaPropia(id)));
	}

	@PutMapping("/playas/{id}")
	public String actualizarPlaya(@PathVariable int id, @ModelAttribute("form") PlayaForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		Playa actual = playaPropia(id);

		form.setId(id);
		// Las coordenadas se cambian en su propia pantalla
		if (form.getLocalizacion() != null) {
			form.getLocalizacion().setLatitud(actual.getLocalizacion().getLatitud());
			form.getLocalizacion().setLongitud(actual.getLocalizacion().getLongitud());
		}
		return guardarPlaya(form, binding, model, redirect);
	}

	@DeleteMapping("/playas/{id}")
	public String borrarPlaya(@PathVariable int id, RedirectAttributes redirect) {
		playaService.delete(playaPropia(id));
		redirect.addFlashAttribute("aviso", "fase5.borrado");

		return "redirect:/playas";
	}

	@GetMapping("/playas/{id}/imagen")
	public String imagenPlaya(@PathVariable int id, Model model) {
		return vistaImagen(model, playaPropia(id));
	}

	@PostMapping("/playas/{id}/imagen")
	public String subirImagenPlaya(@PathVariable int id, @RequestParam("foto") MultipartFile foto, Model model, RedirectAttributes redirect) throws IOException {
		Playa playa = playaPropia(id);

		try {
			playaService.addImageToFoto(id, foto.getBytes());
		} catch (IllegalArgumentException formatoNoAdmitido) {
			model.addAttribute("message", "fase5.imagen.formato");
			return vistaImagen(model, playa);
		}
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/playas/" + id;
	}

	@GetMapping("/playas/{id}/ubicacion")
	public String ubicacion(@PathVariable int id, Model model) {
		Playa playa = playaMovible(id);
		PlayaCoordinatesForm form = new PlayaCoordinatesForm();

		form.setLatitud(playa.getLocalizacion().getLatitud());
		form.setLongitud(playa.getLocalizacion().getLongitud());
		return vistaUbicacion(model, playa, form);
	}

	@PostMapping("/playas/{id}/ubicacion")
	public String guardarUbicacion(@PathVariable int id, @ModelAttribute("form") PlayaCoordinatesForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		Playa playa = playaMovible(id);

		form.setPlaya(playa);
		validator.validate(form, binding);
		if (binding.hasErrors())
			return vistaUbicacion(model, playa, form);

		playa.getLocalizacion().setLatitud(form.getLatitud());
		playa.getLocalizacion().setLongitud(form.getLongitud());
		playaService.save(playa);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/playas/" + id;
	}

	private String guardarPlaya(PlayaForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		validator.validate(form, binding);
		if (binding.hasErrors())
			return formularioPlaya(model, form);

		playaService.save(playaService.reconstruct(form));
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/playas";
	}

	private Playa playaPropia(int id) {
		Playa playa = playaService.findOne(id);

		playaService.checkPrincipal(playa);
		return playa;
	}

	// Solo se puede mover una playa sin negocios (regla de la aplicación original)
	private Playa playaMovible(int id) {
		Playa playa = playaPropia(id);

		Assert.isTrue(playa.getNegocios().isEmpty());
		return playa;
	}

	private static String formularioPlaya(Model model, PlayaForm form) {
		model.addAttribute("form", form);

		return "admin/playa";
	}

	private static String vistaUbicacion(Model model, Playa playa, PlayaCoordinatesForm form) {
		model.addAttribute("playa", playa);
		model.addAttribute("form", form);
		model.addAttribute("puntos", List.of());
		model.addAttribute("centro", Map.of("lat", form.getLatitud() == null ? playa.getLocalizacion().getLatitud() : form.getLatitud(),
			"lng", form.getLongitud() == null ? playa.getLocalizacion().getLongitud() : form.getLongitud()));
		model.addAttribute("mapaEditable", true);

		return "admin/ubicacion";
	}

	private static String vistaImagen(Model model, Playa playa) {
		model.addAttribute("imagen", new SubidaImagen(playa.getNombre(), "/admin/playas/" + playa.getId() + "/imagen", "playas", playa.getId(), playa.getImagen() != null, "/playas/" + playa.getId()));

		return SubidaImagen.VISTA;
	}

	// Peticiones de negocio -------------------------------------------------

	@GetMapping("/peticiones")
	public String peticiones(Model model) {
		model.addAttribute("peticiones", peticionService.findPeticionNegocioPendiente());

		return "admin/peticiones";
	}

	@GetMapping("/peticiones/{id}/{decision:aceptacion|rechazo}")
	public String resolver(@PathVariable int id, @PathVariable String decision, Model model) {
		return vistaResolucion(model, new PeticionNegocioResolucionForm(peticionService.findOneToEdit(id)), decision);
	}

	@PostMapping("/peticiones/{id}/{decision:aceptacion|rechazo}")
	public String guardarResolucion(@PathVariable int id, @PathVariable String decision, @ModelAttribute("form") PeticionNegocioResolucionForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		form.setId(id);
		validator.validate(form, binding);
		if (binding.hasErrors())
			return vistaResolucion(model, form, decision);

		PeticionNegocio peticion = peticionService.reconstruct(form);
		if ("aceptacion".equals(decision))
			peticionService.accept(peticion);
		else
			peticionService.reject(peticion);
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/admin/peticiones";
	}

	private String vistaResolucion(Model model, PeticionNegocioResolucionForm form, String decision) {
		model.addAttribute("form", form);
		model.addAttribute("peticion", peticionService.findOneToEdit(form.getId()));
		model.addAttribute("decision", decision);

		return "admin/resolucion";
	}

	// Alta de administradores ------------------------------------------------

	@GetMapping("/administradores/nuevo")
	public String nuevoAdministrador(Model model) {
		model.addAttribute("form", new AdministradorRegistroForm());

		return "admin/administrador";
	}

	@PostMapping("/administradores")
	public String crearAdministrador(@ModelAttribute("form") @Validated AdministradorRegistroForm form, BindingResult binding, Model model, RedirectAttributes redirect) {
		if (form.getPassword() != null && !form.getPassword().equals(form.getVerifyPassword()))
			binding.rejectValue("verifyPassword", "actor.password.error.coincidir");
		if (binding.hasErrors())
			return "admin/administrador";

		try {
			administradorService.save(administradorService.reconstruct(form));
		} catch (DataIntegrityViolationException duplicado) {
			model.addAttribute("message", "register.duplicated.user");
			return "admin/administrador";
		}
		redirect.addFlashAttribute("aviso", "fase5.guardado");
		return "redirect:/admin/dashboard";
	}

}
