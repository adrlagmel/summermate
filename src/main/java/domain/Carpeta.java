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
public class Carpeta extends DomainEntity{
	
	// Constructor ----------------------------------
	
	public Carpeta(){
		super();
	}
	
	// Attributes ----------------------------------
	
	private String nombre;

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	// Relationships ----------------------------------
	
	private Actor actor;
	private Collection<Mensaje> mensajes;

	@Valid
	@ManyToOne(optional=false)
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	@NotNull
	@Valid
	@OneToMany(mappedBy = "carpeta", cascade = CascadeType.ALL)
	public Collection<Mensaje> getMensajes() {
	    return mensajes;
	}
	public void setMensajes(Collection<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public void addMensaje(Mensaje mensaje) {
	    mensajes.add(mensaje);
	    mensaje.setCarpeta(this);
	}
	public void removeMensaje(Mensaje mensaje) {
	    mensajes.remove(mensaje);
	    mensaje.setCarpeta(null);
	}
	
	
	
	

}
