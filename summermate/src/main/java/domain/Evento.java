package domain;

import javax.persistence.Access;

import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;


@Entity
@Access(AccessType.PROPERTY)
public class Evento extends DomainEntity{
		
	private String			codigo;
	private String 			nombre;
	private String 			descripcion;
	private Double 			precio;
	private byte[]			imagen;
		
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
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
	
	@Column(columnDefinition = "LONGBLOB")
	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen= imagen;
	}
	
	
	// Relationships -------------------------
		
	private Negocio  negocio;
	private Usuario usuario;
	
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
	
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
		
}