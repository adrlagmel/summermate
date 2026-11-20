/* PagoPremium.java
 *
 * Pago de la cuenta premium de un empresario (Fase 6). Sigue el flujo de una
 * pasarela con página de pago externa: se crea PENDIENTE con una referencia,
 * la pasarela lo confirma o se cancela, y al confirmarse se amplía
 * Empresario.premiumHasta.
 */

package domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "idx_pagopremium_empresario", columnList = "empresario_id"))
@Access(AccessType.PROPERTY)
public class PagoPremium extends DomainEntity {

	public enum Plan {
		MENSUAL(1, new BigDecimal("9.99")), ANUAL(12, new BigDecimal("99.00"));

		public final int		meses;
		public final BigDecimal	importe;

		Plan(int meses, BigDecimal importe) {
			this.meses = meses;
			this.importe = importe;
		}

		public int getMeses() {
			return meses;
		}

		public BigDecimal getImporte() {
			return importe;
		}
	}

	public enum Estado {
		PENDIENTE, PAGADO, CANCELADO
	}


	private String			referencia;
	private Plan			plan;
	private BigDecimal		importe;
	private Estado			estado;
	private LocalDateTime	fecha;
	private Empresario		empresario;


	@NotBlank
	@Column(unique = true)
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@NotNull
	@Column(precision = 7, scale = 2)
	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@NotNull
	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Empresario getEmpresario() {
		return empresario;
	}

	public void setEmpresario(Empresario empresario) {
		this.empresario = empresario;
	}

}
