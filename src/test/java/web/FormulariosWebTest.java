/* FormulariosWebTest.java
 *
 * Un test (al menos) por cada formulario DTO: se envía como lo haría el
 * navegador (POST con CSRF, y _method para PUT) y se comprueba el efecto en
 * la base de datos o los errores de validación.
 */

package web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

class FormulariosWebTest extends AbstractWebTest {

	private static final DateTimeFormatter	LOCAL	= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	@Autowired
	private PasswordEncoder					passwordEncoder;


	private static String dentroDeDias(int dias, int hora) {
		return LocalDate.now().plusDays(dias).atTime(hora, 0).format(LOCAL);
	}

	private static MockHttpServletRequestBuilder registro(String url, String username) {
		return registro(url, username, true);
	}

	private static MockHttpServletRequestBuilder registro(String url, String username, boolean aceptaCondiciones) {
		return post(url).with(csrf())
			.param("registroForm.username", username)
			.param("registroForm.password", "secreta1")
			.param("registroForm.verifyPassword", "secreta1")
			.param("registroForm.nombre", "Lucía")
			.param("registroForm.apellidos", "Martín")
			.param("registroForm.email", "lucia@example.com")
			.param("registroForm.telefono", "+34666111222")
			.param("registroForm.fechaNacimiento", "1995-04-03")
			.param("registroForm.nacionalidad", "Española")
			.param("registroForm.direccion", "Calle Sol 1")
			.param("registroForm.sexo", "MUJER")
			.param("registroForm.contractAccepted", String.valueOf(aceptaCondiciones));
	}

	// RegistroForm / UsuarioRegistroForm / EmpresarioRegistroForm --------------

	@Test
	void registroDeUsuario() throws Exception {
		mvc.perform(registro("/registro/usuario", "lucia95").param("estadoActual", "De vacaciones"))
			.andExpect(redirectedUrl("/login"));
		assertEquals(1, contar("select count(*) from useraccount u join useraccount_authorities a on a.useraccount_id = u.id where u.username = 'lucia95' and a.authority = 'USUARIO'"));
	}

	@Test
	void registroConContrasenasDistintas() throws Exception {
		mvc.perform(registro("/registro/usuario", "lucia95").param("estadoActual", "x").param("registroForm.verifyPassword", "otra1234"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrorCode("form", "registroForm.verifyPassword", "actor.password.error.coincidir"));
		assertEquals(0, contar("select count(*) from useraccount where username = 'lucia95'"));
	}

	@Test
	void registroSinAceptarLasCondiciones() throws Exception {
		mvc.perform(registro("/registro/usuario", "lucia95", false).param("estadoActual", "x"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("form", "registroForm.contractAccepted"));
	}

	@Test
	void registroConUsuarioRepetido() throws Exception {
		mvc.perform(registro("/registro/usuario", "usuario1").param("estadoActual", "x"))
			.andExpect(status().isOk()).andExpect(model().attribute("message", "register.duplicated.user"));
	}

	@Test
	void registroDeEmpresario() throws Exception {
		mvc.perform(registro("/registro/empresario", "empresa95").param("cif", "B12345674"))
			.andExpect(redirectedUrl("/login"));
		assertEquals(1, contar("select count(*) from empresario e join actor a on a.id = e.id join useraccount u on u.id = a.useraccount_id where u.username = 'empresa95' and e.cif = 'B12345674'"));
	}

	// AdministradorRegistroForm ---------------------------------------------

	@Test
	void altaDeAdministrador() throws Exception {
		mvc.perform(post("/admin/administradores").with(como("admin")).with(csrf())
			.param("username", "admin3").param("password", "secreta1").param("verifyPassword", "secreta1")
			.param("nombre", "Ana").param("apellidos", "Ruiz").param("email", "ana@example.com"))
			.andExpect(redirectedUrl("/admin/dashboard"));
		assertEquals(1, contar("select count(*) from useraccount where username = 'admin3'"));
	}

	// UsuarioEditForm y EmpresarioEditForm ------------------------------------

	@Test
	void editarPerfilDeUsuario() throws Exception {
		mvc.perform(post("/perfil").with(como("usuario1")).with(csrf()).param("_method", "put").param("tipo", "usuario")
			.param("nombre", "Nuevo").param("apellidos", "Nombre").param("email", "nuevo@example.com").param("telefono", "+34600000001")
			.param("fechaNacimiento", "1990-01-01").param("nacionalidad", "Española").param("direccion", "Calle 1").param("sexo", "HOMBRE")
			.param("estadoActual", "Aquí"))
			.andExpect(redirectedUrl("/perfil"));
		assertEquals(1, contar("select count(*) from actor where id = 10 and nombre = 'Nuevo' and email = 'nuevo@example.com'"));
	}

	@Test
	void editarPerfilDeEmpresarioConErrores() throws Exception {
		mvc.perform(post("/perfil").with(como("empresario1")).with(csrf()).param("_method", "put").param("tipo", "empresario")
			.param("nombre", "").param("apellidos", "X").param("email", "no-es-un-email").param("telefono", "123")
			.param("fechaNacimiento", "1990-01-01").param("nacionalidad", "E").param("direccion", "C").param("sexo", "HOMBRE").param("cif", "B1"))
			.andExpect(status().isOk()).andExpect(view().name("perfil/editar"))
			.andExpect(model().attributeHasFieldErrors("form", "nombre", "email", "telefono"));
	}

	@Test
	void editarPerfilDeEmpresario() throws Exception {
		mvc.perform(post("/perfil").with(como("empresario1")).with(csrf()).param("_method", "put").param("tipo", "empresario")
			.param("nombre", "Pepe").param("apellidos", "X").param("email", "pepe@example.com").param("telefono", "+34600000002")
			.param("fechaNacimiento", "1980-01-01").param("nacionalidad", "E").param("direccion", "C").param("sexo", "HOMBRE").param("cif", "B99999997"))
			.andExpect(redirectedUrl("/perfil"));
		assertEquals(1, contar("select count(*) from empresario where id = 3 and cif = 'B99999997'"));
	}

	@Test
	void editarPerfilDeEmpresarioConCifMal() throws Exception {
		mvc.perform(post("/perfil").with(como("empresario1")).with(csrf()).param("_method", "put").param("tipo", "empresario")
			.param("nombre", "Pepe").param("apellidos", "X").param("email", "pepe@example.com").param("telefono", "+34600000002")
			.param("fechaNacimiento", "1980-01-01").param("nacionalidad", "E").param("direccion", "C").param("cif", "B99999990"))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("form", "cif"));
	}

	// ActorEditPasswordForm ---------------------------------------------------

	@Test
	void cambiarContrasena() throws Exception {
		mvc.perform(post("/perfil/contrasena").with(como("usuario1")).with(csrf()).param("_method", "put")
			.param("oldPassword", "usuario1").param("password", "nueva123").param("verifyPassword", "nueva123"))
			.andExpect(redirectedUrl("/perfil"));
		String hash = jdbc().queryForObject("select password from useraccount where username = 'usuario1'", String.class);
		assertTrue(passwordEncoder.matches("nueva123", hash));
	}

	@Test
	void cambiarContrasenaConLaActualMal() throws Exception {
		mvc.perform(post("/perfil/contrasena").with(como("usuario1")).with(csrf()).param("_method", "put")
			.param("oldPassword", "equivocada").param("password", "nueva123").param("verifyPassword", "nueva123"))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrorCode("form", "oldPassword", "actor.password.error"));
	}

	// MensajeForm ---------------------------------------------------------------

	@Test
	void enviarMensaje() throws Exception {
		mvc.perform(post("/mensajes").with(como("usuario1")).with(csrf())
			.param("beneficiario", "3").param("asunto", "Hola").param("cuerpo", "¿Tenéis mesa?"))
			.andExpect(redirectedUrl("/carpetas"));
		// Una copia en la entrada del destinatario y otra en "Enviados" del remitente.
		assertEquals(1, contar("select count(*) from mensaje m join carpeta c on c.id = m.carpeta_id where m.asunto = 'Hola' and c.actor_id = 3 and c.nombre = 'Entrada'"));
		assertEquals(1, contar("select count(*) from mensaje m join carpeta c on c.id = m.carpeta_id where m.asunto = 'Hola' and c.actor_id = m.remitente_id and c.nombre = 'Enviados'"));
	}

	@Test
	void mensajeConHtmlPeligroso() throws Exception {
		mvc.perform(post("/mensajes").with(como("usuario1")).with(csrf())
			.param("beneficiario", "3").param("asunto", "<script>alert(1)</script>").param("cuerpo", "x"))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("form", "asunto"));
	}

	// ReservaForm ---------------------------------------------------------------

	@Test
	void reservar() throws Exception {
		mvc.perform(post("/negocios/1/reservas").with(como("usuario1")).with(csrf())
			.param("fecha", dentroDeDias(5, 14)).param("comensales", "2").param("fumadores", "false").param("comentarios", "Junto al mar"))
			.andExpect(redirectedUrl("/mis/reservas"));
		assertEquals(1, contar("select count(*) from reserva where comentarios = 'Junto al mar' and negocio_id = 1"));
	}

	@Test
	void reservarFueraDeHorario() throws Exception {
		mvc.perform(post("/negocios/1/reservas").with(como("usuario1")).with(csrf())
			.param("fecha", dentroDeDias(5, 9)).param("comensales", "2").param("fumadores", "false").param("comentarios", "Pronto"))
			.andExpect(status().isOk()).andExpect(model().attribute("message", "booking.commit.error"));
		assertEquals(0, contar("select count(*) from reserva where comentarios = 'Pronto'"));
	}

	@Test
	void cancelarReserva() throws Exception {
		mvc.perform(post("/negocios/1/reservas").with(como("usuario1")).with(csrf())
			.param("fecha", dentroDeDias(5, 14)).param("comensales", "2").param("fumadores", "false").param("comentarios", "Borrar"));
		Integer id = jdbc().queryForObject("select id from reserva where comentarios = 'Borrar'", Integer.class);

		mvc.perform(post("/mis/reservas/" + id).with(como("usuario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/mis/reservas"));
		assertEquals(0, contar("select count(*) from reserva where id = ?", id));
	}

	// ValoracionPlayaForm y ValoracionNegocioForm -------------------------------

	@Test
	void valorarPlaya() throws Exception {
		mvc.perform(post("/playas/3/valoraciones").with(como("usuario1")).with(csrf())
			.param("titulo", "Preciosa").param("comentario", "Agua limpia").param("puntuacion", "9"))
			.andExpect(redirectedUrl("/mis/valoraciones/playas"));
		assertEquals(1, contar("select count(*) from valoracionplaya where playa_id = 3 and usuario_id = 10 and puntuacion = 9"));
	}

	@Test
	void valorarPlayaFueraDeRango() throws Exception {
		mvc.perform(post("/playas/3/valoraciones").with(como("usuario1")).with(csrf())
			.param("titulo", "Mal").param("comentario", "x").param("puntuacion", "11"))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("form", "puntuacion"));
	}

	@Test
	void editarValoracionDeNegocio() throws Exception {
		mvc.perform(post("/mis/valoraciones/negocios/1").with(como("usuario1")).with(csrf()).param("_method", "put")
			.param("version", "0").param("titulo", "Cambiada").param("comentario", "Mejor de lo que dije").param("puntuacion", "7"))
			.andExpect(redirectedUrl("/mis/valoraciones/negocios"));
		assertEquals(1, contar("select count(*) from valoracionnegocio where id = 1 and titulo = 'Cambiada'"));
	}

	@Test
	void noSePuedeEditarLaValoracionDeOtro() throws Exception {
		mvc.perform(post("/mis/valoraciones/negocios/2").with(como("usuario1")).with(csrf()).param("_method", "put")
			.param("version", "0").param("titulo", "Hackeada").param("comentario", "x").param("puntuacion", "1"))
			.andExpect(status().isForbidden());
		assertEquals(0, contar("select count(*) from valoracionnegocio where titulo = 'Hackeada'"));
	}

	// DenunciaValoracionForm ----------------------------------------------------

	@Test
	void denunciarValoracion() throws Exception {
		mvc.perform(post("/empresario/valoraciones/1/denuncias").with(como("empresario1")).with(csrf())
			.param("titulo", "Falsa").param("comentario", "Nunca vino").param("tipo", "FALSEDAD"))
			.andExpect(redirectedUrl("/empresario/denuncias"));
		assertEquals(1, contar("select count(*) from denunciavaloracion where titulo = 'Falsa' and valoracionnegocio_id = 1"));
	}

	@Test
	void noSePuedeDenunciarLaValoracionDeOtroNegocio() throws Exception {
		// La valoración 3 es de un negocio de empresario2
		mvc.perform(post("/empresario/valoraciones/3/denuncias").with(como("empresario1")).with(csrf())
			.param("titulo", "Falsa").param("comentario", "x").param("tipo", "FALSEDAD"))
			.andExpect(status().isForbidden());
	}

	// NegocioForm -----------------------------------------------------------------

	@Test
	void crearNegocio() throws Exception {
		mvc.perform(post("/empresario/negocios").with(como("empresario1")).with(csrf())
			.param("nombre", "Chiringuito Nuevo").param("tipo", "Chiringuito").param("descripcion", "Pescaíto")
			.param("telefono", "+34956000000").param("paginaWeb", "https://example.com").param("aforo", "40").param("playa", "3"))
			.andExpect(redirectedUrl("/empresario/negocios"));
		assertEquals(1, contar("select count(*) from negocio where nombre = 'Chiringuito Nuevo' and empresario_id = 3 and playa_id = 3"));
	}

	@Test
	void crearNegocioConAforoExcesivo() throws Exception {
		mvc.perform(post("/empresario/negocios").with(como("empresario1")).with(csrf())
			.param("nombre", "Grande").param("tipo", "Bar").param("descripcion", "x")
			.param("telefono", "+34956000000").param("aforo", "501").param("playa", "3"))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("form", "aforo"));
	}

	@Test
	void suspenderYReactivarNegocio() throws Exception {
		mvc.perform(post("/empresario/negocios/1/suspension").with(como("empresario1")).with(csrf()))
			.andExpect(redirectedUrl("/empresario/negocios"));
		assertEquals(1, contar("select count(*) from negocio where id = 1 and not negocioactivo"));

		mvc.perform(post("/empresario/negocios/1/suspension").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/negocios"));
		assertEquals(1, contar("select count(*) from negocio where id = 1 and negocioactivo"));
	}

	// EventoForm e inscripción ---------------------------------------------------

	@Test
	void crearEventoEInscribirse() throws Exception {
		mvc.perform(post("/empresario/eventos").with(como("empresario1")).with(csrf())
			.param("nombre", "Noche de flamenco").param("descripcion", "En la arena").param("fechaCelebracion", dentroDeDias(10, 22))
			.param("precio", "5").param("negocio", "1"))
			.andExpect(redirectedUrl("/empresario/eventos"));
		Integer id = jdbc().queryForObject("select id from evento where nombre = 'Noche de flamenco'", Integer.class);

		mvc.perform(post("/eventos/" + id + "/inscripcion").with(como("usuario1")).with(csrf()))
			.andExpect(status().is3xxRedirection());
		assertEquals(1, contar("select count(*) from evento_usuario where eventos_id = ? and usuarios_id = 10", id));

		mvc.perform(post("/eventos/" + id + "/inscripcion").with(como("usuario1")).with(csrf()).param("_method", "delete"))
			.andExpect(status().is3xxRedirection());
		assertEquals(0, contar("select count(*) from evento_usuario where eventos_id = ? and usuarios_id = 10", id));
	}

	@Test
	void crearEventoEnNegocioAjeno() throws Exception {
		mvc.perform(post("/empresario/eventos").with(como("empresario1")).with(csrf())
			.param("nombre", "Intruso").param("descripcion", "x").param("fechaCelebracion", dentroDeDias(10, 22))
			.param("precio", "5").param("negocio", "2"))
			.andExpect(status().isForbidden());
		assertEquals(0, contar("select count(*) from evento where nombre = 'Intruso'"));
	}

	// PeticionNegocioForm y PeticionNegocioResolucionForm --------------------------

	@Test
	void enviarPeticionDeNegocio() throws Exception {
		mvc.perform(post("/empresario/peticion").with(como("empresario7")).with(csrf()).param("titulo", "Quiero abrir un bar"))
			.andExpect(redirectedUrl("/empresario/peticion"));
		assertEquals(1, contar("select count(*) from peticionnegocio where titulo = 'Quiero abrir un bar' and estado = 'PENDIENTE'"));
	}

	@Test
	void aceptarPeticion() throws Exception {
		mvc.perform(post("/admin/peticiones/3/aceptacion").with(como("admin")).with(csrf()).param("version", "0").param("comentarios", "Adelante"))
			.andExpect(redirectedUrl("/admin/peticiones"));
		assertEquals(1, contar("select count(*) from peticionnegocio where id = 3 and estado = 'ACEPTADO' and comentarios = 'Adelante'"));
	}

	@Test
	void rechazarPeticion() throws Exception {
		mvc.perform(post("/admin/peticiones/6/rechazo").with(como("admin")).with(csrf()).param("version", "0").param("comentarios", "No"))
			.andExpect(redirectedUrl("/admin/peticiones"));
		assertEquals(1, contar("select count(*) from peticionnegocio where id = 6 and estado = 'RECHAZADO'"));
	}

	// PlayaForm y PlayaCoordinatesForm ---------------------------------------------

	@Test
	void crearPlaya() throws Exception {
		mvc.perform(post("/admin/playas").with(como("admin")).with(csrf())
			.param("nombre", "Playa Nueva").param("descripcion", "Arena fina").param("composicion", "Arena").param("servicios", "Duchas")
			.param("extension", "800").param("localizacion.via", "Paseo").param("localizacion.nombreVia", "Marítimo")
			.param("localizacion.numeroVia", "1").param("localizacion.codigoPostal", "11520").param("localizacion.ciudad", "Rota")
			.param("localizacion.provincia", "Cádiz").param("localizacion.latitud", "36.62").param("localizacion.longitud", "-6.36"))
			.andExpect(redirectedUrl("/playas"));
		assertEquals(1, contar("select count(*) from playa where nombre = 'Playa Nueva' and ciudad = 'Rota' and administrador_id = 1"));
	}

	@Test
	void editarPlayaNoCambiaLasCoordenadas() throws Exception {
		mvc.perform(post("/admin/playas/3").with(como("admin")).with(csrf()).param("_method", "put").param("version", "0")
			.param("nombre", "Calas renombradas").param("descripcion", "d").param("composicion", "c").param("servicios", "s")
			.param("extension", "10").param("localizacion.via", "Calle").param("localizacion.nombreVia", "Mar")
			.param("localizacion.numeroVia", "1").param("localizacion.codigoPostal", "11520").param("localizacion.ciudad", "Rota")
			.param("localizacion.provincia", "Cádiz").param("localizacion.latitud", "0").param("localizacion.longitud", "0"))
			.andExpect(redirectedUrl("/playas"));
		assertEquals(1, contar("select count(*) from playa where id = 3 and nombre = 'Calas renombradas' and latitud <> 0"));
	}

	@Test
	void ubicacionFueraDeRango() throws Exception {
		mvc.perform(post("/admin/playas/3/ubicacion").with(como("admin")).with(csrf()).param("latitud", "120").param("longitud", "0"))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("form", "latitud"));
	}

	// Subida de imágenes ----------------------------------------------------------

	@Test
	void subirFotoDePerfil() throws Exception {
		byte[] png = { (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1a, '\n', 0, 0, 0, 0 };

		mvc.perform(multipart("/perfil/foto").file(new MockMultipartFile("foto", "yo.png", "image/png", png)).with(como("usuario1")).with(csrf()))
			.andExpect(redirectedUrl("/perfil"));
		assertEquals(1, contar("select count(*) from usuario u join imagen i on i.id = u.imagen_id where u.id = 10 and i.tipo = 'image/png'"));
	}

	@Test
	void subirAlgoQueNoEsUnaImagen() throws Exception {
		mvc.perform(multipart("/perfil/foto").file(new MockMultipartFile("foto", "x.png", "image/png", "<html>".getBytes())).with(como("usuario1")).with(csrf()))
			.andExpect(status().isOk()).andExpect(model().attribute("message", "fase5.imagen.formato"));
	}

}
