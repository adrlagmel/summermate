/* EventoForm.java
 *
 * Campos editables de un evento. No incluye usuarios (participantes), imagen
 * ni fechaRegistro: antes iban como campos ocultos y cualquiera podía
 * cambiarlos. El código se muestra pero no se puede modificar.
 */

package forms;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import utilities.validators.SafeHtml;
import domain.Evento;
import domain.Negocio;

public class EventoForm extends EntityForm {

	private String	codigo;
	private String	nombre;
	private String	descripcion;
	private LocalDateTime	fechaCelebracion;
	private BigDecimal	precio;
	private Negocio	negocio;

	public EventoForm() {
	}

	public EventoForm(Evento evento) {
		copyIdentity(evento);
		codigo = evento.getCodigo();
		nombre = evento.getNombre();
		descripcion = evento.getDescripcion();
		fechaCelebracion = evento.getFechaCelebracion();
		precio = evento.getPrecio();
		negocio = evento.getNegocio();
	}

	// Solo lectura: lo genera EventoService.create() y reconstruct() lo ignora.
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@NotBlank
	@SafeHtml
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@NotBlank
	@SafeHtml
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public LocalDateTime getFechaCelebracion() {
		return fechaCelebracion;
	}

	public void setFechaCelebracion(LocalDateTime fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}

	@NotNull
	@DecimalMin("0.00")
	@Digits(integer = 5, fraction = 2)
	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	@NotNull
	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}

}
