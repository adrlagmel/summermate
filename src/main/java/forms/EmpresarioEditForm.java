package forms;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

public class EmpresarioEditForm {
	
	private String nombre;
	private String apellidos;
	private String email;
		
	private String telefono;
	private Date   fechaNacimiento;
	private String nacionalidad;
	private String direccion;
	private String sexo;
	
	private String  cif;
	
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
	@NotBlank
	@Email
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotBlank
	@Pattern(regexp = "^\\+\\d{2,3}\\d{7,14}$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
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
	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}	
	
}
