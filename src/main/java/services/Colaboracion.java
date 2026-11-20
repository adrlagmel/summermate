/* Colaboracion.java
 *
 * Nivel de colaboración de un usuario (memoria del TFG). Los puntos no se
 * guardan: se calculan de lo que ha aportado, así no se descuadran si borra
 * una valoración.
 *   10 por valoración de playa o de negocio
 *    2 por cada voto de "útil" que reciben sus valoraciones
 *    5 por reserva ya disfrutada
 */

package services;

public record Colaboracion(int valoracionesPlaya, int valoracionesNegocio, int votosUtiles, int reservasPasadas) {

	public enum Nivel {
		TURISTA(0), VERANEANTE(50), LOCAL(150), EMBAJADOR(400);

		public final int desde;

		Nivel(int desde) {
			this.desde = desde;
		}
	}


	public int puntos() {
		return 10 * (valoracionesPlaya + valoracionesNegocio) + 2 * votosUtiles + 5 * reservasPasadas;
	}

	public Nivel nivel() {
		Nivel result = Nivel.TURISTA;
		for (Nivel n : Nivel.values())
			if (puntos() >= n.desde)
				result = n;
		return result;
	}

	// Puntos que faltan para el siguiente nivel (0 en el último)
	public int faltan() {
		Nivel actual = nivel();
		if (actual.ordinal() == Nivel.values().length - 1)
			return 0;
		return Nivel.values()[actual.ordinal() + 1].desde - puntos();
	}

}
