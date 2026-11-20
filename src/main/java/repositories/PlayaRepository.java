package repositories;

import java.util.Collection;

import java.util.List;
import services.PuntoMapa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Playa;
import domain.ValoracionNegocio;

@Repository
public interface PlayaRepository extends JpaRepository<Playa, Integer> {
	
	@Query("select distinct p from Playa p where p.valoracionMedia=(select max(valoracionMedia) from Playa)")
	Collection<Playa> findPlayaMejorValorada();
	
	// Fase 3: antes, para cada playa se contaban sus valoraciones y se
	// comparaba con el máximo, que a su vez contaba las de todas las playas
	// (subconsultas correlacionadas: coste cuadrático). Ahora se agrupa una
	// sola vez las valoraciones por playa.
	@Query("select p from Playa p where p.id in (select v.playa.id from ValoracionPlaya v group by v.playa.id"
		+ " having count(v) >= all (select count(v2) from ValoracionPlaya v2 group by v2.playa.id))")
	Collection<Playa> findPlayaMasComentarios();
	
	// La primera fila es el máximo (se pide con PageRequest.of(0, 1))
	@Query("select count(v) from ValoracionPlaya v group by v.playa.id order by count(v) desc")
	List<Long> valoracionesPorPlaya(Pageable pageable);

	// Fase 3: versiones paginadas (Pageable) para los listados ------------

	// Solo lo necesario para el mapa (sin cargar las entidades completas)
	@Query("select new services.PuntoMapa(p.id, p.nombre, p.localizacion.latitud, p.localizacion.longitud) from Playa p")
	List<PuntoMapa> findPuntosMapa();

	// Playas a menos de ?5 km de (?1, ?2), de la más cercana a la más lejana.
	// El recuadro (?3 grados de latitud, ?4 de longitud) descarta enseguida
	// las lejanas; la fórmula de Haversine (radio terrestre: 6371 km) da la
	// distancia exacta sobre la esfera. Antes se cargaban todas las playas en
	// memoria y se comparaban con un umbral de 10.000.000 km: devolvía todas.
	@Query("select p from Playa p"
		+ " where p.localizacion.latitud between ?1 - ?3 and ?1 + ?3"
		+ " and p.localizacion.longitud between ?2 - ?4 and ?2 + ?4"
		+ " and " + DISTANCIA_KM + " <= ?5"
		+ " order by " + DISTANCIA_KM)
	List<Playa> findCercanas(double latitud, double longitud, double margenLatitud, double margenLongitud, double radioKm);

	String DISTANCIA_KM = "2 * 6371 * asin(sqrt("
		+ "sin(radians(p.localizacion.latitud - ?1) / 2) * sin(radians(p.localizacion.latitud - ?1) / 2)"
		+ " + cos(radians(?1)) * cos(radians(p.localizacion.latitud))"
		+ " * sin(radians(p.localizacion.longitud - ?2) / 2) * sin(radians(p.localizacion.longitud - ?2) / 2)))";

	// Fase 5: el administrador de cada playa en la misma consulta
	@Override
	@EntityGraph(attributePaths = { "administrador" })
	org.springframework.data.domain.Page<Playa> findAll(org.springframework.data.domain.Pageable pageable);

}
