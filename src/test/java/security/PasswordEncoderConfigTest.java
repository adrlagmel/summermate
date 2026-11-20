package security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Test unitario (sin Spring ni base de datos) del codificador de contraseñas.
 */
public class PasswordEncoderConfigTest {

	// md5("admin"): así se guardaban las contraseñas en la versión original.
	private static final String LEGACY_MD5 = "21232f297a57a5a743894a0e4a801fc3";

	private final PasswordEncoder encoder = new PasswordEncoderConfig().passwordEncoder();

	@Test
	public void lasContrasenasNuevasSeGuardanConBcrypt() {
		String hash = encoder.encode("admin");

		assertTrue(hash.startsWith("{bcrypt}$2a$"));
		assertTrue(encoder.matches("admin", hash));
		assertFalse(encoder.matches("otra", hash));
	}

	@Test
	public void cadaHashLlevaSuPropiaSal() {
		assertFalse(encoder.encode("admin").equals(encoder.encode("admin")));
	}

	@Test
	public void losHashesMd5AntiguosSiguenSiendoValidos() {
		assertTrue(encoder.matches("admin", LEGACY_MD5));
		assertFalse(encoder.matches("otra", LEGACY_MD5));
	}

	@Test
	public void soloLosHashesAntiguosNecesitanMigrarse() {
		assertTrue(encoder.upgradeEncoding(LEGACY_MD5));
		assertFalse(encoder.upgradeEncoding(encoder.encode("admin")));
	}
}
