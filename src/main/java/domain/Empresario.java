package domain;

import java.util.Collection;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Empresario extends Cliente{
	
	// Attributes ----------------------------------
	
	private String cif;
	
	@Pattern(regexp = "^\\w+$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
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
