/* AbstractTest.java
 *
 * Copyright (C) 2015 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package utilities;

import org.junit.After;
import org.junit.Before;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import security.LoginService;

public abstract class AbstractTest {

	// Base de datos de pruebas ---------------------------
	//
	// Se arranca un PostgreSQL desechable en Docker una sola vez por JVM y se
	// publica su conexión como propiedades del sistema (DB_URL, DB_USER y
	// DB_PASSWORD), que es lo que leen spring/config/data.xml y DatabaseUtil.
	// El bloque static se ejecuta antes de que Spring cree el contexto.

	private static final PostgreSQLContainer<?> POSTGRES;

	static {
		POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
			.withDatabaseName("summermate")
			.withUsername("summermate")
			.withPassword("summermate");
		POSTGRES.start();

		System.setProperty("DB_URL", POSTGRES.getJdbcUrl());
		System.setProperty("DB_USER", POSTGRES.getUsername());
		System.setProperty("DB_PASSWORD", POSTGRES.getPassword());
	}

	// Supporting services --------------------------------

	@Autowired
	private LoginService loginService;
	
	// Set up and tear down -------------------------------
	
	@Before
	public void setUp() {
		PopulateDatabase.main(null);
	}
	
	@After
	public void tearDown() {
	}

	// Supporting methods ---------------------------------

	public void authenticate(String username) {
		UserDetails userDetails;
		TestingAuthenticationToken authenticationToken;
		SecurityContext context;
	
		if (username == null)
			authenticationToken = null;
		else {
			userDetails = loginService.loadUserByUsername(username);
			authenticationToken = new TestingAuthenticationToken(userDetails, null);		    
		}
		
		context = SecurityContextHolder.getContext();
		context.setAuthentication(authenticationToken);
	}
	
	public void unauthenticate() {
		authenticate(null);
	}
	
}
