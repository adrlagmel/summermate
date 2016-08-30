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
	
	@Query("select p from Playa p where p.valoracionPlayas.size=(select max(p.valoracionPlayas.size) from Playa p)")
	Collection<Playa> findPlayaMasComentarios();
	
	@Query("select max(p.valoracionPlayas.size) from Playa p")
	Integer masValoraciones();

}