/* AlmacenImagenes.java
 *
 * Almacén de objetos para las imágenes (Fase 8). La implementación es
 * AlmacenS3, que sirve para Cloudflare R2 y cualquier servicio compatible con
 * S3. Si no hay almacén configurado (summermate.imagenes.almacen=bd), no hay
 * bean de este tipo y las imágenes siguen en la base de datos.
 */

package services.imagenes;

import java.util.Optional;

public interface AlmacenImagenes {

	void guardar(String clave, byte[] datos, String tipo);

	byte[] leer(String clave);

	void borrar(String clave);

	// URL pública del objeto, si el cubo se publica (dominio propio de R2)
	Optional<String> urlPublica(String clave);

}
