/* ImagenesController.java
 *
 * Sirve las imágenes guardadas en la tabla imagen (antes FotoController):
 *   /foto/displayImage.do?playaId=          -> GET /imagenes/playas/{id}
 *   /foto/displayImageNegocio.do?negocioId= -> GET /imagenes/negocios/{id}
 *   /foto/displayImageEvento.do?eventoId=   -> GET /imagenes/eventos/{id}
 *   /foto/displayImagePerfil.do?usuarioId=  -> GET /imagenes/usuarios/{id}
 *
 * Se conserva lo de las Fases 2 y 3: tipo MIME real, nosniff, 404 si no hay
 * imagen y caché con ETag (el id de la imagen cambia al sustituirla, así que
 * si el navegador ya la tiene se responde 304 sin leer los bytes).
 */

package controllers.publico;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Imagen;
import services.EventoService;
import services.NegocioService;
import services.PlayaService;
import services.UsuarioService;

@Controller
@RequestMapping("/imagenes")
public class ImagenesController {

	@Autowired
	private PlayaService	playaService;

	@Autowired
	private NegocioService	negocioService;

	@Autowired
	private EventoService	eventoService;

	@Autowired
	private UsuarioService	usuarioService;


	@GetMapping("/playas/{id}")
	public ResponseEntity<byte[]> playa(@PathVariable int id, @RequestHeader(value = "If-None-Match", required = false) String etag) {
		return imagen(() -> playaService.findOne(id).getImagen(), etag);
	}

	@GetMapping("/negocios/{id}")
	public ResponseEntity<byte[]> negocio(@PathVariable int id, @RequestHeader(value = "If-None-Match", required = false) String etag) {
		return imagen(() -> negocioService.findOneToDisplay(id).getImagen(), etag);
	}

	@GetMapping("/eventos/{id}")
	public ResponseEntity<byte[]> evento(@PathVariable int id, @RequestHeader(value = "If-None-Match", required = false) String etag) {
		return imagen(() -> eventoService.findOneToDisplay(id).getImagen(), etag);
	}

	@GetMapping("/usuarios/{id}")
	public ResponseEntity<byte[]> usuario(@PathVariable int id, @RequestHeader(value = "If-None-Match", required = false) String etag) {
		return imagen(() -> usuarioService.findOne(id).getImagen(), etag);
	}

	@Autowired
	private services.ImagenService	imagenService;

	private ResponseEntity<byte[]> imagen(Supplier<Imagen> buscar, String ifNoneMatch) {
		Imagen imagen;
		String etag;

		try {
			imagen = buscar.get();
		} catch (IllegalArgumentException | NullPointerException noExiste) {
			imagen = null;
		}
		if (imagen == null)
			return ResponseEntity.notFound().build();

		// Fase 8: si el cubo de R2 es público, la imagen se sirve desde allí
		// (CDN de Cloudflare) y esta ruta solo redirige.
		java.util.Optional<String> publica = imagenService.urlPublica(imagen);
		if (publica.isPresent())
			return ResponseEntity.status(HttpStatus.FOUND).location(java.net.URI.create(publica.get()))
				.cacheControl(CacheControl.maxAge(java.time.Duration.ofHours(1))).build();

		// El proxy perezoso de Imagen da el id sin leer los bytes
		etag = "\"imagen-" + imagen.getId() + "\"";
		if (etag.equals(ifNoneMatch))
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(etag).cacheControl(CacheControl.noCache()).build();

		return ResponseEntity.ok()
			.eTag(etag)
			.cacheControl(CacheControl.noCache())
			.contentType(MediaType.parseMediaType(imagen.getTipo()))
			.header("X-Content-Type-Options", "nosniff")
			.body(imagenService.datos(imagen));
	}

}
