/* AvisosCorreoWebTest.java
 *
 * Fase 6: avisos por correo. En los tests no se envía nada: el Cartero
 * guarda lo que habría enviado.
 */
package web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import services.correo.Cartero;
import services.correo.Correo;

class AvisosCorreoWebTest extends AbstractWebTest {

	@Autowired
	private Cartero cartero;

	@BeforeEach
	void vaciar() {
		cartero.enviados().clear();
	}

	private List<Correo> para(String email) {
		return cartero.enviados().stream().filter(c -> c.para().equals(email)).toList();
	}

	private String email(String username) {
		return jdbc().queryForObject("select a.email from actor a join useraccount u on u.id = a.useraccount_id where u.username = ?", String.class, username);
	}

	@Test
	void unaReservaAvisaAlUsuario() throws Exception {
		String fecha = LocalDate.now().plusDays(5).atTime(14, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		mvc.perform(post("/negocios/1/reservas").with(como("usuario1")).with(csrf())
			.param("fecha", fecha).param("comensales", "2").param("fumadores", "false").param("comentarios", "Con aviso"));

		List<Correo> correos = para(email("usuario1"));
		assertEquals(1, correos.size());
		assertTrue(correos.get(0).asunto().contains("Comida Fast"));
	}

	@Test
	void unaReservaRechazadaNoAvisa() throws Exception {
		String fecha = LocalDate.now().plusDays(5).atTime(9, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		mvc.perform(post("/negocios/1/reservas").with(como("usuario1")).with(csrf())
			.param("fecha", fecha).param("comensales", "2").param("fumadores", "false").param("comentarios", "Sin aviso"));

		assertEquals(0, cartero.enviados().size());
	}

	@Test
	void unMensajeAvisaAlDestinatario() throws Exception {
		mvc.perform(post("/mensajes").with(como("usuario1")).with(csrf())
			.param("beneficiario", "3").param("asunto", "Hola").param("cuerpo", "Secreto"));

		String destino = jdbc().queryForObject("select email from actor where id = 3", String.class);
		List<Correo> correos = para(destino);
		assertEquals(1, correos.size());
		// El aviso no copia el texto del mensaje
		assertTrue(!correos.get(0).cuerpo().contains("Secreto"));
	}

	@Test
	void resolverUnaPeticionAvisaAlEmpresario() throws Exception {
		// La petición 3 está pendiente y sin administrador asignado
		mvc.perform(post("/admin/peticiones/3/aceptacion").with(como("admin")).with(csrf()).param("comentarios", "Bienvenido").param("version", "0"));

		String empresario = jdbc().queryForObject("select a.email from peticionnegocio p join actor a on a.id = p.empresario_id where p.id = 3", String.class);
		assertEquals(1, para(empresario).size());
	}
}
