package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Carpeta;

@Repository
public interface CarpetaRepository extends JpaRepository<Carpeta, Integer> {
	
	@Query("select f from Actor a join a.carpetas f where a.id=?1 and f.nombre='Entrada'")
	Carpeta entradaCarpeta(int actorId);
	
	@Query("select f from Actor a join a.carpetas f where a.id=?1 and f.nombre='Enviados'")
	Carpeta salidaCarpeta(int actorId);
	 
//	@Query("select f from Actor a join a.folders f where a.id=?1 and f.name='Papelera'")
//	Carpeta papeleraCarpeta(int actorId);
	 
	@Query("select a.carpetas from Actor a where a.id=?1")
	Collection<Carpeta> findCarpetasByActor(int actorId);
}
