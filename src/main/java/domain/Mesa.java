/* Mesa.java
 *
 * Mesa de un negocio de restauración (memoria del TFG, OBJ-0034). Una reserva
 * ocupa una mesa con capacidad suficiente durante la duración de la reserva.
 */

package domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import utilities.validators.SafeHtml;

@Entity
@Table(indexes = @Index(name = "idx_mesa_negocio", columnList = "negocio_id"))
@Access(AccessType.PROPERTY)
public class Mesa extends DomainEntity {

	private String	nombre;
	private Integer	capacidad;
	private Negocio	negocio;


	@NotBlank
	@Size(max = 40)
	@SafeHtml
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@NotNull
	@Range(min = 1, max = 20)
	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}

}
