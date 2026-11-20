package repositories;

import java.util.Collection;
import java.time.LocalDateTime;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Query("select distinct s from Evento s where s.fechaCelebracion >= ?1")
	Collection<Evento> findEventosActivos(LocalDateTime fechaActual);
	
	@Query("select distinct s from Evento s where s.fechaCelebracion <= ?1")
	Collection<Evento> findEventosInactivos(LocalDateTime fechaActual);

	// Fase 3: versiones paginadas (Pageable) para los listados ------------

	@Query("select s from Evento s where s.negocio.empresario.id = ?1")
	Page<Evento> findByEmpresario(int empresarioId, Pageable pageable);

	@Query("select s from Evento s where s.fechaCelebracion >= ?1")
	Page<Evento> findEventosActivos(LocalDateTime fechaActual, Pageable pageable);

	@Query("select s from Evento s where s.fechaCelebracion <= ?1")
	Page<Evento> findEventosInactivos(LocalDateTime fechaActual, Pageable pageable);

}
