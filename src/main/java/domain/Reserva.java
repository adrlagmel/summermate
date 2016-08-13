package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Reserva extends DomainEntity{

	// Constructor --------------------
	
		public Reserva() {
			super();
		}
		
	
	// Attributes ----------------------------
	
	private String 	codigo;
	private Date	fechaCreacion;
	private Date 	fecha;
	private String 	comentarios;
	
	private Integer comensales;
	private Boolean fumadores;
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
	
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	
	@Min(1)
	public Integer getComensales() {
		return comensales;
	}

	public void setComensales(Integer comensales) {
		this.comensales = comensales;
	}
	
	public Boolean getFumadores() {
		return fumadores;
	}

	public void setFumadores(Boolean fumadores) {
		this.fumadores = fumadores;
	}
	
	// Relationships -------------------------
	
	
	private Usuario usuario;
	private Negocio negocio;
	private ValoracionNegocio valoracionNegocio;
	
	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public Negocio getNegocio() {
		return negocio;
	}
	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
	
	
	@Valid
	@OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
	public ValoracionNegocio getValoracionNegocio() {
		return valoracionNegocio;
	}
	public void setValoracionNegocio(ValoracionNegocio valoracionNegocio) {
		this.valoracionNegocio = valoracionNegocio;
	}
	
	
	
}
