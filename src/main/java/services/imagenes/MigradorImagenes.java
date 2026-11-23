/* MigradorImagenes.java
 *
 * Pasa al almacén de objetos las imágenes que todavía están en la base de
 * datos (Fase 8). Se ejecuta al arrancar solo si se pide:
 *   IMAGENES_ALMACEN=s3 IMAGENES_MIGRAR=true java -jar summermate.jar
 * Cada imagen va en su propia transacción: si algo falla a medias, lo ya
 * movido se queda movido y basta con volver a lanzarlo.
 */

package services.imagenes;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import domain.Imagen;

@Component
@ConditionalOnProperty(name = "summermate.imagenes.migrar", havingValue = "true")
@ConditionalOnBean(AlmacenImagenes.class)
public class MigradorImagenes implements ApplicationRunner {

	private static final Logger		log	= LoggerFactory.getLogger(MigradorImagenes.class);

	private final EntityManager		em;
	private final AlmacenImagenes	almacen;
	private final TransactionTemplate	tx;


	public MigradorImagenes(EntityManager em, AlmacenImagenes almacen, TransactionTemplate tx) {
		this.em = em;
		this.almacen = almacen;
		this.tx = tx;
	}

	@Override
	public void run(ApplicationArguments args) {
		List<Integer> pendientes = tx.execute(s -> em.createQuery("select i.id from Imagen i where i.clave is null", Integer.class).getResultList());
		log.info("Imágenes por pasar al almacén: {}", pendientes.size());
		int movidas = migrar(pendientes);
		log.info("Imágenes pasadas al almacén: {}", movidas);
	}

	public int migrar(List<Integer> ids) {
		int movidas = 0;
		for (Integer id : ids) {
			Boolean hecho = tx.execute(s -> {
				Imagen imagen = em.find(Imagen.class, id);
				if (imagen == null || imagen.getClave() != null)
					return false;
				String clave = "imagenes/" + UUID.randomUUID() + switch (imagen.getTipo()) {
					case "image/jpeg" -> ".jpg";
					case "image/png" -> ".png";
					default -> ".webp";
				};
				almacen.guardar(clave, imagen.getDatos(), imagen.getTipo());
				imagen.setClave(clave);
				imagen.setDatos(null);
				return true;
			});
			if (Boolean.TRUE.equals(hecho))
				movidas++;
		}
		return movidas;
	}

}
