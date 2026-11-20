package domain;

import java.util.Collection;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;

@Entity
@Table(indexes = {
	@Index(name = "idx_carpeta_actor", columnList = "actor_id")
})
@Access(AccessType.PROPERTY)
public class Carpeta extends DomainEntity{
	
	// Constructor ----------------------------------
	
	public Carpeta(){
		super();
	}
	
	// Attributes ----------------------------------
	
	private String nombre;

	@NotBlank
	@SafeHtml
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
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
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
