/* DenunciaValoracionForm.java
 *
 * Lo que escribe un empresario al denunciar una valoración de su negocio. El
 * cliente (denunciante) lo pone el servicio. La valoración solo se usa al crear
 * la denuncia; al editar se ignora.
 */

package forms;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import javax.validation.constraints.Pattern;
import domain.DenunciaValoracion;
import domain.ValoracionNegocio;

public class DenunciaValoracionForm extends EntityForm {

	private ValoracionNegocio	valoracionNegocio;
	private String	titulo;
	private String	comentario;
	private String	tipo;

	public DenunciaValoracionForm() {
	}

	public DenunciaValoracionForm(DenunciaValoracion entity) {
		copyIdentity(entity);
		valoracionNegocio = entity.getValoracionNegocio();
		titulo = entity.getTitulo();
		comentario = entity.getComentario();
		tipo = entity.getTipo();
	}

	@NotNull
	public ValoracionNegocio getValoracionNegocio() {
		return valoracionNegocio;
	}

	public void setValoracionNegocio(ValoracionNegocio valoracionNegocio) {
		this.valoracionNegocio = valoracionNegocio;
	}

	@NotBlank
	@SafeHtml
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@NotBlank
	@SafeHtml
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@NotBlank
	@Pattern(regexp = "^FALSEDAD|RECHAZO|BURLA$")
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
