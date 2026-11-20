/* NifCif.java
 *
 * Identificador fiscal español válido: CIF de empresa, NIF de autónomo o NIE,
 * con su carácter de control. Un valor vacío se da por bueno; para exigirlo
 * se combina con @NotBlank.
 */

package utilities.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NifCifValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NifCif {

	String message() default "{utilities.validators.NifCif.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
