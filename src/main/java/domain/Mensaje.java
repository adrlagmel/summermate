package domain;

import java.time.LocalDateTime;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import jakarta.validation.constraints.NotBlank;
import utilities.validators.SafeHtml;

@Entity
@Table(indexes = {
	@Index(name = "idx_mensaje_carpeta", columnList = "carpeta_id"),
	@Index(name = "idx_mensaje_remitente", columnList = "remitente_id"),
	@Index(name = "idx_mensaje_beneficiario", columnList = "beneficiario_id")
})
@Access(AccessType.PROPERTY)
public class Mensaje extends DomainEntity{
	
	// Constructor ----------------------------
	
	public Mensaje(){
		super();
	}
	
	// Attributes ----------------------------
	
	private LocalDateTime fechaEnvio;
	private String asunto;
	private String cuerpo;
	
	@NotNull
	@Past
	public LocalDateTime getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(LocalDateTime fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	
	@NotBlank
	@SafeHtml
	public String getAsunto() {
		return asunto;
	}
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
	@NotBlank
	@SafeHtml
	public String getCuerpo() {
		return cuerpo;
	}
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	
		
	// Relationships ----------------------------
	
	private Actor remitente;
	private Actor beneficiario;
	private Carpeta carpeta;

	
	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Actor getRemitente() {
		return remitente;
	}
	public void setRemitente(Actor remitente) {
		this.remitente = remitente;
	}
	
	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Actor getBeneficiario() {
		return beneficiario;
	}
	public void setBeneficiario(Actor beneficiario) {
		this.beneficiario = beneficiario;
	}
	
	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	public Carpeta getCarpeta() {
		return carpeta;
	}
	public void setCarpeta(Carpeta carpeta) {
		this.carpeta = carpeta;
	}
	
	
	
	

}
