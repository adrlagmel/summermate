package domain;

import java.util.Collection;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class ValoracionNegocio extends DomainEntity{
	
	// Constructor --------------------
	
	public ValoracionNegocio() {
		super();
		
	}
	
	// Attributes --------------------
	
	private String 	titulo;
	private Date 	fecha;
	private Integer puntuacion;
	private String 	comentario;
	private Integer 	comentarioUtil;
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@NotNull
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	@NotNull
	@Range(min=0, max=10)
	public Integer getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(Integer puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	@Min(0)
	public Integer getComentarioUtil() {
		return comentarioUtil;
	}

	public void setComentarioUtil(Integer comentarioUtil) {
		this.comentarioUtil = comentarioUtil;
	}
	
	
	// Relationships ------------------------------------------------
	
	

	private Reserva reserva;
	private Collection <DenunciaValoracion> denuncias;
	
	@Valid
	@OneToOne(optional= false)
	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	@NotNull
	@Valid
	@OneToMany(mappedBy="valoracionNegocio")
	public Collection<DenunciaValoracion> getDenuncias() {
		return denuncias;
	}

	public void setDenuncias(Collection<DenunciaValoracion> denuncias) {
		this.denuncias = denuncias;
	}
	
	


}
