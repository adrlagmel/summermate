package domain;

import jakarta.validation.constraints.NotBlank;
import utilities.validators.NifCif;
import java.time.LocalDate;
import java.util.Collection;


import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import utilities.validators.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Empresario extends Cliente{
	
	// Attributes ----------------------------------
	
	private String cif;
	
	@NotBlank
	@NifCif
	public String getCif() {
		return cif;

	}

	public void setCif(String cif) {
		this.cif = cif;

	}
	
	// Cuenta premium (Fase 6): activa hasta esta fecha incluida. Null si nunca
	// la ha contratado.
	private LocalDate premiumHasta;

	public LocalDate getPremiumHasta() {
		return premiumHasta;
	}

	public void setPremiumHasta(LocalDate premiumHasta) {
		this.premiumHasta = premiumHasta;
	}

	@Transient
	public boolean isPremium() {
		return premiumHasta != null && !premiumHasta.isBefore(LocalDate.now());
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
