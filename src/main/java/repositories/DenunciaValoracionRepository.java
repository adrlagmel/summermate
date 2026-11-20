package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.DenunciaValoracion;

@Repository
public interface DenunciaValoracionRepository extends JpaRepository<DenunciaValoracion, Integer> {
	
	@Query("select distinct d from DenunciaValoracion d where d.cliente.id=?1")
	Collection<DenunciaValoracion> findDenunciasByEmpresario(int empresarioId);
	
	@Query("select count(d) > 0 from DenunciaValoracion d where d.cliente.id = ?1 and d.valoracionNegocio.id = ?2")
	boolean yaDenunciada(int clienteId, int valoracionNegocioId);

	// Moderación: todas, con lo que pinta la lista en la misma consulta.
	@Query("select d from DenunciaValoracion d join fetch d.cliente join fetch d.valoracionNegocio v join fetch v.reserva r join fetch r.negocio join fetch r.usuario order by v.id, d.id")
	List<DenunciaValoracion> findAllParaModerar();

	@Query("select distinct d from DenunciaValoracion d where d.valoracionNegocio.id=?1")
	Collection<DenunciaValoracion> findDenunciasByValoracion(int valoracionNegocioId);
	
	
	
}