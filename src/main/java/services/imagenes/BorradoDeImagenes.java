/* BorradoDeImagenes.java
 *
 * Cuando se borra una fila de Imagen (al cambiar o quitar la foto, por
 * orphanRemoval), su objeto en el almacén se borra después de confirmar la
 * transacción. Si la transacción se deshace, el objeto se queda.
 * Hibernate crea este listener con el contenedor de Spring, así que puede
 * recibir beans.
 */

package services.imagenes;

import jakarta.persistence.PostRemove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import domain.Imagen;

@Component
public class BorradoDeImagenes {

	private static final Logger						log	= LoggerFactory.getLogger(BorradoDeImagenes.class);

	private final ObjectProvider<AlmacenImagenes>	almacen;


	public BorradoDeImagenes(ObjectProvider<AlmacenImagenes> almacen) {
		this.almacen = almacen;
	}

	@PostRemove
	public void borrada(Imagen imagen) {
		String clave = imagen.getClave();
		AlmacenImagenes a = almacen.getIfAvailable();
		if (clave == null || a == null)
			return;

		Runnable borrar = () -> {
			try {
				a.borrar(clave);
			} catch (RuntimeException fallo) {
				log.warn("No se pudo borrar la imagen {} del almacén: {}", clave, fallo.getMessage());
			}
		};
		if (TransactionSynchronizationManager.isSynchronizationActive())
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

				@Override
				public void afterCommit() {
					borrar.run();
				}
			});
		else
			borrar.run();
	}

}
