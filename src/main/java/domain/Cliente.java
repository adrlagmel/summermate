	package domain;

import java.util.Collection;

import java.time.LocalDate;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Cliente extends Actor{
	
	private String 		telefono;
	private LocalDate 		fechaNacimiento;
	private String 		nacionalidad;
	private String		direccion;
	private Sexo		sexo;
	
	
	@NotBlank
	@Pattern(regexp = "^\\+\\d{2,3}\\d{7,14}$")
	@SafeHtml
	public String getTelefono(){
		return telefono;
	}
	public void setTelefono(String telefono){
		this.telefono = telefono;
	}
	
	@NotNull
	@Past
	public LocalDate getFechaNacimiento(){
		return fechaNacimiento;
	}
	public void setFechaNacimiento(LocalDate fechaNacimiento){
		this.fechaNacimiento = fechaNacimiento;
	}
	
	@NotBlank
	@SafeHtml
	public String getNacionalidad(){
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad){
		this.nacionalidad = nacionalidad;
	}
	
	@NotBlank
	@SafeHtml
	public String getDireccion(){
		return direccion;
	}
	public void setDireccion(String direccion){
		this.direccion = direccion;
	}
	
	@Enumerated(EnumType.STRING)
	public Sexo getSexo(){
		return sexo;
	}
	public void setSexo(Sexo sexo){
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
