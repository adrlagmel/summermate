package repositories;

import java.util.Collection;
import java.time.LocalDateTime;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

	@EntityGraph(attributePaths = { "negocio", "negocio.playa", "negocio.empresario" })
	@Query("select s from Evento s where s.negocio.empresario.id = ?1")
	Page<Evento> findByEmpresario(int empresarioId, Pageable pageable);

	@EntityGraph(attributePaths = { "negocio", "negocio.playa", "negocio.empresario" })
	@Query("select s from Evento s where s.fechaCelebracion >= ?1")
	Page<Evento> findEventosActivos(LocalDateTime fechaActual, Pageable pageable);

	@EntityGraph(attributePaths = { "negocio", "negocio.playa", "negocio.empresario" })
	@Query("select s from Evento s where s.fechaCelebracion <= ?1")
	Page<Evento> findEventosInactivos(LocalDateTime fechaActual, Pageable pageable);

	// Fase 5: las listas muestran negocio y playa de cada evento. Con
	// Hibernate 6 la carga por lotes (default_batch_fetch_size) no llegaba a
	// agrupar estos proxies y salía una consulta por fila, así que se traen
	// en la misma consulta.
	@Override
	@EntityGraph(attributePaths = { "negocio", "negocio.playa", "negocio.empresario" })
	Page<Evento> findAll(Pageable pageable);

}
