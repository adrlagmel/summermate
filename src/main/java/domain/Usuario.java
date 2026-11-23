package domain;

import java.util.Collection;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import utilities.validators.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Usuario extends Cliente{

	// Attributes ----------------------------------
	
		private String estadoActual;
		private Imagen	imagen;
		
		// La imagen va en su propia tabla y se carga solo cuando se usa (LAZY).
		// orphanRemoval: al cambiarla o quitarla se borra la anterior.
		@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
		@JoinColumn(unique = true)
		public Imagen getImagen() {
			return imagen;
		}

		public void setImagen(Imagen imagen) {
			this.imagen= imagen;
		}
		
		@SafeHtml
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
		@OneToMany(mappedBy="usuario")
		public Collection<Reserva> getReservas() {
			return reservas;
		}

		public void setReservas(Collection<Reserva> reservas) {
			this.reservas = reservas;
		}
		
		@NotNull
		@ManyToMany(mappedBy="usuarios")
		public Collection<Evento> getEventos() {
			return eventos;
		}

		public void setEventos(Collection<Evento> eventos) {
			this.eventos = eventos;
		}
		
		@NotNull
		@OneToMany(mappedBy="usuario")
		public Collection<ValoracionPlaya> getValoracionPlayas() {
			return valoracionPlayas;
		}

		public void setValoracionPlayas(Collection<ValoracionPlaya> valoracionPlayas) {
			this.valoracionPlayas = valoracionPlayas;
		}
	
}
