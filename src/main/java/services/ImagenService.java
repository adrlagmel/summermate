/* ImagenService.java
 *
 * Crea imágenes a partir de lo que sube el usuario. El tipo se deduce de los
 * primeros bytes del fichero ("magic numbers"), no del nombre ni de la
 * cabecera Content-Type, que controla el navegador. Antes se aceptaba
 * cualquier fichero y se servía siempre como image/jpeg.
 */

package services;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Imagen;

@Service
public class ImagenService {

	public static final String	JPEG	= "image/jpeg";
	public static final String	PNG		= "image/png";
	public static final String	WEBP	= "image/webp";


	// Devuelve null si no se ha subido nada (así se borraba la imagen antes).
	// Lanza IllegalArgumentException si el fichero no es JPEG, PNG o WebP.
	public Imagen crear(byte[] datos) {
		String tipo;

		if (datos == null || datos.length == 0)
			return null;

		tipo = detectarTipo(datos);
		Assert.notNull(tipo, "Formato de imagen no admitido: solo JPEG, PNG o WebP");

		return new Imagen(datos, tipo);
	}

	public static String detectarTipo(byte[] d) {
		if (d == null)
			return null;
		if (empiezaPor(d, 0, 0xFF, 0xD8, 0xFF))
			return JPEG;
		if (empiezaPor(d, 0, 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A))
			return PNG;
		if (empiezaPor(d, 0, 'R', 'I', 'F', 'F') && empiezaPor(d, 8, 'W', 'E', 'B', 'P'))
			return WEBP;
		return null;
	}

	private static boolean empiezaPor(byte[] datos, int desde, int... firma) {
		if (datos.length < desde + firma.length)
			return false;
		for (int i = 0; i < firma.length; i++)
			if ((datos[desde + i] & 0xFF) != firma[i])
				return false;
		return true;
	}

}
