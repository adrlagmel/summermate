/* PasswordEncoderConfig.java
 *
 * Codificador de contraseñas de la aplicación.
 *
 * Las contraseñas nuevas se guardan con BCrypt y con el prefijo {bcrypt}.
 * Los hashes antiguos (MD5 sin sal y sin prefijo) se siguen aceptando al
 * iniciar sesión, y DaoAuthenticationProvider los vuelve a guardar en BCrypt
 * gracias a LoginService.updatePassword (migración transparente).
 */

package security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

	public static final String CURRENT = "bcrypt";

	@Bean
	public PasswordEncoder passwordEncoder() {
		Map<String, PasswordEncoder> encoders;
		DelegatingPasswordEncoder result;

		encoders = new HashMap<String, PasswordEncoder>();
		encoders.put(CURRENT, new BCryptPasswordEncoder());

		result = new DelegatingPasswordEncoder(CURRENT, encoders);
		// Hashes sin prefijo: los MD5 de la versión original del proyecto.
		result.setDefaultPasswordEncoderForMatches(legacyMd5());

		return result;
	}

	// MD5 está obsoleto (por eso Spring lo marca @Deprecated); solo se usa
	// para comprobar contraseñas antiguas, nunca para guardar nuevas.
	@SuppressWarnings("deprecation")
	private static PasswordEncoder legacyMd5() {
		return new MessageDigestPasswordEncoder("MD5");
	}

}
