/* PasarelaSimulada.java
 *
 * Pasarela para desarrollo y demostración: la "página de pago" es una vista
 * de la propia aplicación con los botones Pagar y Cancelar. No cobra nada.
 * Para cobrar de verdad hace falta otra implementación (por ejemplo, Stripe)
 * y unas claves.
 */

package services.pagos;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import domain.PagoPremium;

@Component
@ConditionalOnProperty(name = "summermate.pagos.pasarela", havingValue = "simulada", matchIfMissing = true)
public class PasarelaSimulada implements PasarelaPago {

	@Override
	public String iniciar(PagoPremium pago) {
		return "/empresario/premium/pagos/" + pago.getReferencia();
	}

	@Override
	public boolean confirmaDesdeLaAplicacion() {
		return true;
	}

}
