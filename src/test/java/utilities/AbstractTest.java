/* AbstractTest.java
 *
 * Base de los tests de integración (Spring Boot + JUnit 5).
 *
 * Base de datos: un PostgreSQL desechable de Testcontainers que Spring Boot
 * conecta solo (@ServiceConnection). Flyway crea el esquema y, con el perfil
 * dev, carga los datos de ejemplo. El contexto (y el contenedor) se comparte
 * entre todas las clases que heredan de esta.
 *
 * Antes de cada test se vacían las tablas y se vuelve a cargar la semilla,
 * como hacía PopulateDatabase. Se usa TRUNCATE en lugar de borrar el esquema
 * porque así el pool de conexiones no se queda con sentencias preparadas que
 * apuntan a tablas que ya no existen.
 */

package utilities;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import security.LoginService;

@SpringBootTest(classes = summermate.SummerMateApplication.class)
@ActiveProfiles("dev")
public abstract class AbstractTest {

	@ServiceConnection
	protected static final PostgreSQLContainer<?>	POSTGRES	= new PostgreSQLContainer<>("postgres:16-alpine");

	static {
		POSTGRES.start();
	}

	@Autowired
	private LoginService							loginService;

	@Autowired
	private DataSource								dataSource;


	@BeforeEach
	public void recargarDatosDeEjemplo() throws Exception {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		String tablas = String.join(", ", jdbc.queryForList(
			"select tablename from pg_tables where schemaname = 'public' and tablename <> 'flyway_schema_history'", String.class));

		jdbc.execute("truncate table " + tablas + " cascade");
		// Misma conexión que el JdbcTemplate: en los tests @Transactional es la
		// de la transacción del test, y todo se deshace al terminar.
		Connection connection = DataSourceUtils.getConnection(dataSource);
		try {
			ScriptUtils.executeSqlScript(connection, new ClassPathResource("db/dev/V1000__datos_ejemplo.sql"));
		} finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
		}
		unauthenticate();
	}

	public void authenticate(String username) {
		TestingAuthenticationToken authenticationToken = null;

		if (username != null) {
			UserDetails userDetails = loginService.loadUserByUsername(username);
			authenticationToken = new TestingAuthenticationToken(userDetails, null, new java.util.ArrayList<>(userDetails.getAuthorities()));
		}
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	public void unauthenticate() {
		SecurityContextHolder.clearContext();
	}

}
