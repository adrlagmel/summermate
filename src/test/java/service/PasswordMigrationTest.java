package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import services.AdministradorService;
import utilities.AbstractTest;
import domain.Administrador;

/*
 * Migración de contraseñas: un usuario con un hash MD5 de la versión original
 * inicia sesión y su hash se reescribe en BCrypt. El proveedor se monta igual
 * que en spring/config/security.xml.
 */
@Transactional
public class PasswordMigrationTest extends AbstractTest {

	// md5("usuario1"), tal y como estaba en PopulateDatabase.xml
	private static final String LEGACY_MD5 = "122b738600a0f74f7c331c0ef59bc34c";

	@Autowired
	private LoginService loginService;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AdministradorService administradorService;

	private DaoAuthenticationProvider provider;

	@BeforeEach
	public void configurarProveedor() {
		provider = new DaoAuthenticationProvider(loginService);
		provider.setUserDetailsPasswordService(loginService);
		provider.setPasswordEncoder(passwordEncoder);

		UserAccount account = userAccountRepository.findByUsername("usuario1");
		account.setPassword(LEGACY_MD5);
		userAccountRepository.saveAndFlush(account);
	}

	@Test
	public void unLoginCorrectoMigraElHashABcrypt() {
		provider.authenticate(new UsernamePasswordAuthenticationToken("usuario1", "usuario1"));

		String stored = userAccountRepository.findByUsername("usuario1").getPassword();
		assertTrue(stored.startsWith("{bcrypt}"));
		assertTrue(passwordEncoder.matches("usuario1", stored));
	}

	@Test
	public void unLoginFallidoNoTocaElHash() {
		try {
			provider.authenticate(new UsernamePasswordAuthenticationToken("usuario1", "mala"));
		} catch (BadCredentialsException expected) {
			assertEquals(LEGACY_MD5, userAccountRepository.findByUsername("usuario1").getPassword());
			return;
		}
		throw new AssertionError("Debería rechazar la contraseña");
	}

	@Test
	public void losDatosDeEjemploYaUsanBcrypt() {
		for (UserAccount account : userAccountRepository.findAll())
			if (!account.getUsername().equals("usuario1"))
				assertTrue(account.getPassword().startsWith("{bcrypt}"), account.getUsername());
	}

	@Test
	public void unRegistroNuevoGuardaLaContrasenaConBcrypt() {
		unauthenticate();
		Administrador admin = administradorService.create();
		admin.getUserAccount().setUsername("adminNuevo");
		admin.getUserAccount().setPassword("secreta");
		admin.setNombre("Nombre");
		admin.setApellidos("Apellidos");
		admin.setEmail("nuevo@test.test");
		administradorService.save(admin);

		String stored = userAccountRepository.findByUsername("adminNuevo").getPassword();
		assertTrue(stored.startsWith("{bcrypt}"));
		assertTrue(passwordEncoder.matches("secreta", stored));
	}
}
