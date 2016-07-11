package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Actor extends DomainEntity {
	
	// Constructor ----------------------------------------------------------
	
	public Actor() {

	}
	
	// Attributes -----------------------------------------------------------
	
	private String nombre;
	private String apellidos;
	private String email;
	

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
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	@NotNull
	@Email
	@SafeHtml(whitelistType = WhiteListType.NONE)
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
	 @OneToOne(cascade=CascadeType.ALL,optional = false)
	 public UserAccount getUserAccount() {
	  return userAccount;
	 }

	 public void setUserAccount(UserAccount userAccount) {
	  this.userAccount = userAccount;
	 }
	 
	 @NotNull
	 @Valid
	 @OneToMany(mappedBy="actor")
	 public Collection<Carpeta> getCarpetas() {
		return carpetas;
	 }

	 public void setCarpetas(Collection<Carpeta> carpetas) {
		 this.carpetas = carpetas;
	 }
}
