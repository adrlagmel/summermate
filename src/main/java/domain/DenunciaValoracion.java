package domain;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;

@Entity
@Table(indexes = {
	@Index(name = "idx_denunciavaloracion_cliente", columnList = "cliente_id"),
	@Index(name = "idx_denunciavaloracion_valoracionnegocio", columnList = "valoracionNegocio_id")
})
@Access(AccessType.PROPERTY)
public class DenunciaValoracion extends DomainEntity {

	// Constructor ----------------------------
	public DenunciaValoracion (){
		super();
	}
	
	// Attributes ----------------------------
	
	private String 	titulo;
	private String 	comentario;
	private TipoDenuncia 	tipo;
	
	@NotBlank
	@SafeHtml
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@NotBlank
	@SafeHtml
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	// Se guarda como texto (no como número de orden) para que añadir o
	// reordenar valores no cambie el significado de los datos existentes.
	@NotNull
	@Enumerated(EnumType.STRING)
	public TipoDenuncia getTipo() {
		return tipo;
	}
	public void setTipo(TipoDenuncia tipo) {
		this.tipo = tipo;
	}
	
	// Relationships -------------------------
	
	private ValoracionNegocio valoracionNegocio;
	private Cliente cliente;

	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public ValoracionNegocio getValoracionNegocio() {
		return valoracionNegocio;
	}
	public void setValoracionNegocio(ValoracionNegocio valoracionNegocio) {
		this.valoracionNegocio = valoracionNegocio;
	}
	
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
