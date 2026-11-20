package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Reserva;

@Repository
// Fase 3: Reserva.valoracionNegocio es el lado inverso (mappedBy) de un
// @OneToOne y Hibernate no puede cargarlo de forma perezosa: sin el
// "left join fetch" lanzaba una consulta extra por cada reserva listada.
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
	
	@Query("select b from Reserva b left join fetch b.valoracionNegocio join fetch b.negocio where b.negocio.id=?1 order by b.fecha asc")
	Collection<Reserva> findReservasPorNegocio(int id);
	
	@Query("select b from Reserva b left join fetch b.valoracionNegocio join fetch b.negocio where b.negocio.empresario.id=?1 order by b.fecha asc")
	Collection<Reserva> findReservasPorEmpresario(int id);
	
	@Query("select b from Reserva b left join fetch b.valoracionNegocio join fetch b.negocio where b.usuario.id=?1 order by b.fecha asc")
	Collection<Reserva> findReservasPorUsuario(int usuarioId);
	
	// Reservas de un empresario en el intervalo [desde, hasta). Antes usaba
	// DATE_FORMAT, que es exclusivo de MySQL y además impedía usar índices.
	@Query("select distinct s from Reserva s left join fetch s.valoracionNegocio join fetch s.negocio where s.negocio.empresario.id = ?1 and s.fecha >= ?2 and s.fecha < ?3")
	Collection<Reserva> search(int empresarioId, Date desde, Date hasta);

	// Fase 3: versiones paginadas (Pageable) para los listados ------------

	// Con "join fetch" Spring Data no sabe derivar el recuento: se le da aparte.
	@Query(value = "select b from Reserva b left join fetch b.valoracionNegocio join fetch b.negocio where b.negocio.empresario.id = ?1",
		countQuery = "select count(b) from Reserva b where b.negocio.empresario.id = ?1")
	Page<Reserva> findReservasPorEmpresario(int id, Pageable pageable);

	@Query(value = "select b from Reserva b left join fetch b.valoracionNegocio join fetch b.negocio where b.usuario.id = ?1",
		countQuery = "select count(b) from Reserva b where b.usuario.id = ?1")
	Page<Reserva> findReservasPorUsuario(int usuarioId, Pageable pageable);

	@Query(value = "select s from Reserva s left join fetch s.valoracionNegocio join fetch s.negocio where s.negocio.empresario.id = ?1 and s.fecha >= ?2 and s.fecha < ?3",
		countQuery = "select count(s) from Reserva s where s.negocio.empresario.id = ?1 and s.fecha >= ?2 and s.fecha < ?3")
	Page<Reserva> search(int empresarioId, Date desde, Date hasta, Pageable pageable);

}
