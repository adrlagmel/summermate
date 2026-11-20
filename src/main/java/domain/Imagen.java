/* Imagen.java
 *
 * Fase 2: las imágenes estaban en una columna byte[] de cada entidad (Negocio,
 * Playa, Evento, Usuario). JPA carga siempre los campos básicos, así que un
 * listado de negocios traía todas sus fotos a memoria aunque no se mostraran.
 * Ahora viven en su propia tabla y las entidades las referencian con un
 * @OneToOne perezoso: los bytes solo se leen al servir la imagen.
 */

package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Access(AccessType.PROPERTY)
public class Imagen extends DomainEntity {

	private byte[]	datos;
	private String	tipo;
	private int		tamano;


	public Imagen() {
	}

	public Imagen(byte[] datos, String tipo) {
		this.datos = datos;
		this.tipo = tipo;
		this.tamano = datos.length;
	}

	@NotNull
	public byte[] getDatos() {
		return datos;
	}

	public void setDatos(byte[] datos) {
		this.datos = datos;
	}

	// Tipo MIME detectado a partir del contenido (ver services.ImagenService).
	@NotNull
	@Pattern(regexp = "^image/(jpeg|png|webp)$")
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Min(1)
	public int getTamano() {
		return tamano;
	}

	public void setTamano(int tamano) {
		this.tamano = tamano;
	}

}
