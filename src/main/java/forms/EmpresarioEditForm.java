package forms;

import jakarta.validation.constraints.NotBlank;
import utilities.validators.NifCif;
import domain.Sexo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import jakarta.validation.constraints.Email;
import utilities.validators.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

public class EmpresarioEditForm {
	
	private String nombre;
	private String apellidos;
	private String email;
		
	private String telefono;
	private LocalDate   fechaNacimiento;
	private String nacionalidad;
	private String direccion;
	private Sexo sexo;
	
	private String  cif;
	
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
	@NotBlank
	@Email
	@SafeHtml
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotBlank
	@Pattern(regexp = "^\\+\\d{2,3}\\d{7,14}$")
	@SafeHtml
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	@NotNull
	@Past
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	public LocalDate getFechaNacimiento(){
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento(LocalDate fechaNacimiento){
		this.fechaNacimiento = fechaNacimiento;
	}
	
	
	@NotBlank
	@SafeHtml
	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	@NotBlank
	@SafeHtml
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	@NotBlank
	@NifCif
	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}	
	
}
