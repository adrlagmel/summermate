/* ValoracionNegocioForm.java
 *
 * Lo que escribe un usuario al valorar un negocio tras su reserva. Fecha,
 * denuncias y comentarioUtil los gestiona el servicio. La reserva solo se usa
 * al crear la valoración (y debe ser del usuario); al editar se ignora.
 */

package forms;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import org.hibernate.validator.constraints.Range;
import domain.Reserva;
import domain.ValoracionNegocio;

public class ValoracionNegocioForm extends EntityForm {

	private Reserva	reserva;
	private String	titulo;
	private String	comentario;
	private Integer	puntuacion;

	public ValoracionNegocioForm() {
	}

	public ValoracionNegocioForm(ValoracionNegocio entity) {
		copyIdentity(entity);
		reserva = entity.getReserva();
		titulo = entity.getTitulo();
		comentario = entity.getComentario();
		puntuacion = entity.getPuntuacion();
	}

	@NotNull
	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
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
