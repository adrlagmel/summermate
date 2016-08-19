package repositories;

import java.util.Collection;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Evento;
import domain.Usuario;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
	
	@Query("select distinct s from Evento s where s.negocio.empresario.id=?1")
	Collection<Evento> findByEmpresario(int empresarioId);
	
	@Query("select s.eventos from Usuario s where s.id=?1")
	Collection<Evento> findByUsuario(int usuarioId);
	
	@Query("select s.usuarios from Evento s where s.id=?1")
	Collection<Usuario> findParticipantsByEvento(int eventoId);
	
}