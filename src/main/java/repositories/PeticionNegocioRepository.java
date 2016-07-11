package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PeticionNegocio;

@Repository
public interface PeticionNegocioRepository extends JpaRepository<PeticionNegocio, Integer> {
	
	@Query("select pn from PeticionNegocio pn where pn.empresario.id=?1")
	PeticionNegocio findByEmpresarioId(int sportPartnerId);
	
	@Query("select pn from PeticionNegocio pn where pn.estado='PENDIENTE'")
	Collection<PeticionNegocio> findPeticionesNegociosPendientes();
	
}
