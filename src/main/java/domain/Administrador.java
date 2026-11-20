package domain;


import java.util.Collection;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Administrador extends Actor{

	// Attributes ------------------------------
	
	// Relationships ---------------------------
	
	private Collection<PeticionNegocio> peticionNegocios;
	private Collection<Playa> playas;

	@NotNull
	@Valid
	@OneToMany(mappedBy="administrador")
	public Collection<PeticionNegocio> getPeticionNegocios() {
		return peticionNegocios;
	}

	public void setPeticionNegocios(Collection<PeticionNegocio> peticionNegocios) {
		this.peticionNegocios = peticionNegocios;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy="administrador")
	public Collection<Playa> getPlayas() {
		return playas;
	}

	public void setPlayas(Collection<Playa> playas) {
		this.playas = playas;
	}

}
