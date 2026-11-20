package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.PagoPremium;

@Repository
public interface PagoPremiumRepository extends JpaRepository<PagoPremium, Integer> {

	@Query("select p from PagoPremium p where p.referencia = ?1")
	PagoPremium findByReferencia(String referencia);

	@Query("select p from PagoPremium p where p.empresario.id = ?1 order by p.fecha desc")
	List<PagoPremium> findByEmpresario(int empresarioId);

}
