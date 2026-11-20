package domain;

import java.util.Collection;

import java.time.LocalDateTime;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import utilities.validators.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class ValoracionNegocio extends DomainEntity{
	
	// Constructor --------------------
	
	public ValoracionNegocio() {
		super();
		
	}
	
	// Attributes --------------------
	
	private String 	titulo;
	private LocalDateTime 	fecha;
	private Integer puntuacion;
	private String 	comentario;
	private Integer comentarioUtil;
	
	@NotBlank
	@SafeHtml
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@NotNull
	public LocalDateTime getFecha() {
		return fecha;
	}
	
	public void setFecha(LocalDateTime fecha) {
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
	
	@NotBlank
	@SafeHtml
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
	@OneToOne(fetch = FetchType.LAZY, optional= false)
	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	
	@Valid
	@OneToMany(mappedBy="valoracionNegocio")
	public Collection<DenunciaValoracion> getDenuncias() {
		return denuncias;
	}

	public void setDenuncias(Collection<DenunciaValoracion> denuncias) {
		this.denuncias = denuncias;
	}
	
	


}
