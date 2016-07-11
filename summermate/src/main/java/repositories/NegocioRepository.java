package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Negocio;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Integer> {
	
	@Query("select distinct s from Negocio s where s.localizacion.ciudad like %?1%")
	Collection<Negocio> search(String s);
	
	@Query("select distinct s from Negocio s where s.empresario.id=?2 and s.localizacion.ciudad like %?1%")
	Collection<Negocio> searchNegocio(String s, int empresarioId);
	
	@Query("select distinct s from Negocio s where s.empresario.id=?1")
	Collection<Negocio> findByEmpresario(int empresarioId);
	
	@Query("select s from Negocio s where s.reservas.size=(select max(s2.reservas.size) from Negocio s2)")
	Collection<Negocio> negocioConMasReservas();
	
}