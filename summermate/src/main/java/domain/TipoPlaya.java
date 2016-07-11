package domain;

import javax.persistence.Access;

import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

@Embeddable
@Access(AccessType.PROPERTY)
public class TipoPlaya {


	// Constructor ------------------------------------------------------
	
	public TipoPlaya() {

	}
	
	// Attributes -------------------------------------------------------
	
	private String 	composicion;
	private String 	condiciones;
	private String 	ocupacion;
	private String 	servicios;
	private String 	transporte;
	private String 	acceso;
	private Double 	longitud;
	private Double 	anchura;
	private Boolean paseoMaritimo;
	private Boolean zonaFondeo;
	private Boolean vegetacion;
	private Boolean espacioProtegido;
	
	
	
	public String getComposicion() {
		return composicion;
	}

	public void setComposicion(String composicion) {
		this.composicion = composicion;
	}

	@NotBlank
	public String getCondiciones() {
		return condiciones;
	}

	public void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}
	
	public String getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	
	public String getServicios() {
		return servicios;
	}

	public void setServicios(String servicios) {
		this.servicios = servicios;
	}
	
	public String getTransporte() {
		return transporte;
	}

	public void setTransporte(String transporte) {
		this.transporte = transporte;
	}
	
	public String getAcceso() {
		return acceso;
	}

	public void setAcceso(String acceso) {
		this.acceso = acceso;
	}
	
	public Boolean getPaseoMaritimo() {
		return paseoMaritimo;
	}

	public void setPaseoMaritimo(Boolean paseoMaritimo) {
		this.paseoMaritimo = paseoMaritimo;
	}
	
	public Boolean getZonaFondeo() {
		return zonaFondeo;
	}

	public void setZonaFondeo(Boolean zonaFondeo) {
		this.zonaFondeo = zonaFondeo;
	}
	
	public Boolean getVegetacion() {
		return vegetacion;
	}

	public void setVegetacion(Boolean vegetacion) {
		this.vegetacion = vegetacion;
	}
	
	public Boolean getEspacioProtegido() {
		return espacioProtegido;
	}

	public void setEspacioProtegido(Boolean espacioProtegido) {
		this.espacioProtegido = espacioProtegido;
	}
	
	@NotNull
	@Digits(integer=5, fraction=2)
	@Min(0)
	public Double getLongitud() {
		return longitud;
	}
	
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	
	@NotNull
	@Digits(integer=5, fraction=2)
	@Min(0)
	public Double getAnchura() {
		return anchura;
	}
	
	public void setAnchura(Double anchura) {
		this.anchura = anchura;
	}



}
