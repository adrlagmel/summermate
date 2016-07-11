package forms;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

public class UsuarioRegistroForm {
	
	private String estadoActual;
	private Integer nivelColaborador;
	private Integer puntos;

	private RegistroForm registroForm;

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}	
	
	@NotNull
	@Valid
	public RegistroForm getRegistroForm() {
		return registroForm;
	}

	public void setRegistroForm(RegistroForm registroForm) {
		this.registroForm = registroForm;
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

