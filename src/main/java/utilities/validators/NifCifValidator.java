/* NifCifValidator.java
 *
 * Comprueba el carácter de control de un CIF, NIF o NIE según las reglas de
 * la Agencia Tributaria.
 */

package utilities.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NifCifValidator implements ConstraintValidator<NifCif, String> {

	private static final String	LETRAS_NIF	= "TRWAGMYFPDXBNJZSQVHLCKE";
	private static final String	LETRAS_CIF	= "JABCDEFGHI";

	@Override
	public boolean isValid(String valor, ConstraintValidatorContext context) {
		if (valor == null || valor.isBlank())
			return true;
		return esValido(valor);
	}

	public static boolean esValido(String valor) {
		String v = valor.trim().toUpperCase().replace("-", "").replace(" ", "");

		if (v.matches("[0-9]{8}[A-Z]"))
			return letraNif(v.substring(0, 8)) == v.charAt(8);

		if (v.matches("[XYZ][0-9]{7}[A-Z]"))
			return letraNif("XYZ".indexOf(v.charAt(0)) + v.substring(1, 8)) == v.charAt(8);

		if (v.matches("[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]"))
			return controlCif(v);

		return false;
	}

	private static char letraNif(String numero) {
		return LETRAS_NIF.charAt(Integer.parseInt(numero) % 23);
	}

	private static boolean controlCif(String v) {
		String digitos = v.substring(1, 8);
		int suma = 0;
		for (int i = 0; i < 7; i++) {
			int d = digitos.charAt(i) - '0';
			if (i % 2 == 0) {
				int doble = d * 2;
				suma += doble / 10 + doble % 10;
			} else
				suma += d;
		}
		int control = (10 - suma % 10) % 10;
		char tipo = v.charAt(0);
		char dado = v.charAt(8);

		boolean soloLetra = "NPQRSW".indexOf(tipo) >= 0;
		boolean soloNumero = "ABEH".indexOf(tipo) >= 0;

		boolean esLetra = dado == LETRAS_CIF.charAt(control);
		boolean esNumero = dado == (char) ('0' + control);

		if (soloLetra)
			return esLetra;
		if (soloNumero)
			return esNumero;
		return esLetra || esNumero;
	}

	// Calcula el carácter de control que corresponde a un CIF sin él (para los
	// datos de ejemplo y los tests).
	public static String completarCif(String sinControl) {
		for (char c : "0123456789ABCDEFGHIJ".toCharArray())
			if (esValido(sinControl + c))
				return sinControl + c;
		throw new IllegalArgumentException(sinControl);
	}

}
