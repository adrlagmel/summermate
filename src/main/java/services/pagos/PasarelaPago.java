/* PasarelaPago.java
 *
 * Pasarela de pago de la cuenta premium. La aplicación nunca ve datos de
 * tarjeta: crea el pago, manda al empresario a la página de la pasarela y
 * espera su confirmación. Con Stripe Checkout, iniciar() crearía la sesión de
 * pago y devolvería su URL, y la confirmación llegaría por webhook.
 */

package services.pagos;

import domain.PagoPremium;

public interface PasarelaPago {

	// URL a la que se redirige al empresario para pagar
	String iniciar(PagoPremium pago);

	// Si la propia aplicación puede dar el pago por bueno (solo la simulada)
	boolean confirmaDesdeLaAplicacion();

}
