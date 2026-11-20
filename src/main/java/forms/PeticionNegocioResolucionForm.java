/* PeticionNegocioResolucionForm.java
 *
 * Lo que escribe un administrador al aceptar o rechazar una petición: solo los
 * comentarios. El resto de datos se muestran, pero no se envían.
 */

package forms;

import utilities.validators.SafeHtml;
import domain.PeticionNegocio;

public class PeticionNegocioResolucionForm extends EntityForm {

	private String	comentarios;

	public PeticionNegocioResolucionForm() {
	}

	public PeticionNegocioResolucionForm(PeticionNegocio peticionNegocio) {
		copyIdentity(peticionNegocio);
		comentarios = peticionNegocio.getComentarios();
	}

	@SafeHtml
	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

}
