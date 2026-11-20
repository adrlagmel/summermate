/* SubidaImagen.java
 *
 * Datos que necesita la plantilla subir-imagen.html (común a playas,
 * negocios, eventos y foto de perfil).
 */

package controllers;

public record SubidaImagen(String titulo, String accion, String tipo, int id, boolean tiene, String volver) {

	public static final String	VISTA	= "subir-imagen";

}
