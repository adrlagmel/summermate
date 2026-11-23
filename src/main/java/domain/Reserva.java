package domain;

import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import jakarta.validation.constraints.NotBlank;
import utilities.validators.SafeHtml;

@Entity
@Table(indexes = {
	@Index(name = "idx_reserva_negocio_fecha", columnList = "negocio_id, fecha"),
	@Index(name = "idx_reserva_usuario_fecha", columnList = "usuario_id, fecha"),
	@Index(name = "idx_reserva_codigo", columnList = "codigo")
})
@Access(AccessType.PROPERTY)
public class Reserva extends DomainEntity{

	// Constructor --------------------
	
		public Reserva() {
			super();
		}
		
	
	// Attributes ----------------------------
	
	private String 	codigo;
	private LocalDateTime	fechaCreacion;
	private LocalDateTime 	fecha;
	private String 	comentarios;
	
	private Integer comensales;
	private Boolean fumadores;
	
	@NotBlank
	@SafeHtml
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@NotNull
	@Past
	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	@NotNull
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	
	@SafeHtml
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	
	@NotNull
	@Range(min = 1, max = 10)
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
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Negocio getNegocio() {
		return negocio;
	}
	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
	
	
	// Mesa asignada (solo en negocios con mesas)
	private Mesa mesa;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Mesa getMesa() {
		return mesa;
	}
	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}

	// Hasta cuándo ocupa la reserva su mesa o sus plazas
	@jakarta.persistence.Transient
	public java.time.LocalDateTime getFin() {
		return fecha == null || negocio == null ? null : fecha.plusMinutes(negocio.getDuracionReserva());
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
