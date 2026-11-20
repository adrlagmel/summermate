package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;
import security.LoginService;
import utilities.AbstractTest;

/*
 * Tests de caracterización: fijan el comportamiento actual del login para
 * que no cambie sin querer durante la migración.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
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
	@Test(expected = IllegalArgumentException.class)
	public void usuarioInexistenteSeRechaza() {
		loginService.loadUserByUsername("no-existe");
	}

	private static boolean tieneRol(UserDetails user, String rol) {
		for (GrantedAuthority a : user.getAuthorities())
			if (rol.equals(a.getAuthority()))
				return true;
		return false;
	}
}
