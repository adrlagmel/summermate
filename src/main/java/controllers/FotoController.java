package controllers;

import java.io.IOException;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import services.EventoService;
import services.NegocioService;
import services.PlayaService;
import services.UsuarioService;
import domain.Imagen;

// Sirve las imágenes guardadas en la tabla imagen. Antes respondía siempre
// image/jpeg (fuese lo que fuese el fichero) y, si no había imagen o el id no
// existía, fallaba con un NullPointerException (error 500).
@Controller
@RequestMapping("/foto")
public class FotoController extends AbstractController {

	@Autowired
	private PlayaService	playaService;

	@Autowired
	private NegocioService	negocioService;

	@Autowired
	private EventoService	eventoService;

	@Autowired
	private UsuarioService	usuarioService;


	public FotoController() {
		super();
	}

	@RequestMapping(value = "/displayImage")
	public void displayImage(HttpServletResponse response, @RequestParam int playaId) throws IOException {
		write(response, () -> playaService.findOne(playaId).getImagen());
	}

	@RequestMapping(value = "/displayImageNegocio")
	public void displayImageNegocio(HttpServletResponse response, @RequestParam int negocioId) throws IOException {
		write(response, () -> negocioService.findOneToDisplay(negocioId).getImagen());
	}

	@RequestMapping(value = "/displayImageEvento")
	public void displayImageEvento(HttpServletResponse response, @RequestParam int eventoId) throws IOException {
		write(response, () -> eventoService.findOneToDisplay(eventoId).getImagen());
	}

	@RequestMapping(value = "/displayImagePerfil")
	public void displayImagePerfil(HttpServletResponse response, @RequestParam int usuarioId) throws IOException {
		write(response, () -> usuarioService.findOne(usuarioId).getImagen());
	}

	// Si la entidad no existe (los servicios lanzan IllegalArgumentException o
	// devuelven null) o no tiene imagen, responde 404.
	private void write(HttpServletResponse response, Supplier<Imagen> lookup) throws IOException {
		Imagen imagen;

		try {
			imagen = lookup.get();
		} catch (IllegalArgumentException | NullPointerException noExiste) {
			imagen = null;
		}

		if (imagen == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType(imagen.getTipo());
		response.setContentLength(imagen.getTamano());
		// Impide que el navegador "adivine" otro tipo distinto del declarado
		response.setHeader("X-Content-Type-Options", "nosniff");
		response.getOutputStream().write(imagen.getDatos());
	}

}
