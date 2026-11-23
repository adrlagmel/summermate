/* Dtos.java
 *
 * Lo que entra y sale de la API del móvil (Fase 8), como records. Las
 * entidades no salen nunca tal cual: así la API no expone campos internos
 * (contraseñas, versiones, relaciones perezosas) y puede cambiar el modelo
 * sin romper la app. Las imágenes van como ruta relativa (/imagenes/...).
 */

package api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.data.domain.Page;

import domain.Evento;
import domain.Mensaje;
import domain.Negocio;
import domain.Playa;
import domain.Reserva;
import domain.ValoracionNegocio;
import domain.ValoracionPlaya;
import services.AgendaService;

public final class Dtos {

	private Dtos() {
	}

	// Páginas -----------------------------------------------------------------

	public record Pagina<T>(List<T> contenido, int pagina, int tamano, long total, int totalPaginas) {

		public static <E, T> Pagina<T> de(Page<E> page, Function<E, T> mapa) {
			// Páginas desde 1, como el parámetro ?page= (ver controllers.Paginacion)
			return new Pagina<>(page.getContent().stream().map(mapa).toList(), page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages());
		}
	}

	// Autenticación -------------------------------------------------------------

	public record Login(@NotBlank String username, @NotBlank String password) {
	}

	public record Renovacion(@NotBlank String refreshToken) {
	}

	// Playas -----------------------------------------------------------------

	public record PlayaResumen(int id, String nombre, String ciudad, String provincia, Double valoracionMedia, double latitud, double longitud,
		String imagen) {

		public static PlayaResumen de(Playa p) {
			return new PlayaResumen(p.getId(), p.getNombre(), p.getLocalizacion().getCiudad(), p.getLocalizacion().getProvincia(), p.getValoracionMedia(),
				p.getLocalizacion().getLatitud(), p.getLocalizacion().getLongitud(), p.getImagen() == null ? null : "/imagenes/playas/" + p.getId());
		}
	}

	public record PlayaDetalle(int id, String nombre, String descripcion, String composicion, String servicios, Integer extension, String direccion,
		String ciudad, String provincia, Integer codigoPostal, double latitud, double longitud, Double valoracionMedia, String imagen) {

		public static PlayaDetalle de(Playa p) {
			var l = p.getLocalizacion();
			String direccion = l.getVia() + " " + l.getNombreVia() + (l.getNumeroVia() == null ? "" : ", " + l.getNumeroVia());
			return new PlayaDetalle(p.getId(), p.getNombre(), p.getDescripcion(), p.getComposicion(), p.getServicios(), p.getExtension(), direccion,
				l.getCiudad(), l.getProvincia(), l.getCodigoPostal(), l.getLatitud(), l.getLongitud(), p.getValoracionMedia(),
				p.getImagen() == null ? null : "/imagenes/playas/" + p.getId());
		}
	}

	// Negocios ---------------------------------------------------------------

	public record NegocioResumen(int id, String nombre, String tipo, String ciudad, Integer playaId, String playa, Integer aforo, Double valoracionMedia,
		boolean activo, boolean destacado, String imagen) {

		public static NegocioResumen de(Negocio n) {
			return new NegocioResumen(n.getId(), n.getNombre(), n.getTipo(), n.getPlaya() == null ? null : n.getPlaya().getLocalizacion().getCiudad(),
				n.getPlaya() == null ? null : n.getPlaya().getId(), n.getPlaya() == null ? null : n.getPlaya().getNombre(), n.getAforo(),
				n.getValoracionMedia(), Boolean.TRUE.equals(n.getNegocioActivo()), n.getEmpresario().isPremium(),
				n.getImagen() == null ? null : "/imagenes/negocios/" + n.getId());
		}
	}

	public record NegocioDetalle(int id, String nombre, String tipo, String descripcion, String telefono, String paginaWeb, Integer aforo,
		Double valoracionMedia, boolean activo, boolean destacado, Integer playaId, String playa, LocalTime horaApertura, LocalTime horaCierre,
		Integer duracionReserva, String imagen) {

		public static NegocioDetalle de(Negocio n) {
			return new NegocioDetalle(n.getId(), n.getNombre(), n.getTipo(), n.getDescripcion(), n.getTelefono(), n.getPaginaWeb(), n.getAforo(),
				n.getValoracionMedia(), Boolean.TRUE.equals(n.getNegocioActivo()), n.getEmpresario().isPremium(),
				n.getPlaya() == null ? null : n.getPlaya().getId(), n.getPlaya() == null ? null : n.getPlaya().getNombre(), n.getHoraApertura(),
				n.getHoraCierre(), n.getDuracionReserva(), n.getImagen() == null ? null : "/imagenes/negocios/" + n.getId());
		}
	}

	public record Franja(LocalDateTime inicio, boolean llena, int plazasLibres, Integer mesasLibres) {

		public static Franja de(AgendaService.Franja f) {
			return new Franja(f.inicio(), f.llena(), Math.max(f.plazasLibres(), 0), f.mesasLibres());
		}
	}

	public record Disponibilidad(LocalDate dia, boolean cerrado, List<Franja> franjas) {
	}

	// Eventos ----------------------------------------------------------------

	public record EventoResumen(int id, String nombre, LocalDateTime fechaCelebracion, BigDecimal precio, int negocioId, String negocio, String playa,
		String imagen) {

		public static EventoResumen de(Evento e) {
			Negocio n = e.getNegocio();
			return new EventoResumen(e.getId(), e.getNombre(), e.getFechaCelebracion(), e.getPrecio(), n.getId(), n.getNombre(),
				n.getPlaya() == null ? null : n.getPlaya().getNombre(), e.getImagen() == null ? null : "/imagenes/eventos/" + e.getId());
		}
	}

	public record EventoDetalle(int id, String codigo, String nombre, String descripcion, LocalDateTime fechaCelebracion, BigDecimal precio, int negocioId,
		String negocio, String playa, String imagen, boolean inscrito) {

		public static EventoDetalle de(Evento e, boolean inscrito) {
			Negocio n = e.getNegocio();
			return new EventoDetalle(e.getId(), e.getCodigo(), e.getNombre(), e.getDescripcion(), e.getFechaCelebracion(), e.getPrecio(), n.getId(),
				n.getNombre(), n.getPlaya() == null ? null : n.getPlaya().getNombre(), e.getImagen() == null ? null : "/imagenes/eventos/" + e.getId(),
				inscrito);
		}
	}

	// Valoraciones -------------------------------------------------------------

	public record Valoracion(int id, String titulo, String comentario, int puntuacion, LocalDateTime fecha, String autor, Integer votosUtiles) {

		public static Valoracion de(ValoracionPlaya v) {
			return new Valoracion(v.getId(), v.getTitulo(), v.getComentario(), v.getPuntuacion(), v.getFecha(), v.getUsuario().getNombre(), null);
		}

		public static Valoracion de(ValoracionNegocio v) {
			return new Valoracion(v.getId(), v.getTitulo(), v.getComentario(), v.getPuntuacion(), v.getFecha(), v.getReserva().getUsuario().getNombre(),
				v.getComentarioUtil() == null ? 0 : v.getComentarioUtil());
		}
	}

	public record NuevaValoracion(@NotBlank @Size(max = 255) String titulo, @NotBlank @Size(max = 255) String comentario,
		@NotNull @Min(0) @Max(10) Integer puntuacion) {
	}

	// Reservas ---------------------------------------------------------------

	public record ReservaDto(int id, String codigo, int negocioId, String negocio, LocalDateTime fecha, int comensales, boolean fumadores,
		String comentarios, String mesa, Integer valoracionId) {

		public static ReservaDto de(Reserva r) {
			return new ReservaDto(r.getId(), r.getCodigo(), r.getNegocio().getId(), r.getNegocio().getNombre(), r.getFecha(), r.getComensales(),
				Boolean.TRUE.equals(r.getFumadores()), r.getComentarios(), r.getMesa() == null ? null : r.getMesa().getNombre(),
				r.getValoracionNegocio() == null ? null : r.getValoracionNegocio().getId());
		}
	}

	public record NuevaReserva(@NotNull Integer negocioId, @NotNull @Future LocalDateTime fecha, @NotNull @Min(1) @Max(10) Integer comensales,
		Boolean fumadores, @Size(max = 255) String comentarios) {
	}

	// Perfil -----------------------------------------------------------------

	public record Colaboracion(String nivel, int puntos, int faltan) {
	}

	public record Perfil(int id, String username, String rol, String nombre, String apellidos, String email, String imagen, Colaboracion colaboracion) {
	}

	// Mensajes ---------------------------------------------------------------

	public record MensajeDto(int id, LocalDateTime fecha, String de, String para, String asunto, String cuerpo) {

		public static MensajeDto de(Mensaje m, boolean conCuerpo) {
			return new MensajeDto(m.getId(), m.getFechaEnvio(), m.getRemitente().getNombre() + " " + m.getRemitente().getApellidos(),
				m.getBeneficiario().getNombre() + " " + m.getBeneficiario().getApellidos(), m.getAsunto(), conCuerpo ? m.getCuerpo() : null);
		}

		public static final Comparator<Mensaje> RECIENTES_PRIMERO = Comparator.comparing(Mensaje::getFechaEnvio).reversed();
	}

	public record NuevoMensaje(@NotNull Integer beneficiarioId, @NotBlank @Size(max = 255) String asunto, @NotBlank String cuerpo) {
	}

}
