/* EstadoPeticion.java
 *
 * Estados de una petición de negocio. Antes era un String validado con una
 * expresión regular ("^PENDIENTE|RECHAZADO|ACEPTADO$", que además estaba mal:
 * sin paréntesis, aceptaba cualquier texto que empezara por PENDIENTE). Un
 * error tipográfico como "ACEPTDO" compilaba y fallaba en silencio.
 */

package domain;

public enum EstadoPeticion {

	PENDIENTE, ACEPTADO, RECHAZADO

}
