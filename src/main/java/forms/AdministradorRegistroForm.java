package forms;

import javax.persistence.Column;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;

public class AdministradorRegistroForm {
	
	private String username;
	private String password;
	private String verifyPassword;
	
	private String nombre;
	private String apellidos;
	private String email;

	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Size(min = 5, max = 32)
	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}
		
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

	
	

}
