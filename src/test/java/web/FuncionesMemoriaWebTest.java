/* FuncionesMemoriaWebTest.java
 *
 * Fase 6: funciones descritas en la memoria del TFG que no estaban hechas.
 */
package web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

class FuncionesMemoriaWebTest extends AbstractWebTest {

	private static String dentroDeDias(int dias, int hora) {
		return LocalDate.now().plusDays(dias).atTime(hora, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	private Integer id(String sql) {
		return jdbc().queryForObject(sql, Integer.class);
	}

	// Eliminar evento (UC0004) --------------------------------------------------

	@Test
	void borrarEventoSinInscritos() throws Exception {
		mvc.perform(post("/empresario/eventos").with(como("empresario1")).with(csrf())
			.param("nombre", "Efímero").param("descripcion", "x").param("fechaCelebracion", dentroDeDias(10, 22))
			.param("precio", "0").param("negocio", "1"));
		Integer evento = id("select id from evento where nombre = 'Efímero'");

		mvc.perform(post("/empresario/eventos/" + evento).with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/eventos")).andExpect(flash().attribute("aviso", "fase5.borrado"));
		assertEquals(0, contar("select count(*) from evento where id = ?", evento));
	}

	@Test
	void noSeBorraUnEventoConInscritos() throws Exception {
		mvc.perform(post("/empresario/eventos/1").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/eventos")).andExpect(flash().attribute("message", "evento.borrar.noSePuede"));
		assertEquals(1, contar("select count(*) from evento where id = 1"));
	}

	@Test
	void noSeBorraUnEventoAjeno() throws Exception {
		mvc.perform(post("/empresario/eventos/2").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(status().isForbidden());
	}

	// Eliminar negocio (UC0009) -------------------------------------------------

	@Test
	void borrarNegocioSinEventosNiReservas() throws Exception {
		mvc.perform(post("/empresario/negocios").with(como("empresario1")).with(csrf())
			.param("nombre", "Temporal").param("tipo", "Bar").param("descripcion", "x")
			.param("telefono", "+34956000000").param("aforo", "40").param("playa", "3"));
		Integer negocio = id("select id from negocio where nombre = 'Temporal'");

		mvc.perform(post("/empresario/negocios/" + negocio).with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/negocios")).andExpect(flash().attribute("aviso", "fase5.borrado"));
		assertEquals(0, contar("select count(*) from negocio where id = ?", negocio));
	}

	@Test
	void noSeBorraUnNegocioConEventos() throws Exception {
		mvc.perform(post("/empresario/negocios/1").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/negocios")).andExpect(flash().attribute("message", "negocio.borrar.noSePuede"));
		assertEquals(1, contar("select count(*) from negocio where id = 1"));
	}

	// Cancelar petición -------------------------------------------------------

	@Test
	void cancelarUnaPeticionPendiente() throws Exception {
		// empresario3 tiene la petición 3, pendiente
		mvc.perform(post("/empresario/peticion").with(como("empresario3")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/peticion")).andExpect(flash().attribute("aviso", "peticion.cancelada"));
		assertEquals(0, contar("select count(*) from peticionnegocio where id = 3"));
	}

	@Test
	void noSeCancelaUnaPeticionAceptada() throws Exception {
		mvc.perform(post("/empresario/peticion").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(status().isForbidden());
		assertEquals(1, contar("select count(*) from peticionnegocio where id = 1"));
	}
}
