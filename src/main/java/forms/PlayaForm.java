/* PlayaForm.java
 *
 * Campos editables de una playa. Antes se enlazaba la entidad entera y, como
 * el formulario no enviaba ni la imagen ni la valoración media, cada edición
 * las dejaba a null. La localización es un valor embebido y se edita completa.
 */

package forms;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import utilities.validators.SafeHtml;
import domain.Localizacion;
import domain.Playa;

public class PlayaForm extends EntityForm {

	private String			nombre;
	private String			descripcion;
	private String			composicion;
	private String			servicios;
	private Integer			extension;
	private Localizacion	localizacion;

	public PlayaForm() {
		localizacion = new Localizacion();
	}

	public PlayaForm(Playa playa) {
		copyIdentity(playa);
		nombre = playa.getNombre();
		descripcion = playa.getDescripcion();
		composicion = playa.getComposicion();
		servicios = playa.getServicios();
		extension = playa.getExtension();
		localizacion = playa.getLocalizacion();
	}

	@NotBlank
	@SafeHtml
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@NotBlank
	@SafeHtml
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@NotBlank
	@SafeHtml
	public String getComposicion() {
		return composicion;
	}

	public void setComposicion(String composicion) {
		this.composicion = composicion;
	}

	@NotBlank
	@SafeHtml
	public String getServicios() {
		return servicios;
	}

	public void setServicios(String servicios) {
		this.servicios = servicios;
	}

	@NotNull
	@Min(0)
	public Integer getExtension() {
		return extension;
	}

	public void setExtension(Integer extension) {
		this.extension = extension;
	}

	@NotNull
	@Valid
	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}

}
