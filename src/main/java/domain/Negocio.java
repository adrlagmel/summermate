package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Negocio extends DomainEntity{
		
	private String 			nombre;
	private String 			tipo;
	private String 			descripcion;
	private String 			telefono;
	private Localizacion 	localizacion;
	private String 			paginaWeb;
	private Integer 		aforo;
	private byte[]          imagen;
	private Double valoracionMedia;
	
	public Double getValoracionMedia() {
		return valoracionMedia;
	}
	
	public void setValoracionMedia(Double valoracionMedia) {
		this.valoracionMedia = valoracionMedia;
	}	
		
	
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
		
	@Column(columnDefinition = "LONGBLOB")
	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}
	
	@NotNull
	@Range(min=0, max=200)
	public Integer getAforo() {
		return aforo;
	}
	
	public void setAforo(Integer aforo) {
		this.aforo = aforo;
	}
	
	// Relationships -------------------------

	private Empresario 					  empresario;
	private Collection<Evento> 		      eventos;
	private Collection<Reserva> 		  reservas;
	private Collection<CalendarioNegocio> calendarioNegocios;
	private Playa 					  	  playa;
		
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
	
	@Valid
	@ManyToOne(optional = true)
	public Playa getPlaya() {
		return playa;
	}

	public void setPlaya(Playa playa) {
		this.playa = playa;
	}
	
	@NotNull
	@OneToMany(mappedBy = "negocio")
	public Collection<CalendarioNegocio> getCalendarioNegocios() {
		return calendarioNegocios;
	}

	public void setCalendarioNegocios(Collection<CalendarioNegocio> calendarioNegocios) {
		this.calendarioNegocios = calendarioNegocios;
	}
}