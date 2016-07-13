package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Usuario extends Cliente{

	// Attributes ----------------------------------
	
		private String estadoActual;
		private Integer nivelColaborador;
		private Integer puntos; 
		
		@SafeHtml(whitelistType = WhiteListType.NONE)
		public String getEstadoActual() {
			return estadoActual;
		}

		public void setEstadoActual(String estadoActual) {
			this.estadoActual = estadoActual;
		}
		//Atributo Derivado que ser� un contador de puntos de colaboraci�n
		@Min(0)
		public Integer getNivelColaborador() {
			return nivelColaborador;
		}

		public void setNivelColaborador(Integer nivelColaborador) {
			this.nivelColaborador = nivelColaborador;
		}
		@Min(0)
		public Integer getPuntos() {
			return puntos;
		}

		public void setPuntos(Integer puntos) {
			this.puntos = puntos;
		}

				
		// Relationships -------------------------------
		
		private Collection <Reserva> reservas;
		private Collection <Evento> eventos;
		
		@NotNull
		@Valid
		@OneToMany(mappedBy="usuario")
		public Collection<Reserva> getReservas() {
			return reservas;
		}

		public void setReservas(Collection<Reserva> reservas) {
			this.reservas = reservas;
		}
		
		@NotNull
		@Valid
		@OneToMany(mappedBy="usuario")
		public Collection<Evento> getEventos() {
			return eventos;
		}

		public void setEventos(Collection<Evento> eventos) {
			this.eventos = eventos;
		}
		
		
}
