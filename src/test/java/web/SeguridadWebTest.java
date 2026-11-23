/* SeguridadWebTest.java
 *
 * Seguridad de la capa web: qué rutas ve cada rol, CSRF, login y logout, y
 * que el mapa no permite inyectar código con el nombre de una playa (S6).
 */

package web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SeguridadWebTest extends AbstractWebTest {

	// Rutas por rol ----------------------------------------------------------

	@ParameterizedTest
	@ValueSource(strings = { "/", "/login", "/terminos", "/playas", "/playas/1", "/playas/1/negocios", "/playas/cercanas", "/negocios", "/negocios/1",
		"/eventos", "/eventos/1", "/usuarios", "/registro/usuario", "/registro/empresario" })
	void paginasPublicasSinSesion(String ruta) throws Exception {
		mvc.perform(get(ruta)).andExpect(status().isOk());
	}

	// Antes, la lista de playas sin sesión daba un error 500
	@Test
	void listaDePlayasSinSesion() throws Exception {
		mvc.perform(get("/playas")).andExpect(status().isOk()).andExpect(content().string(containsString("Matalasca")));
	}

	@ParameterizedTest
	@ValueSource(strings = { "/mis/reservas", "/empresario/negocios", "/admin/dashboard", "/perfil", "/carpetas" })
	void zonasPrivadasLlevanAlLogin(String ruta) throws Exception {
		mvc.perform(get(ruta)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));
	}

	@Test
	void cadaRolSoloEntraEnSuZona() throws Exception {
		mvc.perform(get("/mis/reservas").with(como("usuario1"))).andExpect(status().isOk());
		mvc.perform(get("/empresario/negocios").with(como("usuario1"))).andExpect(status().isForbidden());
		mvc.perform(get("/admin/dashboard").with(como("usuario1"))).andExpect(status().isForbidden());

		mvc.perform(get("/empresario/negocios").with(como("empresario1"))).andExpect(status().isOk());
		mvc.perform(get("/mis/reservas").with(como("empresario1"))).andExpect(status().isForbidden());
		mvc.perform(get("/admin/dashboard").with(como("empresario1"))).andExpect(status().isForbidden());

		mvc.perform(get("/admin/dashboard").with(como("admin"))).andExpect(status().isOk());
		mvc.perform(get("/mis/reservas").with(como("admin"))).andExpect(status().isForbidden());
		mvc.perform(get("/empresario/negocios").with(como("admin"))).andExpect(status().isForbidden());
	}

	@Test
	void elRegistroEsSoloParaQuienNoTieneSesion() throws Exception {
		mvc.perform(get("/registro/usuario").with(como("usuario1"))).andExpect(status().isForbidden());
	}

	@Test
	void recursosAjenosDan403() throws Exception {
		mvc.perform(get("/empresario/negocios/2/editar").with(como("empresario1"))).andExpect(status().isForbidden());
		mvc.perform(get("/mis/valoraciones/playas/2/editar").with(como("usuario1"))).andExpect(status().isForbidden());
		mvc.perform(get("/carpetas/1").with(como("usuario1"))).andExpect(status().isForbidden());
		mvc.perform(get("/admin/playas/4/editar").with(como("admin"))).andExpect(status().isForbidden());
	}

	@Test
	void lasRutasAntiguasYaNoExisten() throws Exception {
		mvc.perform(get("/playa/list.do")).andExpect(status().is3xxRedirection());
		mvc.perform(get("/playa/coordenates/save.do?lat=1&lon=1&playaId=3").with(como("admin"))).andExpect(status().isForbidden());
	}

	// CSRF -------------------------------------------------------------------

	@Test
	void sinTokenCsrfNoSeCambiaNada() throws Exception {
		mvc.perform(post("/admin/playas/3/ubicacion").param("latitud", "37").param("longitud", "-6").with(como("admin")))
			.andExpect(status().isForbidden());
		assertEquals(0, contar("select count(*) from playa where id = 3 and latitud = 37"));

		mvc.perform(post("/mis/reservas/1").param("_method", "delete").with(como("usuario1"))).andExpect(status().isForbidden());
		assertEquals(1, contar("select count(*) from reserva where id = 1"));
	}

	// Ubicación de una playa: antes se guardaba con un GET
	@Test
	void ubicacionSeGuardaConPostYCsrf() throws Exception {
		mvc.perform(get("/admin/playas/3/ubicacion").with(como("admin"))).andExpect(status().isOk());
		mvc.perform(post("/admin/playas/3/ubicacion").param("latitud", "37.25").param("longitud", "-6.95").with(como("admin")).with(csrf()))
			.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/playas/3"));
		assertEquals(1, contar("select count(*) from playa where id = 3 and latitud = 37.25 and longitud = -6.95"));
	}

	@Test
	void ubicacionSoloDeLasPlayasPropiasYSinNegocios() throws Exception {
		// La playa 4 es de otro administrador
		mvc.perform(post("/admin/playas/4/ubicacion").param("latitud", "37").param("longitud", "-6").with(como("admin")).with(csrf()))
			.andExpect(status().isForbidden());
		// La playa 1 tiene negocios
		mvc.perform(post("/admin/playas/1/ubicacion").param("latitud", "37").param("longitud", "-6").with(como("admin")).with(csrf()))
			.andExpect(status().isForbidden());
		assertEquals(0, contar("select count(*) from playa where latitud = 37"));
	}

	// Login y logout ---------------------------------------------------------

	@Test
	void loginCorrecto() throws Exception {
		mvc.perform(formLogin("/login").user("usuario1").password("usuario1"))
			.andExpect(authenticated().withUsername("usuario1")).andExpect(redirectedUrl("/"));
	}

	@Test
	void loginIncorrecto() throws Exception {
		mvc.perform(formLogin("/login").user("usuario1").password("otra"))
			.andExpect(unauthenticated()).andExpect(redirectedUrl("/login?error"));
	}

	@Test
	void loginSinCsrfSeRechaza() throws Exception {
		mvc.perform(post("/login").param("username", "usuario1").param("password", "usuario1"))
			.andExpect(status().isForbidden()).andExpect(unauthenticated());
	}

	@Test
	void logoutPorPostConCsrf() throws Exception {
		mvc.perform(post("/logout").with(como("usuario1")).with(csrf()))
			.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/")).andExpect(unauthenticated());
	}

	@Test
	void logoutPorGetNoCierraLaSesion() throws Exception {
		mvc.perform(get("/logout").with(como("usuario1"))).andExpect(status().isForbidden());
	}

	// XSS en el mapa (S6) ------------------------------------------------------

	@Test
	void elNombreDeUnaPlayaNoPuedeInyectarCodigoEnElMapa() throws Exception {
		String malicioso = "</script><script>alert(1)</script>";

		jdbc().update("update playa set nombre = ? where id = 3", malicioso);
		mvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(content().string(not(containsString(malicioso))))
			.andExpect(content().string(containsString("\\/script")));
	}

}
