/* Assert.java
 *
 * Fase 4: Spring 6 eliminó Assert.notNull(Object) y Assert.isTrue(boolean)
 * sin mensaje, que el proyecto usa en cientos de sitios para las reglas de
 * negocio. Esta clase los mantiene con el mismo comportamiento (lanza
 * IllegalArgumentException) para no cambiar la lógica en la migración.
 *
 * Pendiente para la Fase 6: sustituirlos por excepciones de negocio propias
 * con mensajes que se puedan mostrar al usuario.
 */

package utilities;

import java.util.Collection;

public final class Assert {

	private Assert() {
	}

	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static void notNull(Object object, String message) {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void isNull(Object object) {
		isTrue(object == null, "[Assertion failed] - the object argument must be null");
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression)
			throw new IllegalArgumentException(message);
	}

	public static void notEmpty(Collection<?> collection) {
		isTrue(collection != null && !collection.isEmpty(), "[Assertion failed] - this collection must not be empty");
	}

}
