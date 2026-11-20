package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;

import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(indexes = {
	@Index(name = "idx_evento_negocio", columnList = "negocio_id"),
	@Index(name = "idx_evento_fechacelebracion", columnList = "fechaCelebracion")
})
@Access(AccessType.PROPERTY)
public class Evento extends DomainEntity{
		
	private String			codigo;
	private String 			nombre;
	private String 			descripcion;
	private Double 			precio;
	private Imagen			imagen;
	private Date 			fechaRegistro;
	private Date 			fechaCelebracion;
		
	
	@NotBlank
	@SafeHtml
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@NotBlank
	@SafeHtml
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@NotBlank
	@SafeHtml
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public Double getPrecio(){
		return precio;
	}
	public void setPrecio(Double precio){
		this.precio = precio;
	}
	
	// La imagen va en su propia tabla y se carga solo cuando se usa (LAZY).
	// orphanRemoval: al cambiarla o quitarla se borra la anterior.
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(unique = true)
	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen= imagen;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFechaCelebracion() {
		return fechaCelebracion;
	}
	
	public void setFechaCelebracion(Date fechaCelebracion) {
		this.fechaCelebracion = fechaCelebracion;
	}
	
	
	// Relationships -------------------------
		
	private Negocio  negocio;
	private Collection<Usuario> usuarios;
	
	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
		
	@NotNull
	@Valid
	@ManyToMany()
	@JoinTable(indexes = {
		@Index(name = "idx_evento_usuario_evento", columnList = "eventos_id"),
		@Index(name = "idx_evento_usuario_usuario", columnList = "usuarios_id")
	})
	public Collection<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Collection<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}