package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
	
	@Query("select b from Reserva b where b.negocio.id=?1 order by b.fecha asc")
	Collection<Reserva> findReservasPorNegocio(int id);
	
	@Query("select b from Reserva b where b.usuario.id=?1 order by b.fecha asc")
	Collection<Reserva> findReservasPorUsuario(int usuarioId);
	
	
}
