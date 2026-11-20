package forms;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

public class UsuarioEditForm {
	
//	private String username;
//	
//	private String password;
//	private String verifyPassword;
	
	private String nombre;
	private String apellidos;
	private String email;
		
	private String telefono;
	private LocalDate   fechaNacimiento;
	private String nacionalidad;
	private String direccion;
	private String sexo;
	
	private String  estadoActual;
	private Integer nivelColaborador;
	private Integer puntos;
	
//	@Size(min = 5, max = 32)
//	@Column(unique = true)
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	@Size(min = 5, max = 32)
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	@Size(min = 5, max = 32)
//	public String getVerifyPassword() {
//		return verifyPassword;
//	}
//
//	public void setVerifyPassword(String verifyPassword) {
//		this.verifyPassword = verifyPassword;
//	}
		
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
	
	@NotBlank
	@SafeHtml
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
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
	@SafeHtml
	public String getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}	
	
	@Min(0)
	public Integer getNivelColaborador() {
		return nivelColaborador;
	}

	public void setNivelColaborador(Integer nivelColaborador) {
		this.nivelColaborador = nivelColaborador;
	}
	
	@Min(0)
	public Integer getPuntos() {
		return puntos;
	}

	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}

}
