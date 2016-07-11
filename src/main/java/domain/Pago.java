package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Access(AccessType.PROPERTY)
public class Pago extends DomainEntity{
	
	// Constructor --------------------
	
		public Pago() {
			super();
			
		}
		
	// Attributes ----------------------------
		
	private String 	concepto;
	private Date 	fecha;
	private Double	precio;
	
	@NotBlank
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	//Derivado del atributo precio de la clase Reserva
	@Digits(integer=5,fraction=2)
	@Min(0)
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	
	private Reserva 	reserva;
	private Empresario 	empresario;
	
	@Valid
	@OneToOne(optional=true)
	public Reserva getReserva() {
		return reserva;
	}
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	@Valid
	@ManyToOne(optional=true)
	public Empresario getEmpresario() {
		return empresario;
	}
	public void setEmpresario(Empresario empresario) {
		this.empresario = empresario;
	}
	
}
