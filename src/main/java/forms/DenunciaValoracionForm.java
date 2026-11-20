/* DenunciaValoracionForm.java
 *
 * Lo que escribe un empresario al denunciar una valoración de su negocio. El
 * cliente (denunciante) lo pone el servicio. La valoración solo se usa al crear
 * la denuncia; al editar se ignora.
 */

package forms;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import domain.DenunciaValoracion;
import domain.TipoDenuncia;
import domain.ValoracionNegocio;

public class DenunciaValoracionForm extends EntityForm {

	private ValoracionNegocio	valoracionNegocio;
	private String	titulo;
	private String	comentario;
	private TipoDenuncia	tipo;

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

	@NotNull
	public TipoDenuncia getTipo() {
		return tipo;
	}

	public void setTipo(TipoDenuncia tipo) {
		this.tipo = tipo;
	}

}
