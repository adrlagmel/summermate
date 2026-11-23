/* ApiYoController.java
 *
 * Lo del usuario que ha iniciado sesión (Bearer token):
 *   GET    /api/v1/yo                                  perfil y nivel de colaboración
 *   GET    /api/v1/yo/reservas                         sus reservas (paginadas)
 *   POST   /api/v1/yo/reservas                         reservar
 *   DELETE /api/v1/yo/reservas/{id}                    cancelar
 *   POST   /api/v1/yo/reservas/{id}/valoracion         valorar una reserva disfrutada
 *   POST   /api/v1/playas/{id}/valoraciones            valorar una playa
 *   GET    /api/v1/yo/eventos                          eventos a los que va
 *   POST   /api/v1/eventos/{id}/inscripcion            apuntarse
 *   DELETE /api/v1/eventos/{id}/inscripcion            borrarse
 *   GET    /api/v1/yo/mensajes?carpeta=entrada|enviados
 *   GET    /api/v1/yo/mensajes/{id}
 *   POST   /api/v1/yo/mensajes                         enviar
 *   DELETE /api/v1/yo/mensajes/{id}
 *   POST   /api/v1/valoraciones/negocios/{id}/util     marcar como útil (y DELETE para quitarlo)
 * Las reglas son las de la web: los servicios son los mismos.
 */

package api;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.dto.Dtos.Colaboracion;
import api.dto.Dtos.EventoResumen;
import api.dto.Dtos.MensajeDto;
import api.dto.Dtos.NuevaReserva;
import api.dto.Dtos.NuevaValoracion;
import api.dto.Dtos.NuevoMensaje;
import api.dto.Dtos.Pagina;
import api.dto.Dtos.Perfil;
import api.dto.Dtos.ReservaDto;
import api.dto.Dtos.Valoracion;
import controllers.Paginacion;
import domain.Actor;
import domain.Carpeta;
import domain.Mensaje;
import domain.Reserva;
import domain.Usuario;
import domain.ValoracionNegocio;
import domain.ValoracionPlaya;
import forms.MensajeForm;
import forms.ReservaForm;
import forms.ValoracionNegocioForm;
import forms.ValoracionPlayaForm;
import repositories.ActorRepository;
import security.LoginService;
import services.ActorService;
import services.CarpetaService;
import services.EventoService;
import services.MensajeService;
import services.NegocioService;
import services.PlayaService;
import services.ReservaService;
import services.UsuarioService;
import services.ValoracionNegocioService;
import services.ValoracionPlayaService;
import services.VotoUtilService;
import utilities.Assert;

@RestController
@RequestMapping("/api/v1")
public class ApiYoController {

	private final ActorService				actorService;
	private final ActorRepository			actorRepository;
	private final UsuarioService			usuarioService;
	private final ReservaService			reservaService;
	private final NegocioService			negocioService;
	private final PlayaService				playaService;
	private final ValoracionPlayaService	valoracionPlayaService;
	private final ValoracionNegocioService	valoracionNegocioService;
	private final EventoService				eventoService;
	private final MensajeService			mensajeService;
	private final CarpetaService			carpetaService;
	private final VotoUtilService			votoUtilService;


	public ApiYoController(ActorService actorService, ActorRepository actorRepository, UsuarioService usuarioService, ReservaService reservaService,
		NegocioService negocioService, PlayaService playaService, ValoracionPlayaService valoracionPlayaService,
		ValoracionNegocioService valoracionNegocioService, EventoService eventoService, MensajeService mensajeService, CarpetaService carpetaService,
		VotoUtilService votoUtilService) {
		this.actorService = actorService;
		this.actorRepository = actorRepository;
		this.usuarioService = usuarioService;
		this.reservaService = reservaService;
		this.negocioService = negocioService;
		this.playaService = playaService;
		this.valoracionPlayaService = valoracionPlayaService;
		this.valoracionNegocioService = valoracionNegocioService;
		this.eventoService = eventoService;
		this.mensajeService = mensajeService;
		this.carpetaService = carpetaService;
		this.votoUtilService = votoUtilService;
	}

	// Perfil -----------------------------------------------------------------

	@GetMapping("/yo")
	public Perfil yo() {
		Actor actor = actorService.findByPrincipal();
		String rol = LoginService.getPrincipal().getAuthorities().iterator().next().getAuthority();
		Colaboracion colaboracion = null;
		String imagen = null;
		if (actor instanceof Usuario u) {
			services.Colaboracion c = usuarioService.colaboracion(u.getId());
			colaboracion = new Colaboracion(c.nivel().name(), c.puntos(), c.faltan());
			imagen = u.getImagen() == null ? null : "/imagenes/usuarios/" + u.getId();
		}
		return new Perfil(actor.getId(), actor.getUserAccount().getUsername(), rol, actor.getNombre(), actor.getApellidos(), actor.getEmail(), imagen,
			colaboracion);
	}

	// Reservas ---------------------------------------------------------------

	@GetMapping("/yo/reservas")
	public Pagina<ReservaDto> reservas(Paginacion paginacion) {
		return Pagina.de(reservaService.findReservasByUsuario(paginacion.reservas()), ReservaDto::de);
	}

	@PostMapping("/yo/reservas")
	public ResponseEntity<ReservaDto> reservar(@Valid @RequestBody NuevaReserva nueva) {
		ReservaForm form = new ReservaForm();
		form.setReservaId(0);
		form.setNegocio(negocioService.findOneToDisplay(nueva.negocioId()));
		form.setFecha(nueva.fecha());
		form.setComensales(nueva.comensales());
		form.setFumadores(Boolean.TRUE.equals(nueva.fumadores()));
		form.setComentarios(nueva.comentarios() == null ? "" : nueva.comentarios());

		Reserva reserva = reservaService.save(reservaService.reconstruct(form));
		return ResponseEntity.created(URI.create("/api/v1/yo/reservas/" + reserva.getId())).body(ReservaDto.de(reserva));
	}

	@DeleteMapping("/yo/reservas/{id}")
	public ResponseEntity<Void> cancelar(@PathVariable int id) {
		Reserva reserva = reservaService.findOneToEdit(id);
		reservaService.delete(reserva);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/yo/reservas/{id}/valoracion")
	public ResponseEntity<Valoracion> valorarReserva(@PathVariable int id, @Valid @RequestBody NuevaValoracion nueva) {
		ValoracionNegocioForm form = new ValoracionNegocioForm();
		form.setId(0);
		form.setReserva(reservaService.findOneToEdit(id));
		form.setTitulo(nueva.titulo());
		form.setComentario(nueva.comentario());
		form.setPuntuacion(nueva.puntuacion());

		ValoracionNegocio v = valoracionNegocioService.reconstruct(form);
		valoracionNegocioService.save(v);
		return ResponseEntity.status(HttpStatus.CREATED).body(Valoracion.de(v));
	}

	// Valoraciones de playa ----------------------------------------------------

	@PostMapping("/playas/{id}/valoraciones")
	public ResponseEntity<Valoracion> valorarPlaya(@PathVariable int id, @Valid @RequestBody NuevaValoracion nueva) {
		ValoracionPlayaForm form = new ValoracionPlayaForm();
		form.setId(0);
		form.setPlaya(playaService.findOne(id));
		Assert.notNull(form.getPlaya());
		form.setTitulo(nueva.titulo());
		form.setComentario(nueva.comentario());
		form.setPuntuacion(nueva.puntuacion());

		ValoracionPlaya v = valoracionPlayaService.reconstruct(form);
		valoracionPlayaService.save(v);
		return ResponseEntity.status(HttpStatus.CREATED).body(Valoracion.de(v));
	}

	@PostMapping("/valoraciones/negocios/{id}/util")
	public ResponseEntity<Void> util(@PathVariable int id) {
		votoUtilService.votar(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/valoraciones/negocios/{id}/util")
	public ResponseEntity<Void> quitarUtil(@PathVariable int id) {
		votoUtilService.quitar(id);
		return ResponseEntity.noContent().build();
	}

	// Eventos ----------------------------------------------------------------

	@GetMapping("/yo/eventos")
	public List<EventoResumen> eventos() {
		return eventoService.findByUsuario().stream().map(EventoResumen::de).toList();
	}

	@PostMapping("/eventos/{id}/inscripcion")
	public ResponseEntity<Void> apuntarse(@PathVariable int id) {
		eventoService.participate(eventoService.findOneToDisplay(id), usuarioService.findByPrincipal());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/eventos/{id}/inscripcion")
	public ResponseEntity<Void> borrarse(@PathVariable int id) {
		eventoService.unregister(eventoService.findOneToDisplay(id), usuarioService.findByPrincipal());
		return ResponseEntity.noContent().build();
	}

	// Mensajes ---------------------------------------------------------------

	@GetMapping("/yo/mensajes")
	public List<MensajeDto> mensajes(@RequestParam(defaultValue = "entrada") String carpeta) {
		Actor actor = actorService.findByPrincipal();
		Carpeta c = "enviados".equals(carpeta) ? carpetaService.findEnviadosActor(actor) : carpetaService.findEntradaActor(actor);
		return c.getMensajes().stream().sorted(MensajeDto.RECIENTES_PRIMERO).map(m -> MensajeDto.de(m, false)).toList();
	}

	@GetMapping("/yo/mensajes/{id}")
	public MensajeDto mensaje(@PathVariable int id) {
		return MensajeDto.de(mensajeService.findOne(id), true);
	}

	@PostMapping("/yo/mensajes")
	public ResponseEntity<MensajeDto> enviar(@Valid @RequestBody NuevoMensaje nuevo) {
		MensajeForm form = new MensajeForm();
		form.setBeneficiario(actorRepository.findById(nuevo.beneficiarioId()).orElseThrow(ApiErrores.NoEncontrado::new));
		form.setAsunto(nuevo.asunto());
		form.setCuerpo(nuevo.cuerpo());

		Mensaje enviado = mensajeService.reconstruct(form);
		mensajeService.save(enviado);
		return ResponseEntity.status(HttpStatus.CREATED).body(MensajeDto.de(enviado, true));
	}

	@DeleteMapping("/yo/mensajes/{id}")
	public ResponseEntity<Void> borrarMensaje(@PathVariable int id) {
		mensajeService.delete(mensajeService.findOne(id));
		return ResponseEntity.noContent().build();
	}

}
