package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Negocio;
import domain.Playa;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Integer> {
	
	@Query("select distinct s from Negocio s where lower(s.playa.localizacion.ciudad) like lower(concat('%', ?1, '%'))")
	Collection<Negocio> search(String s);
	
	@Query("select distinct s from Negocio s where s.empresario.id=?2 and lower(s.playa.localizacion.ciudad) like lower(concat('%', ?1, '%'))")
	Collection<Negocio> searchNegocio(String s, int empresarioId);
	
	@Query("select distinct s from Negocio s where s.empresario.id=?1")
	Collection<Negocio> findByEmpresario(int empresarioId);
	
	// Mismo cambio que en PlayaRepository.findPlayaMasComentarios: un único
	// GROUP BY en lugar de subconsultas correlacionadas. El dashboard muestra
	// el empresario, así que se trae en la misma consulta.
	@Query("select n from Negocio n join fetch n.empresario where n.id in (select r.negocio.id from Reserva r group by r.negocio.id"
		+ " having count(r) >= all (select count(r2) from Reserva r2 group by r2.negocio.id))")
	Collection<Negocio> negocioConMasReservas();
	
	@Query("select distinct n from Negocio n where n.playa.id=?1")
	Collection<Negocio> NegociosDeLaPlaya(int playaId);
	
	@Query("select distinct n from Negocio n where n.empresario.id=?1 and n.negocioActivo = true")
	Collection<Negocio> findNegociosActivos(int empresarioId);
	
	@Query("select distinct n from Negocio n where n.valoracionMedia=(select max(valoracionMedia) from Negocio)")
	Collection<Negocio> findNegocioMejorValorado();
	
	@Query("select n from Negocio n join fetch n.empresario where n.id in (select r.negocio.id from Reserva r group by r.negocio.id"
		+ " having count(r) >= all (select count(r2) from Reserva r2 group by r2.negocio.id))")
	Collection<Negocio> findNegocioMasReservas();

	// Fase 3: versiones paginadas (Pageable) para los listados ------------

	@Query("select s from Negocio s where lower(s.playa.localizacion.ciudad) like lower(concat('%', ?1, '%'))")
	Page<Negocio> search(String s, Pageable pageable);

	@Query("select s from Negocio s where s.empresario.id = ?1")
	Page<Negocio> findByEmpresario(int empresarioId, Pageable pageable);

	@Query("select n from Negocio n where n.playa.id = ?1")
	Page<Negocio> findByPlaya(int playaId, Pageable pageable);

	// SELECT ... FOR UPDATE: bloquea la fila del negocio hasta que termine la
	// transacción. Lo usa ReservaService para que dos reservas simultáneas no
	// puedan superar juntas el aforo.
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select n from Negocio n where n.id = ?1")
	Negocio findByIdParaReservar(int negocioId);

}
