package domain;

import java.util.Collection;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import utilities.validators.SafeHtml;

import security.UserAccount;

// Raíz de la jerarquía de actores: una tabla "actor" con los campos comunes y
// una tabla por subclase (administrador, cliente, usuario, empresario) unidas
// por el id. Las consultas sobre Actor hacen JOIN, no UNION de tablas enteras.
@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Actor extends DomainEntity {
	
	// Constructor ----------------------------------------------------------
	
	public Actor() {

	}
	
	// Attributes -----------------------------------------------------------
	
	private String nombre;
	private String apellidos;
	private String email;
	

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
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	@NotNull
	@Email
	@SafeHtml
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	 // Relationships ----------------------------------------------------------

	private UserAccount userAccount;
	private Collection<Carpeta> carpetas;
	 
	 @NotNull
	 @Valid
	 @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL,optional = false)
	 public UserAccount getUserAccount() {
	  return userAccount;
	 }

	 public void setUserAccount(UserAccount userAccount) {
	  this.userAccount = userAccount;
	 }
	 
	 @NotNull
	 @OneToMany(mappedBy="actor")
	 public Collection<Carpeta> getCarpetas() {
		return carpetas;
	 }

	 public void setCarpetas(Collection<Carpeta> carpetas) {
		 this.carpetas = carpetas;
	 }
}
