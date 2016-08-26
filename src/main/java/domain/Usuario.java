package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Usuario extends Cliente{

	// Attributes ----------------------------------
	
		private String estadoActual;
		private byte[]	imagen;
		
		@Column(columnDefinition = "LONGBLOB")
		public byte[] getImagen() {
			return imagen;
		}

		public void setImagen(byte[] imagen) {
			this.imagen= imagen;
		}
		
		@SafeHtml(whitelistType = WhiteListType.NONE)
		public String getEstadoActual() {
			return estadoActual;
		}

		public void setEstadoActual(String estadoActual) {
			this.estadoActual = estadoActual;
		}
		//Atributo Derivado que será un contador de puntos de colaboración
				
		// Relationships -------------------------------
		
		private Collection <Reserva> reservas;
		private Collection <Evento> eventos;
		private Collection <ValoracionPlaya> valoracionPlayas;
		
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
		@ManyToMany(mappedBy="usuarios")
		public Collection<Evento> getEventos() {
			return eventos;
		}

		public void setEventos(Collection<Evento> eventos) {
			this.eventos = eventos;
		}
		
		@NotNull
		@Valid
		@OneToMany(mappedBy="usuario")
		public Collection<ValoracionPlaya> getValoracionPlayas() {
			return valoracionPlayas;
		}

		public void setValoracionPlayas(Collection<ValoracionPlaya> valoracionPlayas) {
			this.valoracionPlayas = valoracionPlayas;
		}
	
}
