package services;

import java.util.ArrayList;




import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import utilities.Assert;

import repositories.NegocioRepository;
import security.LoginService;
import security.UserAccount;
import domain.CalendarioNegocio;
import domain.Empresario;
import domain.Evento;
import domain.Negocio;
import domain.PeticionNegocio;
import domain.Reserva;
import domain.EstadoPeticion;
import forms.NegocioForm;

@Service
@Transactional
public class NegocioService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private NegocioRepository negocioRepository;
	
	// Supporting services ---------------------------------------------------

	@Autowired
	private ImagenService imagenService;
	
	@Autowired
	private EmpresarioService empresarioService;
	
	@Autowired
	private PeticionNegocioService peticionNegocioService;
	
	@Autowired
	private AdministradorService administradorService;
	// Constructor -----------------------------------------------------------
	
	public NegocioService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Negocio create(){
		Negocio result = new Negocio();
		
		Empresario principal = empresarioService.findByPrincipal();
		
		PeticionNegocio peticionNegocio = peticionNegocioService.findByEmpresarioId(principal.getId());
		Assert.notNull(peticionNegocio);
		Assert.isTrue(peticionNegocio.getEstado() == EstadoPeticion.ACEPTADO);
		
		result.setNegocioActivo(true);
		
		result.setReservas(new ArrayList<Reserva>());
		result.setCalendarioNegocios(new ArrayList<CalendarioNegocio>());
		result.setEventos(new ArrayList<Evento>());
		result.setEmpresario(principal);
		
		return result;
	}
	
	// Copia en la entidad solo los campos editables del formulario.
	public Negocio reconstruct(NegocioForm form){
		Negocio result;

		if(form.isNew()){
			result = create();
		}else{
			result = findOneToEdit(form.getId());
			form.checkVersion(result);
		}

		result.setNombre(form.getNombre());
		result.setTipo(form.getTipo());
		result.setDescripcion(form.getDescripcion());
		result.setTelefono(form.getTelefono());
		result.setPaginaWeb(form.getPaginaWeb());
		result.setAforo(form.getAforo());
		result.setPlaya(form.getPlaya());

		return result;
	}

	public void save(Negocio negocio){
		checkPrincipal(negocio);
		
		PeticionNegocio peticionNegocio = peticionNegocioService.findByEmpresarioId(negocio.getEmpresario().getId());
		Assert.notNull(peticionNegocio);
		
		if(peticionNegocio != null)
			Assert.isTrue(peticionNegocio.getEstado() == EstadoPeticion.ACEPTADO);
		
		negocioRepository.save(negocio);
	}
	
	public void suspenderNegocio(Negocio negocio){
		checkPrincipal(negocio);
		Assert.notNull(negocio);
		
		negocio.setNegocioActivo(false);
		
		negocioRepository.save(negocio);	
	}
	
	public void altaNegocio(Negocio negocio){
		checkPrincipal(negocio);
		Assert.notNull(negocio);
		
		negocio.setNegocioActivo(true);
		
		negocioRepository.save(negocio);	
	}
	
	public Collection<Negocio> findAll(){
		Collection<Negocio> result = negocioRepository.findAll();
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> findNegociosActivos(int empresarioId){
		Collection<Negocio> result = negocioRepository.findNegociosActivos(empresarioId);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> findNegociosDePlaya(int playaId){
		Collection<Negocio> result = negocioRepository.NegociosDeLaPlaya(playaId);
		Assert.notNull(result);
		
		return result;
		
	}
	
	
	public Negocio findOneToEdit(int negocioId){
		Negocio negocio = negocioRepository.findById(negocioId).orElse(null);
		checkPrincipal(negocio);
		return negocio;
	}
	
	// Other business methods ------------------------------------------------
	public Collection<Negocio> negocioConMasReservas(){
		administradorService.checkPrincipal();
		Collection<Negocio> result = negocioRepository.negocioConMasReservas();
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> searchByTown(String town){
		
		Collection<Negocio> result = negocioRepository.search(town);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> searchByTownAndSportPartner(String ciudad){
		Empresario empresario = empresarioService.findByPrincipal();
		
		Collection<Negocio> result = negocioRepository.searchNegocio(ciudad, empresario.getId());
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Negocio findOneToDisplay(int negocioId){
		
		Negocio result = negocioRepository.findById(negocioId).orElse(null);
		Assert.notNull(result);
		
		return result;
		
	}
	public Collection<Negocio> findNegocioMejorValorado(){
				
		Collection<Negocio> result = negocioRepository.findNegocioMejorValorado();
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Negocio> negociosConMasReservas(){
		
		Collection<Negocio> result = negocioRepository.findNegocioMasReservas();
		Assert.notNull(result);
		
		return result;
		
	}
	
	
	public Collection<Negocio> findByEmpresario(){
		
		Empresario empresario = empresarioService.findByPrincipal();
		
		Collection<Negocio> result = negocioRepository.findByEmpresario(empresario.getId());
		Assert.notNull(result);
		
		return result;
		
	}
	

	public void checkPrincipal(Negocio negocio) {
		Assert.notNull(negocio);
		
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(negocio.getEmpresario().getUserAccount().equals(userAccount));
	}
	
	
	// Una subida vacía quita la imagen; si no es JPEG, PNG o WebP,
	// ImagenService.crear lanza IllegalArgumentException.
	public void addImageToNegocio(int negocioId, byte[] bytes) {
		Negocio n  = findOneToEdit(negocioId);
		n.setImagen(imagenService.crear(bytes));

		save(n);
	}

	// Fase 3: listados paginados en la base de datos ---------------------

	public Page<Negocio> findAll(Pageable pageable){
		return negocioRepository.findAll(pageable);
	}

	public Page<Negocio> findNegociosDePlaya(int playaId, Pageable pageable){
		return negocioRepository.findByPlaya(playaId, pageable);
	}

	public Page<Negocio> searchByTown(String town, Pageable pageable){
		return negocioRepository.search(town, pageable);
	}

	public Page<Negocio> findByEmpresario(Pageable pageable){
		Empresario empresario = empresarioService.findByPrincipal();

		return negocioRepository.findByEmpresario(empresario.getId(), pageable);
	}

	// Ver NegocioRepository.findByIdParaReservar
	public Negocio bloquearParaReservar(int negocioId){
		Negocio result = negocioRepository.findByIdParaReservar(negocioId);
		Assert.notNull(result);

		return result;
	}

	// Fase 5: de las playas indicadas, las que tienen algún negocio
	public java.util.Set<Integer> playasConNegocios(java.util.Collection<Integer> playaIds) {
		if (playaIds.isEmpty())
			return java.util.Collections.emptySet();
		return negocioRepository.findPlayasConNegocios(playaIds);
	}

}
