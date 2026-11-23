/* ApiTest.java
 *
 * Fase 8: API REST del móvil (/api/v1) con JWT.
 */
package web;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.jayway.jsonpath.JsonPath;

class ApiTest extends AbstractWebTest {

	@org.springframework.beans.factory.annotation.Autowired
	private api.LimiteIntentos limite;

	@org.junit.jupiter.api.BeforeEach
	void sinBloqueos() {
		limite.olvidar();
	}

	private ResultActions postJson(String url, String json, String token) throws Exception {
		var peticion = post(url).contentType(MediaType.APPLICATION_JSON).content(json);
		if (token != null)
			peticion.header("Authorization", "Bearer " + token);
		return mvc.perform(peticion);
	}

	private String login(String usuario) throws Exception {
		String respuesta = postJson("/api/v1/auth/login", "{\"username\":\"" + usuario + "\",\"password\":\"" + usuario + "\"}", null)
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		return JsonPath.read(respuesta, "$.accessToken");
	}

	private static String dentroDeDias(int dias, int hora) {
		return LocalDate.now().plusDays(dias).atTime(hora, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	// Autenticación -------------------------------------------------------------

	@Test
	void loginDevuelveTokens() throws Exception {
		postJson("/api/v1/auth/login", "{\"username\":\"usuario1\",\"password\":\"usuario1\"}", null)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").isString())
			.andExpect(jsonPath("$.refreshToken").isString())
			.andExpect(jsonPath("$.expiraEn").value(900))
			.andExpect(jsonPath("$.tipo").value("Bearer"));
	}

	@Test
	void loginMalDa401EnProblemJson() throws Exception {
		postJson("/api/v1/auth/login", "{\"username\":\"usuario1\",\"password\":\"mal\"}", null)
			.andExpect(status().isUnauthorized())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
			.andExpect(jsonPath("$.status").value(401));
	}

	@Test
	void cincoFallosBloqueanElLogin() throws Exception {
		for (int i = 0; i < 5; i++)
			postJson("/api/v1/auth/login", "{\"username\":\"usuario2\",\"password\":\"mal\"}", null).andExpect(status().isUnauthorized());
		// Ni siquiera con la contraseña buena
		postJson("/api/v1/auth/login", "{\"username\":\"usuario2\",\"password\":\"usuario2\"}", null).andExpect(status().isTooManyRequests());
	}

	@Test
	void elTokenDeRenovacionRotaYNoSeReutiliza() throws Exception {
		String r1 = JsonPath.read(postJson("/api/v1/auth/login", "{\"username\":\"usuario1\",\"password\":\"usuario1\"}", null)
			.andReturn().getResponse().getContentAsString(), "$.refreshToken");

		String r2 = JsonPath.read(postJson("/api/v1/auth/refresh", "{\"refreshToken\":\"" + r1 + "\"}", null)
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), "$.refreshToken");
		assertNotEquals(r1, r2);

		// Reutilizar el primero (como haría quien lo robara) anula también el segundo
		postJson("/api/v1/auth/refresh", "{\"refreshToken\":\"" + r1 + "\"}", null).andExpect(status().isUnauthorized());
		postJson("/api/v1/auth/refresh", "{\"refreshToken\":\"" + r2 + "\"}", null).andExpect(status().isUnauthorized());
		// En la base de datos solo hay hashes
		assertEquals(0, contar("select count(*) from tokenrefresco where hash = ? or hash = ?", r1, r2));
	}

	@Test
	void sinTokenOConUnoFalsoDa401() throws Exception {
		mvc.perform(get("/api/v1/yo")).andExpect(status().isUnauthorized()).andExpect(header().string("WWW-Authenticate", "Bearer"));
		mvc.perform(get("/api/v1/yo").header("Authorization", "Bearer no.es.un.jwt")).andExpect(status().isUnauthorized());
	}

	@Test
	void laApiNoUsaCookiesNiCsrf() throws Exception {
		String token = login("usuario1");
		mvc.perform(get("/api/v1/yo").header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(header().doesNotExist("Set-Cookie"));
	}

	// Consulta pública ------------------------------------------------------------

	@Test
	void consultaPublica() throws Exception {
		mvc.perform(get("/api/v1/playas")).andExpect(status().isOk())
			.andExpect(jsonPath("$.pagina").value(1)).andExpect(jsonPath("$.contenido.length()", greaterThan(0)));
		mvc.perform(get("/api/v1/playas/1")).andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Matalascañas"));
		mvc.perform(get("/api/v1/playas/999999")).andExpect(status().isNotFound());
		mvc.perform(get("/api/v1/playas/cercanas").param("lat", "36.7").param("lon", "-6.4").param("radio", "50")).andExpect(status().isOk());
		mvc.perform(get("/api/v1/negocios").param("ciudad", "chip")).andExpect(status().isOk());
		mvc.perform(get("/api/v1/negocios/1")).andExpect(status().isOk()).andExpect(jsonPath("$.horaApertura").value("12:00:00"));
		mvc.perform(get("/api/v1/negocios/1/valoraciones")).andExpect(status().isOk());
		mvc.perform(get("/api/v1/negocios/1/disponibilidad").param("dia", LocalDate.now().plusDays(3).toString()))
			.andExpect(status().isOk()).andExpect(jsonPath("$.franjas", hasSize(24)));
		mvc.perform(get("/api/v1/eventos").param("cuando", "proximos")).andExpect(status().isOk());
		mvc.perform(get("/api/v1/eventos/2")).andExpect(status().isOk()).andExpect(jsonPath("$.inscrito").value(false));
	}

	// Usuario ---------------------------------------------------------------------

	@Test
	void perfilConNivelDeColaboracion() throws Exception {
		mvc.perform(get("/api/v1/yo").header("Authorization", "Bearer " + login("usuario1")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value("usuario1"))
			.andExpect(jsonPath("$.rol").value("USUARIO"))
			.andExpect(jsonPath("$.colaboracion.puntos").value(25));
	}

	@Test
	void reservarYCancelar() throws Exception {
		String token = login("usuario1");
		String json = postJson("/api/v1/yo/reservas",
			"{\"negocioId\":1,\"fecha\":\"" + dentroDeDias(5, 14) + "\",\"comensales\":2,\"comentarios\":\"Desde el móvil\"}", token)
			.andExpect(status().isCreated()).andExpect(header().exists("Location"))
			.andExpect(jsonPath("$.negocio").value("Comida Fast")).andReturn().getResponse().getContentAsString();
		int id = JsonPath.read(json, "$.id");

		mvc.perform(delete("/api/v1/yo/reservas/" + id).header("Authorization", "Bearer " + token)).andExpect(status().isNoContent());
		assertEquals(0, contar("select count(*) from reserva where id = ?", id));
	}

	@Test
	void unaReservaQueNoCabeDa409ConElMotivo() throws Exception {
		postJson("/api/v1/yo/reservas", "{\"negocioId\":1,\"fecha\":\"" + dentroDeDias(5, 9) + "\",\"comensales\":2}", login("usuario1"))
			.andExpect(status().isConflict()).andExpect(jsonPath("$.motivo").value("FUERA_DE_HORARIO"));
	}

	@Test
	void datosMalDan400ConLosCampos() throws Exception {
		postJson("/api/v1/yo/reservas", "{\"negocioId\":1,\"comensales\":40}", login("usuario1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errores.fecha").exists())
			.andExpect(jsonPath("$.errores.comensales").exists());
	}

	@Test
	void unEmpresarioNoReservaPorLaApi() throws Exception {
		postJson("/api/v1/yo/reservas", "{\"negocioId\":1,\"fecha\":\"" + dentroDeDias(5, 14) + "\",\"comensales\":2}", login("empresario1"))
			.andExpect(status().isForbidden());
	}

	@Test
	void valorarPlayaYApuntarseAUnEvento() throws Exception {
		String token = login("usuario2");
		postJson("/api/v1/playas/3/valoraciones", "{\"titulo\":\"Genial\",\"comentario\":\"Agua limpia\",\"puntuacion\":9}", token)
			.andExpect(status().isCreated()).andExpect(jsonPath("$.puntuacion").value(9));

		postJson("/api/v1/eventos/2/inscripcion", "{}", token).andExpect(status().isNoContent());
		mvc.perform(get("/api/v1/eventos/2").header("Authorization", "Bearer " + token)).andExpect(jsonPath("$.inscrito").value(true));
		mvc.perform(get("/api/v1/yo/eventos").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
	}

	@Test
	void enviarYLeerMensajes() throws Exception {
		String token = login("usuario1");
		postJson("/api/v1/yo/mensajes", "{\"beneficiarioId\":3,\"asunto\":\"Hola\",\"cuerpo\":\"Desde la app\"}", token)
			.andExpect(status().isCreated());

		mvc.perform(get("/api/v1/yo/mensajes").param("carpeta", "enviados").header("Authorization", "Bearer " + token))
			.andExpect(status().isOk()).andExpect(jsonPath("$[0].asunto").value("Hola")).andExpect(jsonPath("$[0].cuerpo").doesNotExist());
	}

	@Test
	void nadieLeeElMensajeDeOtro() throws Exception {
		// usuario2 escribe a otra persona; usuario1 no puede leer ninguna de las dos copias
		postJson("/api/v1/yo/mensajes", "{\"beneficiarioId\":3,\"asunto\":\"Privado\",\"cuerpo\":\"x\"}", login("usuario2")).andExpect(status().isCreated());
		Integer ajeno = jdbc().queryForObject("select min(id) from mensaje where asunto = 'Privado'", Integer.class);
		mvc.perform(get("/api/v1/yo/mensajes/" + ajeno).header("Authorization", "Bearer " + login("usuario1"))).andExpect(status().isForbidden());
	}

	@Test
	void borrarLaCuentaDesconectaElMovil() throws Exception {
		String json = postJson("/api/v1/auth/login", "{\"username\":\"usuario1\",\"password\":\"usuario1\"}", null)
			.andReturn().getResponse().getContentAsString();
		String acceso = JsonPath.read(json, "$.accessToken");
		String renovacion = JsonPath.read(json, "$.refreshToken");

		mvc.perform(post("/perfil").with(como("usuario1")).with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
			.param("_method", "delete").param("password", "usuario1"));

		mvc.perform(get("/api/v1/yo").header("Authorization", "Bearer " + acceso)).andExpect(status().isUnauthorized());
		postJson("/api/v1/auth/refresh", "{\"refreshToken\":\"" + renovacion + "\"}", null).andExpect(status().isUnauthorized());
	}

	@Test
	void laDocumentacionOpenApiEsPublica() throws Exception {
		mvc.perform(get("/api/docs/openapi.json")).andExpect(status().isOk())
			.andExpect(jsonPath("$.info.title").value("Summermate API"))
			.andExpect(jsonPath("$.paths['/api/v1/yo/reservas']").exists())
			.andExpect(jsonPath("$.paths['/playas']").doesNotExist());
		String destino = mvc.perform(get("/api/docs")).andExpect(status().is3xxRedirection()).andReturn().getResponse().getRedirectedUrl();
		mvc.perform(get(destino)).andExpect(status().isOk());
	}
}
