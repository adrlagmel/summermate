package domain;

import java.time.LocalDateTime;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "codigo")}, indexes = {
	@Index(name = "idx_peticionnegocio_administrador", columnList = "administrador_id"),
	@Index(name = "idx_peticionnegocio_estado", columnList = "estado")
})
public class PeticionNegocio extends DomainEntity{

	// Attributes ----------------------------
	
	private String 	codigo;
	private LocalDateTime 	fecha;
	private String 	titulo;
	private EstadoPeticion 	estado;
	private String 	comentarios;
	
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
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	
	@NotBlank
	@SafeHtml
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	// Se guarda como texto (no como número de orden) para que añadir o
	// reordenar valores no cambie el significado de los datos existentes.
	@NotNull
	@Enumerated(EnumType.STRING)
	public EstadoPeticion getEstado() {
		return estado;
	}
	
	public void setEstado(EstadoPeticion estado) {
		this.estado = estado;
	}
	
	@SafeHtml
	public String getComentarios() {
		return comentarios;
	}
	
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	
	
	// Relationships -------------------------
	
	private Empresario empresario;
	private Administrador administrador;

	@NotNull
	@Valid
	@OneToOne(fetch = FetchType.LAZY, optional=false)
	public Empresario getEmpresario() {
		return empresario;
	}

	public void setEmpresario(Empresario empresario) {
		this.empresario = empresario;
	}

	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}
	
	
	

}
