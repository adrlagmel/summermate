/* Correo.java
 *
 * Aviso por correo ya redactado. Se publica como evento dentro de la
 * transacción y se envía solo si esta se confirma (ver Cartero).
 */

package services.correo;

public record Correo(String para, String asunto, String cuerpo) {
}
