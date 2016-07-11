package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Pregunta extends DomainEntity{
	
	// Constructor ----------------------------------
	
	public Pregunta(){
		super();
	}
	
	// Attributes ----------------------------------
	
	private String 	titulo;
	private String 	comentarios;
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	
	
	// Relationships -------------------------
	
	private Usuario usuario;
	private Negocio negocio;

	@Valid
	@ManyToOne(optional=false)
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@Valid
	@NotNull
	@OneToOne(optional=false)
	public Negocio getNegocio() {
		return negocio;
	}
	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
	

}
