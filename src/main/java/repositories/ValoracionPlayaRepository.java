package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.ValoracionPlaya;

@Repository
public interface ValoracionPlayaRepository extends JpaRepository<ValoracionPlaya, Integer> {
	
}
