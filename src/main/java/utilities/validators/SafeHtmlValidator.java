/* SafeHtmlValidator.java
 *
 * Valida que el texto no contenga HTML, igual que hacía Hibernate Validator con
 * WhiteListType.NONE (usa jsoup con una lista blanca vacía).
 */

package utilities.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class SafeHtmlValidator implements ConstraintValidator<SafeHtml, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return true;

		return Jsoup.isValid(value, Safelist.none());
	}

}
