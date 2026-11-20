package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
	
	@Query("select b from Reserva b where b.negocio.id=?1 order by b.fecha asc")
	Collection<Reserva> findReservasPorNegocio(int id);
	
	@Query("select b from Reserva b where b.negocio.empresario.id=?1 order by b.fecha asc")
	Collection<Reserva> findReservasPorEmpresario(int id);
	
	@Query("select b from Reserva b where b.usuario.id=?1 order by b.fecha asc")
	Collection<Reserva> findReservasPorUsuario(int usuarioId);
	
	// Reservas de un empresario en el intervalo [desde, hasta). Antes usaba
	// DATE_FORMAT, que es exclusivo de MySQL y además impedía usar índices.
	@Query("select distinct s from Reserva s where s.negocio.empresario.id = ?1 and s.fecha >= ?2 and s.fecha < ?3")
	Collection<Reserva> search(int empresarioId, Date desde, Date hasta);
	
}
