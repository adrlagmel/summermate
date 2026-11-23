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
import jakarta.validation.constraints.Past;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import utilities.validators.SafeHtml;

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
	private LocalDateTime 	fecha;
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
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	public void setFecha(LocalDateTime fecha) {
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
