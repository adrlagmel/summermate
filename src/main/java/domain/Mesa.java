package domain;

import java.util.Collection;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
@Entity
@Access(AccessType.PROPERTY)
public class Mesa extends DomainEntity{

	// Attributes ----------------------------
	
	private String  nombre;
	private Integer personas;
	private Boolean disponible;
	private Boolean fumadores;
	
	
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Min(1)
	public Integer getPersonas() {
		return personas;
	}

	public void setPersonas(Integer personas) {
		this.personas = personas;
	}
	
	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}
	
	public Boolean getFumadores() {
		return fumadores;
	}

	public void setFumadores(Boolean fumadores) {
		this.fumadores = fumadores;
	}

	
	// Relationships -------------------------

	private Negocio 					  negocio;
	private Collection<CalendarioNegocio> calendarioNegocios;

	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public Negocio getNegocio() {
		return negocio;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
	
	@NotNull
	@OneToMany(mappedBy = "mesa")
	public Collection<CalendarioNegocio> getCalendarioNegocios() {
		return calendarioNegocios;
	}

	public void setCalendarioNegocios(Collection<CalendarioNegocio> calendarioNegocios) {
		this.calendarioNegocios = calendarioNegocios;
	}
	
}
