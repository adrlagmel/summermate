package forms;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Negocio;


public class ReservaForm {
		
	private Integer reservaId;
	
	private String  comentarios;
	private Boolean fumadores;
	private Integer comensales;
	private Date    fecha;
	
	private Negocio negocio;
		
	@NotNull
	@Min(0) // Not necessary because an exception is thrown
	public Integer getReservaId() {
		return reservaId;
	}

	public void setReservaId(Integer reservaId) {
		this.reservaId = reservaId;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@SafeHtml(whitelistType=WhiteListType.NONE)
	@NotBlank
	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	
	public Boolean getFumadores() {
		return fumadores;
	}

	public void setFumadores(Boolean fumadores) {
		this.fumadores = fumadores;
	}
	
	@NotNull
	@Min(1) // Not necessary because an exception is thrown
	public Integer getComensales() {
		return comensales;
	}

	public void setComensales(Integer comensales) {
		this.comensales = comensales;
	}

	@NotNull
	@Valid
	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}

}