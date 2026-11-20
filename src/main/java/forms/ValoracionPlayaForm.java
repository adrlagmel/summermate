/* ValoracionPlayaForm.java
 *
 * Lo que escribe un usuario al valorar una playa. Usuario y fecha los pone el
 * servicio. La playa solo se usa al crear la valoración; al editar se ignora.
 */

package forms;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import org.hibernate.validator.constraints.Range;
import domain.Playa;
import domain.ValoracionPlaya;

public class ValoracionPlayaForm extends EntityForm {

	private Playa	playa;
	private String	titulo;
	private String	comentario;
	private Integer	puntuacion;

	public ValoracionPlayaForm() {
	}

	public ValoracionPlayaForm(ValoracionPlaya entity) {
		copyIdentity(entity);
		playa = entity.getPlaya();
		titulo = entity.getTitulo();
		comentario = entity.getComentario();
		puntuacion = entity.getPuntuacion();
	}

	@NotNull
	public Playa getPlaya() {
		return playa;
	}

	public void setPlaya(Playa playa) {
		this.playa = playa;
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
	@Range(min = 0, max = 10)
	public Integer getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Integer puntuacion) {
		this.puntuacion = puntuacion;
	}

}
