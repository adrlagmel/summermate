package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import domain.ValoracionPlaya;

@Repository
public interface ValoracionPlayaRepository extends JpaRepository<ValoracionPlaya, Integer> {
	
	@Query("select distinct v from ValoracionPlaya v where v.cliente.id=?1")
	Collection<ValoracionPlaya> findValoracionPlayaByUsuario(int usuarioId);
	
	@Query("select distinct v from ValoracionPlaya v where v.playa.id=?1")
	Collection<ValoracionPlaya> findValoracionPlayaByPlaya(int playaId);
}
