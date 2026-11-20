/* SafeHtml.java
 *
 * Sustituye a org.hibernate.validator.constraints.SafeHtml, que Hibernate
 * Validator eliminó en la versión 6.2. Equivale al antiguo
 * @SafeHtml: no admite ninguna etiqueta HTML.
 */

package utilities.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {SafeHtmlValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface SafeHtml {

	// Misma clave que la anotación original, para reutilizar las traducciones.
	String message() default "{org.hibernate.validator.constraints.SafeHtml.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
