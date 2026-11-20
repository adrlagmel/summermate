package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Mesa;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer> {

	@Query("select m from Mesa m where m.negocio.id = ?1 order by m.capacidad, m.nombre")
	List<Mesa> findByNegocio(int negocioId);

}
