package services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Actor;
import domain.Carpeta;
import domain.Cliente;
import domain.Empresario;
import domain.Mensaje;
import domain.Negocio;
import domain.Reserva;
import domain.Usuario;
import domain.ValoracionNegocio;
import domain.ValoracionPlaya;
import repositories.ClienteRepository;
import security.LoginService;
import security.UserAccount;
import utilities.Assert;

/*
 * Derechos del RGPD que el cliente ejerce él mismo (Fase 6):
 * - Portabilidad / acceso: exportar() devuelve sus datos para descargarlos en JSON.
 * - Supresión: borrarCuenta() anonimiza la cuenta. Se borran los datos
 *   personales, los mensajes de sus carpetas, sus denuncias y votos, sus
 *   reservas pendientes y sus inscripciones a eventos futuros. Sus
 *   valoraciones y reservas pasadas se quedan, pero ya sin nadie detrás
 *   ("Usuario eliminado"), para no descuadrar las medias de las playas y los
 *   negocios. Un empresario deja sus negocios suspendidos.
 * Los administradores no borran su cuenta desde aquí.
 */
@Service
@Transactional
public class CuentaService {

	// Valores de relleno que cumplen las validaciones de las entidades
	private static final String		TELEFONO_ANONIMO	= "+000000000";
	private static final String		NIF_ANONIMO			= "00000000T";
	private static final LocalDate	FECHA_ANONIMA		= LocalDate.of(1900, 1, 1);

	@Autowired
	private ClienteRepository		clienteRepository;

	@Autowired
	private PasswordEncoder			passwordEncoder;

	@PersistenceContext
	private EntityManager			em;


	@Transactional(readOnly = true)
	public Map<String, Object> exportar() {
		Cliente c = clientePrincipal();
		Map<String, Object> datos = new LinkedHashMap<>();

		datos.put("exportado", LocalDateTime.now());
		datos.put("usuario", c.getUserAccount().getUsername());
		Map<String, Object> perfil = new LinkedHashMap<>();
		perfil.put("nombre", c.getNombre());
		perfil.put("apellidos", c.getApellidos());
		perfil.put("email", c.getEmail());
		perfil.put("telefono", c.getTelefono());
		perfil.put("fechaNacimiento", c.getFechaNacimiento());
		perfil.put("nacionalidad", c.getNacionalidad());
		perfil.put("direccion", c.getDireccion());
		perfil.put("sexo", c.getSexo());
		if (c instanceof Usuario u)
			perfil.put("estadoActual", u.getEstadoActual());
		if (c instanceof Empresario e) {
			perfil.put("cif", e.getCif());
			perfil.put("premiumHasta", e.getPremiumHasta());
		}
		datos.put("perfil", perfil);

		if (c instanceof Usuario u) {
			List<Map<String, Object>> reservas = new ArrayList<>();
			for (Reserva r : u.getReservas()) {
				Map<String, Object> m = new LinkedHashMap<>();
				m.put("codigo", r.getCodigo());
				m.put("negocio", r.getNegocio().getNombre());
				m.put("fecha", r.getFecha());
				m.put("comensales", r.getComensales());
				m.put("comentarios", r.getComentarios());
				ValoracionNegocio v = r.getValoracionNegocio();
				if (v != null)
					m.put("valoracion", Map.of("titulo", v.getTitulo(), "comentario", v.getComentario(), "puntuacion", v.getPuntuacion(), "fecha", v.getFecha()));
				reservas.add(m);
			}
			datos.put("reservas", reservas);

			List<Map<String, Object>> valoracionesPlaya = new ArrayList<>();
			for (ValoracionPlaya v : u.getValoracionPlayas())
				valoracionesPlaya.add(Map.of("playa", v.getPlaya().getNombre(), "titulo", v.getTitulo(), "comentario", v.getComentario(), "puntuacion", v.getPuntuacion(), "fecha", v.getFecha()));
			datos.put("valoracionesDePlayas", valoracionesPlaya);

			datos.put("eventos", u.getEventos().stream().map(e -> Map.of("nombre", e.getNombre(), "fecha", e.getFechaCelebracion())).toList());
		}

		if (c instanceof Empresario e)
			datos.put("negocios", e.getNegocios().stream().map(n -> Map.of("nombre", n.getNombre(), "activo", n.getNegocioActivo())).toList());

		datos.put("denuncias", c.getDenuncias().stream().map(d -> Map.of("titulo", d.getTitulo(), "comentario", d.getComentario(), "tipo", d.getTipo())).toList());

		List<Map<String, Object>> mensajes = new ArrayList<>();
		for (Carpeta carpeta : c.getCarpetas())
			for (Mensaje m : carpeta.getMensajes()) {
				Map<String, Object> fila = new LinkedHashMap<>();
				fila.put("carpeta", carpeta.getNombre());
				fila.put("fecha", m.getFechaEnvio());
				fila.put("de", m.getRemitente().getEmail());
				fila.put("para", m.getBeneficiario().getEmail());
				fila.put("asunto", m.getAsunto());
				fila.put("cuerpo", m.getCuerpo());
				mensajes.add(fila);
			}
		datos.put("mensajes", mensajes);

		return datos;
	}

	// Devuelve false si la contraseña no es la suya (y no cambia nada)
	public boolean borrarCuenta(String password) {
		Cliente c = clientePrincipal();
		UserAccount cuenta = c.getUserAccount();
		if (password == null || !passwordEncoder.matches(password, cuenta.getPassword()))
			return false;

		int id = c.getId();
		LocalDateTime ahora = LocalDateTime.now();

		// Contenido propio que no tiene sentido sin la persona
		em.createQuery("delete from Mensaje m where m.carpeta.id in (select c.id from Carpeta c where c.actor.id = :id)").setParameter("id", id).executeUpdate();
		em.createQuery("delete from DenunciaValoracion d where d.cliente.id = :id").setParameter("id", id).executeUpdate();
		em.createQuery("delete from VotoUtil v where v.cliente.id = :id").setParameter("id", id).executeUpdate();

		if (c instanceof Usuario u) {
			for (Reserva r : new ArrayList<>(u.getReservas()))
				if (r.getFecha().isAfter(ahora)) {
					u.getReservas().remove(r);
					r.getNegocio().getReservas().remove(r);
					em.remove(r);
				}
			u.getEventos().removeIf(e -> {
				boolean futuro = e.getFechaCelebracion().isAfter(ahora);
				if (futuro)
					e.getUsuarios().remove(u);
				return futuro;
			});
			u.setEstadoActual(null);
			u.setImagen(null);
		}

		if (c instanceof Empresario e) {
			for (Negocio n : e.getNegocios())
				n.setNegocioActivo(false);
			e.setCif(NIF_ANONIMO);
			e.setPremiumHasta(null);
		}

		anonimizar(c);
		em.flush();
		em.clear();
		return true;
	}

	private void anonimizar(Actor a) {
		a.setNombre("Usuario");
		a.setApellidos("eliminado");
		a.setEmail("borrado-" + a.getId() + "@summermate.invalid");
		if (a instanceof Cliente c) {
			c.setTelefono(TELEFONO_ANONIMO);
			c.setFechaNacimiento(FECHA_ANONIMA);
			c.setNacionalidad("-");
			c.setDireccion("-");
			c.setSexo(null);
		}
		UserAccount cuenta = a.getUserAccount();
		cuenta.setUsername("borrado-" + a.getId());
		// Un hash que nunca coincide: ya no se puede entrar con esa cuenta
		cuenta.setPassword("{bcrypt}borrada");
	}

	private Cliente clientePrincipal() {
		Assert.isTrue(LoginService.isAuthenticated());
		Cliente c = clienteRepository.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(c);
		return c;
	}

}
