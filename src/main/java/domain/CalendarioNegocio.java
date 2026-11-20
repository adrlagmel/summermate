package domain;

import java.time.LocalDateTime;


import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;

@Entity
@Table(indexes = {
	@Index(name = "idx_calendarionegocio_negocio", columnList = "negocio_id"),
	@Index(name = "idx_calendarionegocio_codigonegocio", columnList = "codigoNegocio")
})
@Access(AccessType.PROPERTY)
public class CalendarioNegocio extends DomainEntity{

	// Attributes ----------------------------
	
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private String anotacionesReserva;
	private String codigoNegocio;
	
	@NotBlank
	@SafeHtml
	public String getCodigoNegocio() {
		return codigoNegocio;
	}
	public void setCodigoNegocio(String codigoNegocio) {
		this.codigoNegocio = codigoNegocio;
	}
	
	@NotNull
	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	@NotNull
	public LocalDateTime getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	@SafeHtml
	public String getAnotacionesReserva() {
		return anotacionesReserva;
	}

	public void setAnotacionesReserva(String anotacionesReserva) {
		this.anotacionesReserva = anotacionesReserva;
	}
	
	// Relationships -------------------------
	

	private Negocio negocio;

	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
	
}
