package domain;

import javax.persistence.Access;

import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Embeddable
@Access(AccessType.PROPERTY)
public class Localizacion{

	// Attributes ----------------------------
	
	private String 	via;
	private String 	nombreVia;
	private Integer numeroVia;
	private Integer codigoPostal;
	private String 	ciudad;
	private String 	provincia;
	private double  latitud;
	private double  longitud;
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getVia() {
		return via;
	}
	
	public void setVia(String via) {
		this.via = via;
	}
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getNombreVia() {
		return nombreVia;
	}
	
	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}
	
	@Min(0)
	public Integer getNumeroVia() {
		return numeroVia;
	}
	
	public void setNumeroVia(Integer numeroVia) {
		this.numeroVia = numeroVia;
	}
	
	@NotNull
	@Range(min=1000, max=99999)
	public Integer getCodigoPostal() {
		return codigoPostal;
	}
	
	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getCiudad() {
		return ciudad;
	}
	
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	@NotBlank
	@SafeHtml(whitelistType=WhiteListType.NONE)
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	
	@Range(min = -90, max = 90)
	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	@Range(min = -180, max = 180)
	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	
	
}
