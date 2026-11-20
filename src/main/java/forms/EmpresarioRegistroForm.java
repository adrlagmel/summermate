package forms;

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import utilities.validators.SafeHtml;


public class EmpresarioRegistroForm {
	
	private String cif;


	private RegistroForm registroForm;

	@Pattern(regexp = "^\\w+$")
	@SafeHtml
	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}
	
	@NotNull
	@Valid
	public RegistroForm getRegistroForm() {
		return registroForm;
	}

	public void setRegistroForm(RegistroForm registroForm) {
		this.registroForm = registroForm;
	}
	
}

