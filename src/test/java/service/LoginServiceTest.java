package service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;
import security.LoginService;
import utilities.AbstractTest;

/*
 * Tests de caracterización: fijan el comportamiento actual del login para
 * que no cambie sin querer durante la migración.
 */
@Transactional
public class LoginServiceTest extends AbstractTest {

	@Autowired
	private LoginService loginService;

	@Test
	public void cargaUnUsuarioExistenteConSusRoles() {
		UserDetails admin = loginService.loadUserByUsername("admin");

		assertEquals("admin", admin.getUsername());
		assertTrue(tieneRol(admin, Authority.ADMININISTRADOR));
	}

	@Test
	public void cadaTipoDeActorTieneSuRol() {
		assertTrue(tieneRol(loginService.loadUserByUsername("usuario1"), Authority.USUARIO));
		assertTrue(tieneRol(loginService.loadUserByUsername("empresario1"), Authority.EMPRESARIO));
	}

	// Hoy un usuario inexistente se rechaza con un Assert de Spring.
	@Test
	public void usuarioInexistenteSeRechaza() {
		assertThrows(IllegalArgumentException.class, () -> {
			loginService.loadUserByUsername("no-existe");
	
		});
	}

	private static boolean tieneRol(UserDetails user, String rol) {
		for (GrantedAuthority a : user.getAuthorities())
			if (rol.equals(a.getAuthority()))
				return true;
		return false;
	}
}
