package domain;

import java.util.Date;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class CalendarioNegocio extends DomainEntity{

	// Attributes ----------------------------
	
	private Date fechaInicio;
	private Date fechaFin;
	private String anotacionesReserva;
	private String codigoNegocio;
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCodigoNegocio() {
		return codigoNegocio;
	}
	public void setCodigoNegocio(String codigoNegocio) {
		this.codigoNegocio = codigoNegocio;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	@SafeHtml(whitelistType=WhiteListType.NONE)
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
	@ManyToOne(optional=false)
	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
	
}
