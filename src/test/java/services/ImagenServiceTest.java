package services;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import domain.Imagen;

/*
 * Test unitario de la detección del tipo de imagen por su contenido.
 */
public class ImagenServiceTest {

	// Sin almacén de objetos: las imágenes van a la base de datos
	private final ImagenService service = new ImagenService(new org.springframework.beans.factory.support.StaticListableBeanFactory().getBeanProvider(services.imagenes.AlmacenImagenes.class));

	@Test
	public void detectaJpegPngYWebp() {
		assertEquals("image/jpeg", ImagenService.detectarTipo(bytes(0xFF, 0xD8, 0xFF, 0xE0, 0, 0)));
		assertEquals("image/png", ImagenService.detectarTipo(bytes(0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0)));
		assertEquals("image/webp", ImagenService.detectarTipo(bytes('R', 'I', 'F', 'F', 1, 2, 3, 4, 'W', 'E', 'B', 'P')));
	}

	@Test
	public void unaImagenGuardaSuTipoYTamano() {
		Imagen imagen = service.crear(bytes(0xFF, 0xD8, 0xFF, 0xE0, 0, 0));

		assertEquals("image/jpeg", imagen.getTipo());
		assertEquals(6, imagen.getTamano());
	}

	@Test
	public void unaSubidaVaciaQuitaLaImagen() {
		assertNull(service.crear(new byte[0]));
		assertNull(service.crear(null));
	}

	// Un HTML con extensión .jpg ya no se acepta (ni se sirve como imagen).
	@Test
	public void rechazaFicherosQueNoSonImagenes() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.crear("<html><script>alert(1)</script></html>".getBytes());
	
		});
	}

	@Test
	public void rechazaFormatosNoAdmitidos() {
		assertThrows(IllegalArgumentException.class, () -> {
			service.crear("GIF89a......".getBytes());
	
		});
	}

	@Test
	public void unFicheroDemasiadoCortoNoEsUnaImagen() {
		assertNull(ImagenService.detectarTipo(bytes(0xFF, 0xD8)));
		assertNull(ImagenService.detectarTipo(bytes('R', 'I', 'F', 'F', 1, 2, 3, 4, 'W', 'A', 'V', 'E')));
	}

	private static byte[] bytes(int... valores) {
		byte[] result = new byte[valores.length];
		for (int i = 0; i < valores.length; i++)
			result[i] = (byte) valores[i];
		return result;
	}
}
