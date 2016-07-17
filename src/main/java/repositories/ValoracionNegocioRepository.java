package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ValoracionNegocio;

@Repository
public interface ValoracionNegocioRepository extends JpaRepository<ValoracionNegocio, Integer> {
	@Query("select distinct v from ValoracionNegocio v where v.reserva.usuario.id=?1")
	Collection<ValoracionNegocio> findValoracionNegocioByUsuario(int usuarioId);
	
	@Query("select distinct v from ValoracionNegocio v where v.reserva.negocio.id=?1")
	Collection<ValoracionNegocio> findValoracionNegocioByNegocio(int negocioId);
	
	
}
