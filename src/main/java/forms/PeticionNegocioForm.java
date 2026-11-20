/* PeticionNegocioForm.java
 *
 * Lo único que escribe un empresario al pedir dar de alta su negocio. Código,
 * fecha, estado y empresario los asigna PeticionNegocioService.create(); antes
 * iban como campos ocultos (incluido el estado, que podía llegar ya ACEPTADO).
 */

package forms;

import org.hibernate.validator.constraints.NotBlank;

import utilities.validators.SafeHtml;

public class PeticionNegocioForm {

	private String	titulo;

	@NotBlank
	@SafeHtml
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
