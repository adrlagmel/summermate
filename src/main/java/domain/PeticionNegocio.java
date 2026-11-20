package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.FetchType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "codigo")}, indexes = {
	@Index(name = "idx_peticionnegocio_administrador", columnList = "administrador_id"),
	@Index(name = "idx_peticionnegocio_estado", columnList = "estado")
})
public class PeticionNegocio extends DomainEntity{

	// Attributes ----------------------------
	
	private String 	codigo;
	private Date 	fecha;
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
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
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
