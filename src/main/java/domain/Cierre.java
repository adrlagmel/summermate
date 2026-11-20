/* Cierre.java
 *
 * Día en que un negocio no admite reservas (vacaciones, evento privado...).
 */

package domain;

import java.time.LocalDate;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import utilities.validators.SafeHtml;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_cierre_negocio_fecha", columnNames = { "negocio_id", "fecha" }))
@Access(AccessType.PROPERTY)
public class Cierre extends DomainEntity {

	private LocalDate	fecha;
	private String		motivo;
	private Negocio		negocio;


	@NotNull
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Size(max = 120)
	@SafeHtml
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
