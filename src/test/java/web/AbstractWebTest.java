/* AbstractWebTest.java
 *
 * Base de los tests de la capa web: MockMvc con la cadena de filtros de
 * Spring Security completa (CSRF incluido) sobre la aplicación real y la base
 * de datos de Testcontainers con los datos de ejemplo.
 */

package web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import security.LoginService;
import utilities.AbstractTest;

@AutoConfigureMockMvc
public abstract class AbstractWebTest extends AbstractTest {

	@Autowired
	protected MockMvc		mvc;

	@Autowired
	private LoginService	loginService;

	@Autowired
	private DataSource		dataSource;


	// Petición hecha por el usuario indicado (con sus autoridades reales)
	protected RequestPostProcessor como(String username) {
		return user(loginService.loadUserByUsername(username));
	}

	protected JdbcTemplate jdbc() {
		return new JdbcTemplate(dataSource);
	}

	protected Integer contar(String sql, Object... args) {
		return jdbc().queryForObject(sql, Integer.class, args);
	}

}
