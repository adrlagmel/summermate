package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.ValoracionNegocio;

@Repository
public interface ValoracionNegocioRepository extends JpaRepository<ValoracionNegocio, Integer> {
	
}
