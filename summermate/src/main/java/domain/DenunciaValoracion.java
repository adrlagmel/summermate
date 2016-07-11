package domain;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class DenunciaValoracion extends DomainEntity {

	// Constructor ----------------------------
	public DenunciaValoracion (){
		super();
	}
	
	// Attributes ----------------------------
	
	private String 	titulo;
	private String 	comentario;
	private String 	tipo;
	
	@NotBlank
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@NotBlank
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	@NotBlank
	@Pattern(regexp="^FALSEDAD|RECHAZO|BURLA$")
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	// Relationships -------------------------
	
	private ValoracionNegocio valoracionNegocio;
	private Cliente cliente;

	@Valid
	@ManyToOne(optional=false)
	public ValoracionNegocio getValoracionNegocio() {
		return valoracionNegocio;
	}
	public void setValoracionNegocio(ValoracionNegocio valoracionNegocio) {
		this.valoracionNegocio = valoracionNegocio;
	}
	
	@Valid
	@ManyToOne(optional=false)
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	
	
}
