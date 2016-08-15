package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CalendarioNegocio;

@Repository
public interface CalendarioNegocioRepository extends JpaRepository<CalendarioNegocio, Integer> {
	
	@Query("select c from CalendarioNegocio c where c.negocio.id=?1")
	Collection<CalendarioNegocio> findByNegocio(int negocioId);
	
	@Query("select scc from CalendarioNegocio scc where scc.negocio.id=?3 and (?1 BETWEEN scc.fechaInicio AND scc.fechaFin or ?2 BETWEEN scc.fechaInicio AND scc.fechaFin)")
    Collection<CalendarioNegocio> findCalendarioNegocioPorFechaDeReserva(Date fechaInicio, Date fechaFin, int negocioId);
	
	@Query("select coalesce(SUM(r.comensales),0) from Reserva r where r.negocio.id=?3 and r.negocio.id=(select distinct scc.negocio.id from CalendarioNegocio scc where scc.negocio.id = ?3 and (?1 BETWEEN scc.fechaInicio AND scc.fechaFin or ?2 BETWEEN scc.fechaInicio AND scc.fechaFin))")
    Integer findComensalesPorFechaDeReserva(Date fechaInicio, Date fechaFin, int negocioId);
}
