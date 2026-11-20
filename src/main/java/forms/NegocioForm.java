/* NegocioForm.java
 *
 * Campos que un empresario puede editar de su negocio. No incluye empresario,
 * negocioActivo, valoracionMedia, imagen ni colecciones: esos los gestiona el
 * servicio. (Antes, al no enviarse la imagen, cada edición la borraba.)
 */

package forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import utilities.validators.SafeHtml;
import domain.Negocio;
import domain.Playa;

public class NegocioForm extends EntityForm {

	private String	nombre;
	private String	tipo;
	private String	descripcion;
	private String	telefono;
	private String	paginaWeb;
	private Integer	aforo;
	private Playa	playa;

	public NegocioForm() {
	}

	public NegocioForm(Negocio negocio) {
		copyIdentity(negocio);
		nombre = negocio.getNombre();
		tipo = negocio.getTipo();
		descripcion = negocio.getDescripcion();
		telefono = negocio.getTelefono();
		paginaWeb = negocio.getPaginaWeb();
		aforo = negocio.getAforo();
		playa = negocio.getPlaya();
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
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	@Pattern(regexp = "^\\+\\d{2,3}\\d{7,14}$")
	@SafeHtml
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@URL
	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	@NotNull
	@Range(min = 0, max = 200)
	public Integer getAforo() {
		return aforo;
	}

	public void setAforo(Integer aforo) {
		this.aforo = aforo;
	}

	public Playa getPlaya() {
		return playa;
	}

	public void setPlaya(Playa playa) {
		this.playa = playa;
	}

}
