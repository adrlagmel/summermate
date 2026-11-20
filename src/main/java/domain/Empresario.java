package domain;

import java.util.Collection;


import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import utilities.validators.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Empresario extends Cliente{
	
	// Attributes ----------------------------------
	
	private String cif;
	
	@Pattern(regexp = "^\\w+$")
	@SafeHtml
	public String getCif() {
		return cif;

	}

	public void setCif(String cif) {
		this.cif = cif;

	}
	
	// Relationships -------------------------------
	
	private Collection<Negocio> negocios;
	
	@NotNull
	@Valid
	@OneToMany(mappedBy="empresario")
	public Collection<Negocio> getNegocios() {
		return negocios;
	}

	public void setNegocios(Collection<Negocio> negocios) {
		this.negocios = negocios;
	}

}
