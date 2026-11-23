/* ApiPublicoController.java
 *
 * Consulta pública de la API (sin iniciar sesión), lo mismo que la web:
 *   GET /api/v1/playas?page=&sort=&dir=            listado
 *   GET /api/v1/playas/cercanas?lat=&lon=&radio=   en un radio (km, por defecto 25)
 *   GET /api/v1/playas/{id}                        ficha
 *   GET /api/v1/playas/{id}/negocios               negocios de la playa
 *   GET /api/v1/playas/{id}/valoraciones
 *   GET /api/v1/negocios?ciudad=                   listado o búsqueda por ciudad
 *   GET /api/v1/negocios/{id}
 *   GET /api/v1/negocios/{id}/valoraciones
 *   GET /api/v1/negocios/{id}/disponibilidad?dia=  ocupación por franjas de media hora
 *   GET /api/v1/eventos?cuando=proximos|pasados
 *   GET /api/v1/eventos/{id}
 * Las listas van paginadas de 10 en 10 (page empieza en 1).
 */

package api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.dto.Dtos.Disponibilidad;
import api.dto.Dtos.EventoDetalle;
import api.dto.Dtos.EventoResumen;
import api.dto.Dtos.Franja;
import api.dto.Dtos.NegocioDetalle;
import api.dto.Dtos.NegocioResumen;
import api.dto.Dtos.Pagina;
import api.dto.Dtos.PlayaDetalle;
import api.dto.Dtos.PlayaResumen;
import api.dto.Dtos.Valoracion;
import controllers.Paginacion;
import domain.Evento;
import domain.Negocio;
import domain.Playa;
import repositories.CierreRepository;
import security.LoginService;
import services.AgendaService;
import services.EventoService;
import services.NegocioService;
import services.PlayaService;
import services.ValoracionNegocioService;
import services.ValoracionPlayaService;

@RestController
@RequestMapping("/api/v1")
public class ApiPublicoController {

	private static final int					RADIO_POR_DEFECTO	= 25;

	private final PlayaService					playaService;
	private final NegocioService				negocioService;
	private final EventoService					eventoService;
	private final ValoracionPlayaService		valoracionPlayaService;
	private final ValoracionNegocioService		valoracionNegocioService;
	private final AgendaService					agendaService;
	private final CierreRepository				cierreRepository;


	public ApiPublicoController(PlayaService playaService, NegocioService negocioService, EventoService eventoService,
		ValoracionPlayaService valoracionPlayaService, ValoracionNegocioService valoracionNegocioService, AgendaService agendaService,
		CierreRepository cierreRepository) {
		this.playaService = playaService;
		this.negocioService = negocioService;
		this.eventoService = eventoService;
		this.valoracionPlayaService = valoracionPlayaService;
		this.valoracionNegocioService = valoracionNegocioService;
		this.agendaService = agendaService;
		this.cierreRepository = cierreRepository;
	}

	// Playas -----------------------------------------------------------------

	@GetMapping("/playas")
	public Pagina<PlayaResumen> playas(Paginacion paginacion) {
		return Pagina.de(playaService.findAll(paginacion.playas()), PlayaResumen::de);
	}

	@GetMapping("/playas/cercanas")
	public List<PlayaResumen> cercanas(@RequestParam double lat, @RequestParam double lon, @RequestParam(required = false) Integer radio) {
		int km = radio == null ? RADIO_POR_DEFECTO : Math.max(1, Math.min(radio, 200));
		return playaService.nearToMe(lat, lon, km).stream().map(PlayaResumen::de).toList();
	}

	@GetMapping("/playas/{id}")
	public PlayaDetalle playa(@PathVariable int id) {
		return PlayaDetalle.de(playa0(id));
	}

	@GetMapping("/playas/{id}/negocios")
	public Pagina<NegocioResumen> negociosDePlaya(@PathVariable int id, Paginacion paginacion) {
		playa0(id);
		return Pagina.de(negocioService.findNegociosDePlaya(id, paginacion.negocios()), NegocioResumen::de);
	}

	@GetMapping("/playas/{id}/valoraciones")
	public List<Valoracion> valoracionesDePlaya(@PathVariable int id) {
		playa0(id);
		return valoracionPlayaService.findValoracionPlayaByPlaya(id).stream().map(Valoracion::de).toList();
	}

	// Negocios ---------------------------------------------------------------

	@GetMapping("/negocios")
	public Pagina<NegocioResumen> negocios(@RequestParam(required = false) String ciudad, Paginacion paginacion) {
		if (StringUtils.hasText(ciudad))
			return Pagina.de(negocioService.searchByTown(ciudad.trim(), paginacion.negocios()), NegocioResumen::de);
		return Pagina.de(negocioService.findAll(paginacion.negocios()), NegocioResumen::de);
	}

	@GetMapping("/negocios/{id}")
	public NegocioDetalle negocio(@PathVariable int id) {
		return NegocioDetalle.de(negocio0(id));
	}

	@GetMapping("/negocios/{id}/valoraciones")
	public List<Valoracion> valoracionesDeNegocio(@PathVariable int id) {
		negocio0(id);
		return valoracionNegocioService.valoracionesNegocioDeUnNegocio(id).stream().map(Valoracion::de).toList();
	}

	@GetMapping("/negocios/{id}/disponibilidad")
	public Disponibilidad disponibilidad(@PathVariable int id, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia) {
		Negocio negocio = negocio0(id);
		LocalDate d = dia == null ? LocalDate.now() : dia;
		boolean cerrado = cierreRepository.cerrado(id, d);
		return new Disponibilidad(d, cerrado, cerrado ? List.of() : agendaService.franjas(negocio, d).stream().map(Franja::de).toList());
	}

	// Eventos ----------------------------------------------------------------

	@GetMapping("/eventos")
	public Pagina<EventoResumen> eventos(@RequestParam(required = false) String cuando, Paginacion paginacion) {
		if ("proximos".equals(cuando))
			return Pagina.de(eventoService.EventosActivos(paginacion.eventos()), EventoResumen::de);
		if ("pasados".equals(cuando))
			return Pagina.de(eventoService.EventosInactivos(paginacion.eventos()), EventoResumen::de);
		return Pagina.de(eventoService.findAll(paginacion.eventos()), EventoResumen::de);
	}

	@GetMapping("/eventos/{id}")
	public EventoDetalle evento(@PathVariable int id) {
		Evento evento = eventoService.findOne(id);
		if (evento == null)
			throw new ApiErrores.NoEncontrado();
		boolean inscrito = LoginService.isAuthenticated()
			&& evento.getUsuarios().stream().anyMatch(u -> u.getUserAccount().getId() == LoginService.getPrincipal().getId());
		return EventoDetalle.de(evento, inscrito);
	}

	// -------------------------------------------------------------------------

	private Playa playa0(int id) {
		Playa playa = playaService.findOne(id);
		if (playa == null)
			throw new ApiErrores.NoEncontrado();
		return playa;
	}

	private Negocio negocio0(int id) {
		try {
			return negocioService.findOneToDisplay(id);
		} catch (IllegalArgumentException noExiste) {
			throw new ApiErrores.NoEncontrado();
		}
	}

}
