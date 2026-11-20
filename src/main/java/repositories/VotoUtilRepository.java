package repositories;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.VotoUtil;

@Repository
public interface VotoUtilRepository extends JpaRepository<VotoUtil, Integer> {

	@Query("select v from VotoUtil v where v.cliente.id = ?1 and v.valoracionNegocio.id = ?2")
	VotoUtil find(int clienteId, int valoracionNegocioId);

	@Query("select count(v) from VotoUtil v where v.valoracionNegocio.id = ?1")
	int contar(int valoracionNegocioId);

	// Al borrar una valoración o un cliente, sus votos se borran en la base de
	// datos (on delete cascade, V5).

	// Ids de las valoraciones (de la lista que se pinta) que el cliente ya votó
	@Query("select v.valoracionNegocio.id from VotoUtil v where v.cliente.id = ?1 and v.valoracionNegocio.id in ?2")
	Set<Integer> votadas(int clienteId, Collection<Integer> valoraciones);


}
