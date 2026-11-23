/* ImagenService.java
 *
 * Crea imágenes a partir de lo que sube el usuario. El tipo se deduce de los
 * primeros bytes del fichero ("magic numbers"), no del nombre ni de la
 * cabecera Content-Type, que controla el navegador. Antes se aceptaba
 * cualquier fichero y se servía siempre como image/jpeg.
 */

package services;

import java.util.UUID;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import services.imagenes.AlmacenImagenes;
import org.springframework.stereotype.Service;
import utilities.Assert;

import domain.Imagen;

@Service
public class ImagenService {

	public static final String	JPEG	= "image/jpeg";
	public static final String	PNG		= "image/png";
	public static final String	WEBP	= "image/webp";
	public static final int		MAX_BYTES	= 3 * 1024 * 1024;

	private final ObjectProvider<AlmacenImagenes>	almacen;


	public ImagenService(ObjectProvider<AlmacenImagenes> almacen) {
		this.almacen = almacen;
	}


	// Devuelve null si no se ha subido nada (así se borraba la imagen antes).
	// Lanza IllegalArgumentException si el fichero no es JPEG, PNG o WebP.
	public Imagen crear(byte[] datos) {
		String tipo;

		if (datos == null || datos.length == 0)
			return null;

		// Fase 6: el límite de subida general es 10 MB (por el CSV de playas);
		// las imágenes siguen limitadas a 3 MB.
		Assert.isTrue(datos.length <= MAX_BYTES, "La imagen ocupa más de 3 MB");
		tipo = detectarTipo(datos);
		Assert.notNull(tipo, "Formato de imagen no admitido: solo JPEG, PNG o WebP");

		AlmacenImagenes almacen = this.almacen.getIfAvailable();
		if (almacen == null)
			return new Imagen(datos, tipo);

		// Fase 8: los bytes van al almacén de objetos (R2). Si la transacción
		// se deshace, el objeto ya subido se borra.
		String clave = "imagenes/" + UUID.randomUUID() + extension(tipo);
		almacen.guardar(clave, datos, tipo);
		if (TransactionSynchronizationManager.isSynchronizationActive())
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

				@Override
				public void afterCompletion(int estado) {
					if (estado == STATUS_ROLLED_BACK)
						almacen.borrar(clave);
				}
			});
		return Imagen.enAlmacen(clave, tipo, datos.length);
	}

	// Bytes de la imagen, estén en la base de datos o en el almacén
	public byte[] datos(Imagen imagen) {
		if (imagen.getClave() == null)
			return imagen.getDatos();
		return almacen.getObject().leer(imagen.getClave());
	}

	public java.util.Optional<String> urlPublica(Imagen imagen) {
		AlmacenImagenes almacen = this.almacen.getIfAvailable();
		if (imagen.getClave() == null || almacen == null)
			return java.util.Optional.empty();
		return almacen.urlPublica(imagen.getClave());
	}

	private static String extension(String tipo) {
		return switch (tipo) {
			case JPEG -> ".jpg";
			case PNG -> ".png";
			default -> ".webp";
		};
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
