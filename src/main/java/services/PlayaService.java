package services;

import java.util.ArrayList;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.Assert;

import repositories.PlayaRepository;
import security.LoginService;
import security.UserAccount;

import domain.Administrador;
import domain.Localizacion;
import domain.Negocio;
import domain.Playa;
import domain.ValoracionPlaya;
import forms.PlayaForm;

@Service
@Transactional
public class PlayaService {
	
	@Autowired
	private PlayaRepository playaRepository;

	@Autowired
	private ImagenService imagenService;
	
	@Autowired
	private AdministradorService administradorService;

	
	public PlayaService(){
		super();
	}
	
	public Playa create() {
		Playa playa = new Playa();
		Administrador administrador 				= administradorService.findByPrincipal();	
		Collection<ValoracionPlaya> valoracionPlayas = new ArrayList<ValoracionPlaya>();
		
		playa.setLocalizacion(new Localizacion());
		playa.setNegocios(new ArrayList<Negocio>());
		playa.setValoracionPlayas(valoracionPlayas);
		playa.setAdministrador(administrador);
		playa.setValoracionMedia(0.0);
		
		return playa;
	}
	
	public Collection<Playa> findAllBeaches(){
		Collection<Playa> playas = playaRepository.findAll();
		
		return playas;
	}

	public Playa findOneToEdit(int playaId) {
		Playa result = playaRepository.findById(playaId).orElse(null);
					
		return result;
	}
	
	public Playa findOne(int playaId) {
		Playa result = playaRepository.findById(playaId).orElse(null);
					
		return result;
	}
	
	// Copia en la entidad solo los campos editables del formulario.
	public Playa reconstruct(PlayaForm form){
		Playa result;

		if(form.isNew()){
			result = create();
		}else{
			result = findOneToEdit(form.getId());
			checkPrincipal(result);
			form.checkVersion(result);
		}

		result.setNombre(form.getNombre());
		result.setDescripcion(form.getDescripcion());
		result.setComposicion(form.getComposicion());
		result.setServicios(form.getServicios());
		result.setExtension(form.getExtension());
		result.setLocalizacion(form.getLocalizacion());

		return result;
	}

	public void save(Playa playa){	
		checkPrincipal(playa);
		
		playaRepository.save(playa);
	}

	public void delete(Playa playa){
		Assert.notNull(playa);
		Assert.isTrue(playa.getNegocios().size()<1);
		
		playaRepository.delete(playa);
	}
	
	public void checkPrincipal(Playa playa) {
		Assert.notNull(playa);
		
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(playa.getAdministrador().getUserAccount().equals(userAccount));
	}
	
	// Una subida vacía quita la imagen; si no es JPEG, PNG o WebP,
	// ImagenService.crear lanza IllegalArgumentException.
	public void addImageToFoto(int playaId, byte[] bytes) {
		Playa p = findOne(playaId);
		p.setImagen(imagenService.crear(bytes));

		save(p);
	}
	
	public static final double	RADIO_POR_DEFECTO_KM	= 25;
	public static final double	RADIO_MAXIMO_KM			= 200;

	public Collection<Playa> nearToMe(Double lat, Double lon) {
		return nearToMe(lat, lon, RADIO_POR_DEFECTO_KM);
	}

	// Playas a menos de radioKm, de la más cercana a la más lejana.
	public List<Playa> nearToMe(double lat, double lon, double radioKm) {
		double margenLatitud, margenLongitud;

		Assert.isTrue(lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180);
		Assert.isTrue(radioKm > 0 && radioKm <= RADIO_MAXIMO_KM);

		// Un grado de latitud son unos 111 km; uno de longitud, menos cuanto
		// más lejos del ecuador (se multiplica por el coseno de la latitud).
		margenLatitud = radioKm / 111.0;
		margenLongitud = radioKm / (111.0 * Math.max(Math.cos(Math.toRadians(lat)), 0.01));

		return playaRepository.findCercanas(lat, lon, margenLatitud, margenLongitud, radioKm);
	}
	
	
	public Collection<Playa> playaMejorValorada() {
		Collection<Playa> res = playaRepository.findPlayaMejorValorada();
		Assert.notNull(res);
		return res;
		
	}
	
	public Collection<Playa> findPlayaMasComentarios() {
		Collection<Playa> res = playaRepository.findPlayaMasComentarios();
		Assert.notNull(res);
		return res;
		
	}
	
	public Integer masValoraciones(){
		List<Long> maximo = playaRepository.valoracionesPorPlaya(PageRequest.of(0, 1));

		return maximo.isEmpty() ? 0 : maximo.get(0).intValue();
	}
	
	
	

	// Fase 3: listados paginados en la base de datos ---------------------

	public Page<Playa> findAll(Pageable pageable){
		return playaRepository.findAll(pageable);
	}

	public List<PuntoMapa> findPuntosMapa(){
		return playaRepository.findPuntosMapa();
	}

}
