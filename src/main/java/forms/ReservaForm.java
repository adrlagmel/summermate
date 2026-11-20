package forms;

import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Negocio;


public class ReservaForm {
		
	private Integer reservaId;
	
	private String  comentarios;
	private Boolean fumadores;
	private Integer comensales;
	private LocalDateTime    fecha;
	
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
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	@SafeHtml
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
	@Range(min = 1, max = 10)
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