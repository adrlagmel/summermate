/* ImportacionPlayasWebTest.java
 *
 * Fase 6: importar playas desde un CSV con nombres de columna variables.
 */
package web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import services.ImportadorPlayas;

class ImportacionPlayasWebTest extends AbstractWebTest {

	private ImportadorPlayas.Resultado importar(byte[] csv, String usuario) throws Exception {
		MvcResult r = mvc.perform(multipart("/admin/playas/importacion").file(new MockMultipartFile("fichero", "playas.csv", "text/csv", csv))
			.with(como(usuario)).with(csrf()))
			.andExpect(status().isOk()).andReturn();
		return (ImportadorPlayas.Resultado) r.getModelAndView().getModel().get("resultado");
	}

	@Test
	void importaElFicheroDeEjemplo() throws Exception {
		int antes = contar("select count(*) from playa");
		ImportadorPlayas.Resultado resultado = importar(Files.readAllBytes(Path.of("scripts/playas-ejemplo.csv")), "admin");

		// 6 filas: 5 buenas y una sin coordenadas
		assertEquals(5, resultado.creadas());
		assertEquals(1, resultado.omitidas().size());
		assertEquals(antes + 5, contar("select count(*) from playa"));
		assertEquals(1, contar("select count(*) from playa where nombre = 'Playa de Bolonia' and ciudad = 'Tarifa' and extension = 3800 and codigopostal = 11001"));
		assertEquals(1, contar("select count(*) from playa where nombre = 'Playa de El Palmar' and descripcion = 'Surf; puestas de sol'"));

		// La segunda vez no duplica nada
		ImportadorPlayas.Resultado otraVez = importar(Files.readAllBytes(Path.of("scripts/playas-ejemplo.csv")), "admin");
		assertEquals(0, otraVez.creadas());
		assertTrue(otraVez.omitidas().stream().filter(o -> o.motivo().equals("ya existe")).count() == 5);
	}

	@Test
	void aceptaComasYLatin1() throws Exception {
		String csv = "nombre_playa,municipio,provincia,lat,lon,codigo_postal\nCala Mondragó,Santanyí,Illes Balears,39.3510,3.1890,07691\n";
		ImportadorPlayas.Resultado resultado = importar(csv.getBytes(StandardCharsets.ISO_8859_1), "admin");

		assertEquals(1, resultado.creadas());
		assertEquals(1, contar("select count(*) from playa where nombre = 'Cala Mondragó' and codigopostal = 7691"));
	}

	@Test
	void sinColumnasObligatoriasNoImportaNada() throws Exception {
		ImportadorPlayas.Resultado resultado = importar("titulo;sitio\nA;B\n".getBytes(StandardCharsets.UTF_8), "admin");
		assertEquals(0, resultado.creadas());
		assertTrue(resultado.omitidas().get(0).motivo().startsWith("faltan columnas"));
	}

	@Test
	void soloElAdministradorImporta() throws Exception {
		mvc.perform(multipart("/admin/playas/importacion").file(new MockMultipartFile("fichero", "p.csv", "text/csv", "x".getBytes()))
			.with(como("empresario1")).with(csrf()))
			.andExpect(status().isForbidden());
	}
}
