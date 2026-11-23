/* ImagenesEnR2Test.java
 *
 * Fase 8: imágenes en un almacén compatible con S3 (Cloudflare R2 en
 * producción). En el test, S3Mock de Adobe hace de R2.
 */
package web;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import services.imagenes.AlmacenImagenes;
import services.imagenes.MigradorImagenes;

class ImagenesEnR2Test extends AbstractWebTest {

	static final String						CUBO	= "summermate-imagenes";

	@SuppressWarnings("resource")
	static final GenericContainer<?>		S3		= new GenericContainer<>("adobe/s3mock:4.7.0")
		.withEnv("COM_ADOBE_TESTING_S3MOCK_STORE_INITIAL_BUCKETS", CUBO)
		.withExposedPorts(9090)
		.waitingFor(Wait.forLogMessage(".*Started S3MockApplication.*", 1));

	static {
		S3.start();
	}

	@DynamicPropertySource
	static void almacen(DynamicPropertyRegistry r) {
		r.add("summermate.imagenes.almacen", () -> "s3");
		r.add("summermate.imagenes.s3.endpoint", () -> "http://" + S3.getHost() + ":" + S3.getMappedPort(9090));
		r.add("summermate.imagenes.s3.bucket", () -> CUBO);
		r.add("summermate.imagenes.s3.region", () -> "us-east-1");
		r.add("summermate.imagenes.s3.access-key", () -> "prueba");
		r.add("summermate.imagenes.s3.secret-key", () -> "prueba");
	}

	private static final byte[]	PNG	= { (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1a, '\n', 1, 2, 3, 4 };
	private static final byte[]	PNG2	= { (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1a, '\n', 9, 9, 9, 9 };

	@Autowired
	private AlmacenImagenes		almacen;

	@Autowired
	private EntityManager		em;

	@Autowired
	private TransactionTemplate	tx;


	private void subirFoto(byte[] png) throws Exception {
		mvc.perform(multipart("/perfil/foto").file(new MockMultipartFile("foto", "yo.png", "image/png", png)).with(como("usuario1")).with(csrf()))
			.andExpect(status().is3xxRedirection());
	}

	private String claveDeUsuario1() {
		return jdbc().queryForObject("select i.clave from usuario u join imagen i on i.id = u.imagen_id where u.id = 10", String.class);
	}

	@Test
	void laFotoSeSubeAlAlmacenYSeSirveDesdeAhi() throws Exception {
		subirFoto(PNG);

		String clave = claveDeUsuario1();
		assertEquals(1, contar("select count(*) from imagen where clave = ? and datos is null", clave));
		assertArrayEquals(PNG, almacen.leer(clave));

		mvc.perform(get("/imagenes/usuarios/10")).andExpect(status().isOk()).andExpect(content().bytes(PNG));
	}

	@Test
	void cambiarLaFotoBorraLaAnteriorDelAlmacen() throws Exception {
		subirFoto(PNG);
		String antigua = claveDeUsuario1();
		subirFoto(PNG2);

		assertArrayEquals(PNG2, almacen.leer(claveDeUsuario1()));
		assertThrows(RuntimeException.class, () -> almacen.leer(antigua));
	}

	@Test
	void elMigradorPasaAlAlmacenLasImagenesDeLaBaseDeDatos() {
		Integer id = tx.execute(s -> {
			domain.Imagen imagen = new domain.Imagen(PNG, "image/png");
			em.persist(imagen);
			return imagen.getId();
		});

		int movidas = new MigradorImagenes(em, almacen, tx).migrar(List.of(id));

		assertEquals(1, movidas);
		String clave = jdbc().queryForObject("select clave from imagen where id = ?", String.class, id);
		assertNull(jdbc().queryForObject("select datos from imagen where id = ?", byte[].class, id));
		assertArrayEquals(PNG, almacen.leer(clave));
	}
}
