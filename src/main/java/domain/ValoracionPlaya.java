package domain;

import java.util.Date;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import utilities.validators.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(indexes = {
	@Index(name = "idx_valoracionplaya_playa", columnList = "playa_id"),
	@Index(name = "idx_valoracionplaya_usuario", columnList = "usuario_id")
})
@Access(AccessType.PROPERTY)
public class ValoracionPlaya extends DomainEntity{
	
	// Constructor --------------------
	
	public ValoracionPlaya() {
		
	}
	
	// Attributes --------------------
	
	private String 	titulo;
	private Date 	fecha;
	private String 	comentario;
	private Integer puntuacion;
	
	@NotBlank
	@SafeHtml
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@NotNull
	@Past
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	@NotNull
	@Range(min=0, max=10)
	public Integer getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(Integer puntuacion) {
		this.puntuacion = puntuacion;
	}

	@NotBlank
	@SafeHtml
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	// Relationships ------------------------------------------------
	
	private Playa playa;
	private Usuario usuario;
	
	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Playa getPlaya() {
		return playa;
	}

	public void setPlaya(Playa playa) {
		this.playa = playa;
	}
	
	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
