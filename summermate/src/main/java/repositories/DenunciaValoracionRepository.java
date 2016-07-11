package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.DenunciaValoracion;

@Repository
public interface DenunciaValoracionRepository extends JpaRepository<DenunciaValoracion, Integer> {
	
}