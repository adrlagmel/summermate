package forms;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import domain.Negocio;


public class MesaForm {
	
	private Integer mesaId;
	
	private String  nombre;
	private Integer personas;
	private Boolean fumadores;
	private Boolean disponible;
	
	private Negocio negocio;


	@NotNull
	@Min(0) 
	public Integer getMesaId() {
		return mesaId;
	}

	public void setReservaId(Integer mesaId) {
		this.mesaId = mesaId;
	}
	
	@SafeHtml(whitelistType=WhiteListType.NONE)
	@NotBlank
	public String getNombre() {
		return nombre;
	}
	
	public void setPersonas(Integer personas) {
		this.personas = personas;
	}
	
	@Min(1)
	public Integer getPersonas() {
		return personas;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Boolean getFumadores() {
		return fumadores;
	}

	public void setFumadores(Boolean fumadores) {
		this.fumadores = fumadores;
	}
	
	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}

	@NotNull
	@Valid
	public Negocio getNegocio() {
		return negocio;
	}

	public void setSportCenter(Negocio negocio) {
		this.negocio = negocio;
	}

}