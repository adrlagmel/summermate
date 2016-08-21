	package domain;

import java.util.Collection;

import java.util.Date;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Cliente extends Actor{
	
	private String 		telefono;
	private Date 		fechaNacimiento;
	private String 		nacionalidad;
	private String		direccion;
	private String		sexo;
	
	
	@NotBlank
	@Pattern(regexp = "^\\+\\d{2,3}\\d{7,14}$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTelefono(){
		return telefono;
	}
	public void setTelefono(String telefono){
		this.telefono = telefono;
	}
	
	@NotNull
	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	public Date getFechaNacimiento(){
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento){
		this.fechaNacimiento = fechaNacimiento;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNacionalidad(){
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad){
		this.nacionalidad = nacionalidad;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDireccion(){
		return direccion;
	}
	public void setDireccion(String direccion){
		this.direccion = direccion;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSexo(){
		return sexo;
	}
	public void setSexo(String sexo){
		this.sexo = sexo;
	}
	
	// Relationships ------------------------------------------------
	
	
	private Collection <DenunciaValoracion> denuncias;

	@NotNull
	@Valid
	@OneToMany(mappedBy="cliente")
	public Collection<DenunciaValoracion> getDenuncias() {
		return denuncias;
	}
	public void setDenuncias(Collection<DenunciaValoracion> denuncias) {
		this.denuncias = denuncias;
	}
	
	
}
