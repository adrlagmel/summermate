package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import utilities.validators.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

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
	
	private Date fechaEnvio;
	private String asunto;
	private String cuerpo;
	
	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
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
	@ManyToOne(optional=false)
	public Actor getRemitente() {
		return remitente;
	}
	public void setRemitente(Actor remitente) {
		this.remitente = remitente;
	}
	
	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public Actor getBeneficiario() {
		return beneficiario;
	}
	public void setBeneficiario(Actor beneficiario) {
		this.beneficiario = beneficiario;
	}
	
	@NotNull
	@Valid
	@ManyToOne(optional=false)
	public Carpeta getCarpeta() {
		return carpeta;
	}
	public void setCarpeta(Carpeta carpeta) {
		this.carpeta = carpeta;
	}
	
	
	
	

}
