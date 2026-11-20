/* RgpdWebTest.java
 *
 * Fase 6: derechos del RGPD que el cliente ejerce desde su perfil.
 */
package web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

class RgpdWebTest extends AbstractWebTest {

	@Test
	void descargarMisDatos() throws Exception {
		mvc.perform(get("/perfil/datos").with(como("usuario1")))
			.andExpect(status().isOk())
			.andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("attachment")))
			.andExpect(jsonPath("$.usuario").value("usuario1"))
			.andExpect(jsonPath("$.perfil.email").exists())
			.andExpect(jsonPath("$.reservas").isArray())
			.andExpect(jsonPath("$.valoracionesDePlayas").isArray());
	}

	@Test
	void conLaContrasenaMalNoSeBorra() throws Exception {
		mvc.perform(post("/perfil").with(como("usuario1")).with(csrf()).param("_method", "delete").param("password", "otra"))
			.andExpect(status().isOk()).andExpect(model().attribute("message", "baja.contrasenaMal"));
		assertEquals(1, contar("select count(*) from useraccount where username = 'usuario1'"));
	}

	@Test
	void borrarLaCuentaAnonimizaYNoDejaEntrar() throws Exception {
		// usuario1 (actor 10) tiene una valoración de playa y una de negocio
		mvc.perform(post("/perfil").with(como("usuario1")).with(csrf()).param("_method", "delete").param("password", "usuario1"))
			.andExpect(redirectedUrl("/"));

		assertEquals(0, contar("select count(*) from useraccount where username = 'usuario1'"));
		assertEquals(1, contar("select count(*) from actor where id = 10 and nombre = 'Usuario' and email like 'borrado-%'"));
		assertEquals(0, contar("select count(*) from mensaje m join carpeta c on c.id = m.carpeta_id where c.actor_id = 10"));
		// Las valoraciones se quedan (anónimas)
		assertEquals(1, contar("select count(*) from valoracionplaya where usuario_id = 10"));

		mvc.perform(formLogin("/login").user("usuario1").password("usuario1"))
			.andExpect(redirectedUrl("/login?error"));
	}

	@Test
	void unEmpresarioQueSeDaDeBajaDejaSusNegociosSuspendidos() throws Exception {
		mvc.perform(post("/perfil").with(como("empresario1")).with(csrf()).param("_method", "delete").param("password", "empresario1"))
			.andExpect(redirectedUrl("/"));
		assertEquals(0, contar("select count(*) from negocio where empresario_id = 3 and negocioactivo"));
	}

	@Test
	void elAdministradorNoSeDaDeBajaAsi() throws Exception {
		mvc.perform(post("/perfil").with(como("admin")).with(csrf()).param("_method", "delete").param("password", "admin"))
			.andExpect(status().isForbidden());
	}
}
