/* LimiteIntentos.java
 *
 * Frena los intentos de adivinar contraseñas por la API: 5 fallos seguidos
 * con un mismo usuario, o 30 desde una misma IP (varias personas pueden
 * compartirla), y hay que esperar 15 minutos. Vive en memoria, suficiente
 * para una sola instancia de la aplicación. Detrás de un proxy, la IP real
 * solo llega si server.forward-headers-strategy está configurado.
 */

package api;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class LimiteIntentos {

	static final int		MAX_POR_USUARIO	= 5;
	static final int		MAX_POR_IP		= 30;
	static final Duration	BLOQUEO			= Duration.ofMinutes(15);

	private record Fallos(int veces, Instant ultimo) {
	}

	private final Map<String, Fallos> fallos = new ConcurrentHashMap<>();


	public boolean bloqueado(String usuario, String ip) {
		return superado("u:" + usuario.toLowerCase(), MAX_POR_USUARIO) || superado("ip:" + ip, MAX_POR_IP);
	}

	public void fallo(String usuario, String ip) {
		apuntar("u:" + usuario.toLowerCase());
		apuntar("ip:" + ip);
	}

	public void exito(String usuario) {
		fallos.remove("u:" + usuario.toLowerCase());
	}

	// Para los tests
	public void olvidar() {
		fallos.clear();
	}

	private boolean superado(String clave, int maximo) {
		Fallos f = fallos.get(clave);
		return f != null && f.ultimo().isAfter(Instant.now().minus(BLOQUEO)) && f.veces() >= maximo;
	}

	private void apuntar(String clave) {
		Instant ahora = Instant.now();
		Instant limite = ahora.minus(BLOQUEO);
		fallos.merge(clave, new Fallos(1, ahora), (viejo, nuevo) -> viejo.ultimo().isBefore(limite) ? nuevo : new Fallos(viejo.veces() + 1, ahora));
		if (fallos.size() > 10_000)
			fallos.values().removeIf(f -> f.ultimo().isBefore(limite));
	}

}
