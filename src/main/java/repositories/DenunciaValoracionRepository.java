package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DenunciaValoracion;

@Repository
public interface DenunciaValoracionRepository extends JpaRepository<DenunciaValoracion, Integer> {
	
	@Query("select distinct d from DenunciaValoracion d where d.cliente.id=?1")
	Collection<DenunciaValoracion> findDenunciasByEmpresario(int empresarioId);
}