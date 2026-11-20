/* EntityForm.java
 *
 * Base de los formularios que editan una entidad. Los controladores enlazan
 * estos objetos en lugar de las entidades, así que solo se puede modificar lo
 * que el formulario declara (evita el "mass assignment": que alguien añada a la
 * petición campos como valoracionMedia o negocioActivo).
 *
 * Se conserva la versión para no perder el bloqueo optimista que antes daba
 * el merge de la entidad: si alguien la ha modificado mientras tanto, falla.
 */

package forms;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import domain.DomainEntity;

public abstract class EntityForm {

	private int id;
	private int version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isNew() {
		return id == 0;
	}

	// Copia el id y la versión de la entidad (para mostrar el formulario).
	protected void copyIdentity(DomainEntity entity) {
		this.id = entity.getId();
		this.version = entity.getVersion();
	}

	// Comprueba que la entidad no ha cambiado desde que se abrió el formulario.
	public void checkVersion(DomainEntity entity) {
		if (entity.getVersion() != version)
			throw new ObjectOptimisticLockingFailureException(entity.getClass(), entity.getId());
	}

}
