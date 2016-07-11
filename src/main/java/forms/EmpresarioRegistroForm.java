package forms;

import javax.validation.Valid;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;


public class EmpresarioRegistroForm {
	
	private String cif;


	private RegistroForm registroForm;

	@Pattern(regexp = "^\\w+$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
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

