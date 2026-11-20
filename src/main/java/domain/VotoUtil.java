/* VotoUtil.java
 *
 * Voto de "comentario útil" (memoria del TFG): un cliente marca como útil la
 * valoración de negocio de otro. Uno por cliente y valoración. El total se
 * guarda también en ValoracionNegocio.comentarioUtil para pintar las listas
 * sin contar en cada fila.
 */

package domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
	uniqueConstraints = @UniqueConstraint(name = "uk_votoutil_cliente_valoracion", columnNames = { "cliente_id", "valoracionNegocio_id" }),
	indexes = @Index(name = "idx_votoutil_valoracionnegocio", columnList = "valoracionNegocio_id"))
@Access(AccessType.PROPERTY)
public class VotoUtil extends DomainEntity {

	private Cliente				cliente;
	private ValoracionNegocio	valoracionNegocio;


	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public ValoracionNegocio getValoracionNegocio() {
		return valoracionNegocio;
	}

	public void setValoracionNegocio(ValoracionNegocio valoracionNegocio) {
		this.valoracionNegocio = valoracionNegocio;
	}

}
