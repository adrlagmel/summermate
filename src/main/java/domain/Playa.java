package domain;

import java.util.Collection;



import javax.persistence.Access;

import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;



@Entity
@Access(AccessType.PROPERTY)
public class Playa extends DomainEntity{

	// Attributes ----------------------------
	
	private String 	nombre;
	private String 	descripcion;
	private String 	composicion;
	private String 	servicios;
	private Integer	extension;
	private Localizacion localizacion;
	private byte[] imagen;
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComposicion() {
		return composicion;
	}

	public void setComposicion(String composicion) {
		this.composicion = composicion;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getServicios() {
		return servicios;
	}

	public void setServicios(String servicios) {
		this.servicios = servicios;
	}
	
	@NotNull
	@Min(0)
	public Integer getExtension() {
		return extension;
	}
	
	public void setExtension(Integer extension) {
		this.extension = extension;
	}	
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@NotNull
	@Valid
	public Localizacion getLocalizacion() {
		return localizacion;
	}
	
	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}
	
	@Column(columnDefinition = "LONGBLOB")
	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}
	
	// Relationships -------------------------
	
	private Administrador 				administrador;
	private Collection<ValoracionPlaya> valoracionPlayas;
	private Collection<Negocio>		    negocios;

	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}
	
	@NotNull
	@Valid
	@OneToMany(mappedBy="playa",cascade = CascadeType.ALL)
	public Collection<ValoracionPlaya> getValoracionPlayas() {
		return valoracionPlayas;
	}

	public void setValoracionPlayas(Collection<ValoracionPlaya> valoracionPlayas) {
		this.valoracionPlayas = valoracionPlayas;
	}
	
	@NotNull
	@Valid
	@OneToMany(mappedBy="playa", cascade = CascadeType.ALL)
	public Collection<Negocio> getNegocios() {
		return negocios;
	}

	public void setNegocios(Collection<Negocio> negocios) {
		this.negocios = negocios;
	}

}
