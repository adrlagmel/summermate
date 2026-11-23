package domain;

import java.time.LocalTime;
import java.util.Collection;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import utilities.validators.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(indexes = {
	@Index(name = "idx_negocio_empresario", columnList = "empresario_id"),
	@Index(name = "idx_negocio_playa", columnList = "playa_id")
})
@Access(AccessType.PROPERTY)
public class Negocio extends DomainEntity{
		
	private String 			nombre;
	private String 			tipo;
	private String 			descripcion;
	private String 			telefono;
	private String 			paginaWeb;
	private Integer 		aforo;
	private Imagen          imagen;
	private Double 			valoracionMedia;
	private Boolean 		negocioActivo;
	
	public Double getValoracionMedia() {
		return valoracionMedia;
	}
	
	public void setValoracionMedia(Double valoracionMedia) {
		this.valoracionMedia = valoracionMedia;
	}	
		
	
	@NotBlank
	@SafeHtml
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@NotBlank
	@Pattern(regexp = "^\\+\\d{2,3}\\d{7,14}$")
	@SafeHtml
	public String getTelefono(){
		return telefono;
	}
	
	public void setTelefono(String telefono){
		this.telefono = telefono;
	}
	
	@NotBlank
	@SafeHtml
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@NotBlank
	@SafeHtml
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
		
	// La imagen va en su propia tabla y se carga solo cuando se usa (LAZY).
	// orphanRemoval: al cambiarla o quitarla se borra la anterior.
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(unique = true)
	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
	}
	
	@NotNull
	@Range(min=1, max=500)
	public Integer getAforo() {
		return aforo;
	}
	
	public void setAforo(Integer aforo) {
		this.aforo = aforo;
	}
	
	public Boolean getNegocioActivo() {
		return negocioActivo;
	}
	
	public void setNegocioActivo(Boolean negocioActivo) {
		this.negocioActivo = negocioActivo;
	}
	
	// Relationships -------------------------

	private Empresario 					  empresario;
	private Collection<Evento> 		      eventos;
	private Collection<Reserva> 		  reservas;
	private Collection<Mesa>			  mesas;
	private Collection<Cierre>			  cierres;
	private Playa 					  	  playa;
		
	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
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
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Playa getPlaya() {
		return playa;
	}

	public void setPlaya(Playa playa) {
		this.playa = playa;
	}
	
	// Mesas del negocio (OBJ-0034). Sin mesas, las reservas solo se limitan
	// por el aforo; con mesas, cada reserva ocupa una.
	@OneToMany(mappedBy = "negocio", cascade = CascadeType.REMOVE)
	@OrderBy("capacidad, nombre")
	public Collection<Mesa> getMesas() {
		return mesas;
	}

	public void setMesas(Collection<Mesa> mesas) {
		this.mesas = mesas;
	}

	// Días en que el negocio no admite reservas
	@OneToMany(mappedBy = "negocio", cascade = CascadeType.REMOVE)
	@OrderBy("fecha")
	public Collection<Cierre> getCierres() {
		return cierres;
	}

	public void setCierres(Collection<Cierre> cierres) {
		this.cierres = cierres;
	}

	// Horario (Fase 6) ----------------------

	private LocalTime	horaApertura	= LocalTime.of(12, 0);
	private LocalTime	horaCierre		= LocalTime.MIDNIGHT;
	private Integer		duracionReserva	= 60;

	// Primera hora a la que se puede reservar
	@NotNull
	public LocalTime getHoraApertura() {
		return horaApertura;
	}

	public void setHoraApertura(LocalTime horaApertura) {
		this.horaApertura = horaApertura;
	}

	// Hora de cierre; 00:00 significa medianoche (fin del día)
	@NotNull
	public LocalTime getHoraCierre() {
		return horaCierre;
	}

	public void setHoraCierre(LocalTime horaCierre) {
		this.horaCierre = horaCierre;
	}

	// Minutos que ocupa una reserva (la mesa o las plazas)
	@NotNull
	@Range(min = 15, max = 240)
	public Integer getDuracionReserva() {
		return duracionReserva;
	}

	public void setDuracionReserva(Integer duracionReserva) {
		this.duracionReserva = duracionReserva;
	}
}
