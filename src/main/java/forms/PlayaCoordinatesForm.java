package forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import domain.Playa;



public class PlayaCoordinatesForm {
	
	private Playa playa;
	
	private Double longitud;
	private Double latitud;
	
	@NotNull
	public Playa getPlaya(){
		return playa;
	}
	
	public void setPlaya(Playa playa){
		this.playa = playa;
	}
	
	@Range(min = -90, max = 90)
	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	
	@Range(min = -180, max = 180)
	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	

}
