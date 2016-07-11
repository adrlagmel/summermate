package repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Mesa;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer> {
	
	@Query("select m from Mesa m where m.negocio.id=?1")
	Collection<Mesa> findByNegocio(int negocioId);
	
	@Query("select m from Mesa m where m.negocio.empresario.id=?1")
	Collection<Mesa> findMesasPorEmpresario(int empresarioId);
}
