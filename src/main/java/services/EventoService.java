package services;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EventoRepository;
import security.LoginService;
import security.UserAccount;
import utilities.validators.RandomString;
import domain.Evento;
import domain.Empresario;
import domain.Usuario;

@Service
@Transactional
public class EventoService {
	
	// Managed repository ----------------------------------------------------
	
	@Autowired
	private EventoRepository eventoRepository;
	
	// Supporting services ---------------------------------------------------
	
	@Autowired
	private EmpresarioService empresarioService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private NegocioService negocioService;
	
	// Constructor -----------------------------------------------------------
	
	public EventoService(){
		
	}
	
	// Simple CRUD Methods ---------------------------------------------------
	
	public Evento create(){
		Evento result = new Evento();
		
		RandomString rs = new RandomString(6);
		String randomStr = rs.nextString();
		
		Assert.isTrue(negocioService.findByEmpresario().size()>0);
		result.setCodigo(randomStr);
		
		result.setUsuarios(new ArrayList<Usuario>());
		result.setFechaRegistro(new Date(System.currentTimeMillis()));

		return result;
	}
	
	public void save(Evento evento){
		checkPrincipal(evento);
		
		evento.setFechaRegistro(new Date(System.currentTimeMillis()));
		Assert.isTrue(evento.getFechaCelebracion().compareTo(new Date()) >= 0);
		Assert.isTrue(evento.getNegocio().getNegocioActivo());
		
		eventoRepository.save(evento);
	}
	
	public Collection<Evento> findAll(){
		
		Collection<Evento> result = eventoRepository.findAll();
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Evento> EventosActivos(){
		Date ahora = new Date();
		Collection<Evento> result = eventoRepository.findEventosActivos(ahora);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Evento> EventosInactivos(){
		Date ahora = new Date();
		Collection<Evento> result = eventoRepository.findEventosInactivos(ahora);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Evento findOneToEdit(int eventoId){
		Evento evento = eventoRepository.findOne(eventoId);
				
		checkPrincipal(evento);
		Assert.isTrue(evento.getFechaCelebracion().compareTo(new Date()) >= 0);
		
		return evento;
	}
	
	public Evento findOne(int eventoId){		
		return eventoRepository.findOne(eventoId);
	}
	
	public void participate(Evento evento, Usuario usuario) {
		Date fechaActual = new Date();
		Assert.isTrue(!evento.getUsuarios().contains(usuario));
		Assert.isTrue(evento.getFechaCelebracion().after(fechaActual));
		
		usuario.getEventos().add(evento);
		evento.getUsuarios().add(usuario);
		
		eventoRepository.save(evento);		
		
	}
	
	public void unregister(Evento evento, Usuario usuario) {
		Date fechaActual = new Date();
		Assert.isTrue(evento.getUsuarios().contains(usuario));
		Assert.isTrue(evento.getFechaCelebracion().after(fechaActual));
		
		usuario.getEventos().remove(evento);
		evento.getUsuarios().remove(usuario);
		
		eventoRepository.save(evento);		
		
	}
	
	// Other business methods ------------------------------------------------
		
//	public Collection<Evento> searchByTown(String town){
//		
//		Collection<Evento> result = eventoRepository.search(town);
//		Assert.notNull(result);
//		
//		return result;
//		
//	}
	
//	public Collection<Evento> searchByTownAndSportPartner(String ciudad){
//		Empresario empresario = empresarioService.findByPrincipal();
//		
//		Collection<Evento> result = eventoRepository.searchEvento(ciudad, empresario.getId());
//		Assert.notNull(result);
//		
//		return result;
//		
//	}
	
	public Evento findOneToDisplay(int eventoId){
		
		Evento result = eventoRepository.findOne(eventoId);
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Evento> findByEmpresario(){
		Empresario empresario = empresarioService.findByPrincipal();
		
		Collection<Evento> result = eventoRepository.findByEmpresario(empresario.getId());
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Usuario> findParticipantsByEvento(int eventId){		
		Collection<Usuario> result = eventoRepository.findParticipantsByEvento(eventId);
		
		checkPrincipal(findOneToDisplay(eventId));
		Assert.notNull(result);
		
		return result;
		
	}
	
	public Collection<Evento> findByUsuario(){
		
		Usuario usuario = usuarioService.findByPrincipal();
		
		Collection<Evento> result = eventoRepository.findByUsuario(usuario.getId());
		Assert.notNull(result);
		
		return result;
		
	}
	
	public void checkPrincipal(Evento evento) {
		Assert.notNull(evento);
		
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.isTrue(evento.getNegocio().getEmpresario().getUserAccount().equals(userAccount));
	}
	
	public void addImageToEvento(int eventoId, byte[] bytes) {
		
		Evento e = findOneToEdit(eventoId);
		if(bytes.length==0){
		
			e.setImagen(null);
		}else{
			e.setImagen(bytes);
		}
		
		save(e);
	}
	
}
