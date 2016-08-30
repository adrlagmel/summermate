package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Negocio;
import domain.Playa;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Integer> {
	
	@Query("select distinct s from Negocio s where s.playa.localizacion.ciudad like %?1%")
	Collection<Negocio> search(String s);
	
	@Query("select distinct s from Negocio s where s.empresario.id=?2 and s.playa.localizacion.ciudad like %?1%")
	Collection<Negocio> searchNegocio(String s, int empresarioId);
	
	@Query("select distinct s from Negocio s where s.empresario.id=?1")
	Collection<Negocio> findByEmpresario(int empresarioId);
	
	@Query("select s from Negocio s where s.reservas.size=(select max(s2.reservas.size) from Negocio s2)")
	Collection<Negocio> negocioConMasReservas();
	
	@Query("select distinct n from Negocio n where n.playa.id=?1)")
	Collection<Negocio> NegociosDeLaPlaya(int playaId);
	
	@Query("select distinct n from Negocio n where n.empresario.id=?1 and n.negocioActivo = true)")
	Collection<Negocio> findNegociosActivos(int empresarioId);
	
	@Query("select distinct n from Negocio n where n.valoracionMedia=(select max(valoracionMedia) from Negocio)")
	Collection<Negocio> findNegocioMejorValorado();
	
	@Query("select n from Negocio n where n.reservas.size=(select max(n.reservas.size) from Negocio n)")
	Collection<Negocio> findNegocioMasReservas();

	
}