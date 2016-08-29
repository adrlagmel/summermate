package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Playa;
import domain.ValoracionNegocio;

@Repository
public interface PlayaRepository extends JpaRepository<Playa, Integer> {
	
	@Query("select distinct p from Playa p where p.valoracionMedia=(select max(valoracionMedia) from Playa)")
	Collection<Playa> findPlayaMejorValorada();

}