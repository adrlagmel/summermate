/* Imagen.java
 *
 * Fase 2: las imágenes estaban en una columna byte[] de cada entidad (Negocio,
 * Playa, Evento, Usuario). JPA carga siempre los campos básicos, así que un
 * listado de negocios traía todas sus fotos a memoria aunque no se mostraran.
 * Ahora viven en su propia tabla y las entidades las referencian con un
 * @OneToOne perezoso: los bytes solo se leen al servir la imagen.
 *
 * Fase 8: con un almacén de objetos (Cloudflare R2 u otro compatible con S3)
 * la imagen guarda solo la clave del objeto y los bytes viven fuera de la
 * base de datos. Sin almacén configurado siguen en la columna datos, como
 * antes. Siempre hay una de las dos cosas.
 */

package domain;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@EntityListeners(services.imagenes.BorradoDeImagenes.class)
@Access(AccessType.PROPERTY)
public class Imagen extends DomainEntity {

	private byte[]	datos;
	private String	clave;
	private String	tipo;
	private int		tamano;


	public Imagen() {
	}

	public Imagen(byte[] datos, String tipo) {
		this.datos = datos;
		this.tipo = tipo;
		this.tamano = datos.length;
	}

	// Imagen guardada en el almacén de objetos con esa clave
	public static Imagen enAlmacen(String clave, String tipo, int tamano) {
		Imagen imagen = new Imagen();
		imagen.clave = clave;
		imagen.tipo = tipo;
		imagen.tamano = tamano;
		return imagen;
	}

	// Null cuando la imagen está en el almacén de objetos
	public byte[] getDatos() {
		return datos;
	}

	public void setDatos(byte[] datos) {
		this.datos = datos;
	}

	// Clave del objeto en el almacén (null si los bytes están en la base de datos)
	@Column(unique = true)
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	@AssertTrue(message = "La imagen necesita sus datos o la clave del almacén")
	@Transient
	public boolean isConContenido() {
		return datos != null || clave != null;
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
