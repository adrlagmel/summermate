package domain;

import java.util.Collection;


import javax.persistence.Access;

import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;



@Entity
@Access(AccessType.PROPERTY)
public class Playa extends DomainEntity{

	// Attributes ----------------------------
	
	private String 		nombre;
	private String 		provincia;
	private String 		municipio;
	private TipoPlaya 	tipoPlaya;
	private String 		descripcion;
	
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
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getProvincia() {
		return provincia;
	}
	
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getMunicipio() {
		return municipio;
	}
	
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
	@NotNull
	@Valid
	public TipoPlaya getTipoPlaya() {
		return tipoPlaya;
	}
	
	public void setTipoPlaya(TipoPlaya tipoPlaya) {
		this.tipoPlaya = tipoPlaya;
	}
	
	
	// Relationships -------------------------
	
	private Administrador 		administrador;
	private Collection<ValoracionPlaya> valoracionPlayas;

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

}
