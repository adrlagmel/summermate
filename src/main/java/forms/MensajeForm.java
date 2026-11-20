/* MensajeForm.java
 *
 * Datos que el usuario escribe al enviar un mensaje. Remitente, carpeta y
 * fecha los pone el servicio; antes iban como campos ocultos y se podían
 * falsear (incluido el id, que permitía sobrescribir un mensaje existente).
 */

package forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import utilities.validators.SafeHtml;
import domain.Actor;

public class MensajeForm {

	private Actor	beneficiario;
	private String	asunto;
	private String	cuerpo;

	@NotNull
	public Actor getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Actor beneficiario) {
		this.beneficiario = beneficiario;
	}

	@NotBlank
	@SafeHtml
	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	@NotBlank
	@SafeHtml
	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

}
