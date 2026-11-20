package repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Cierre;

@Repository
public interface CierreRepository extends JpaRepository<Cierre, Integer> {

	@Query("select count(c) > 0 from Cierre c where c.negocio.id = ?1 and c.fecha = ?2")
	boolean cerrado(int negocioId, LocalDate fecha);

	@Query("select c from Cierre c where c.negocio.id = ?1 and c.fecha >= ?2 order by c.fecha")
	List<Cierre> proximos(int negocioId, LocalDate desde);

}
