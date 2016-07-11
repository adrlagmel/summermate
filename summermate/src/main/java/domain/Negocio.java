package domain;


import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;


@Entity
@Access(AccessType.PROPERTY)
public class Negocio extends DomainEntity{
		
	private String 			nombre;
	private String 			tipo;
	private String 			sector;
	private String 			descripcion;
	private String 			telefono;
	private Localizacion 	localizacion;
	private String 			paginaWeb;
		
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@NotBlank
	@Pattern(regexp = "^\\+\\d{2,3}\\d{7,14}$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTelefono(){
		return telefono;
	}
	
	public void setTelefono(String telefono){
		this.telefono = telefono;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@URL
	public String getPaginaWeb(){
		return paginaWeb;
	}
	public void setPaginaWeb(String paginaWeb){
		this.paginaWeb = paginaWeb;
	}
	
	@NotNull
	@Valid
	public Localizacion getLocalizacion() {
		return localizacion;
	}
	
	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}
		
	// Relationships -------------------------

	private Empresario 					  empresario;
	private Collection<Evento> 		      eventos;
	private Collection<Reserva> 		  reservas;
	private Collection<Mesa> 			  mesas;
		
	@Valid
	@ManyToOne(optional = true)
	public Empresario getEmpresario() {
		return empresario;
	}

	public void setEmpresario(Empresario empresario) {
		this.empresario = empresario;
	}

	@NotNull
	@OneToMany(mappedBy = "negocio")
	public Collection<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(Collection<Evento> eventos) {
		this.eventos = eventos;
	}
	
	@NotNull
	@OneToMany(mappedBy = "negocio")
	public Collection<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(Collection<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	@NotNull
	@OneToMany(mappedBy = "negocio")
	public Collection<Mesa> getMesas() {
		return mesas;
	}

	public void setMesas(Collection<Mesa> mesas) {
		this.mesas = mesas;
	}
}