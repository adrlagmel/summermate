/* ActuatorTest.java
 *
 * Fase 7: salud y métricas en el puerto de gestión, y nada de Actuator en el
 * puerto de la aplicación. Arranca un servidor de verdad en puertos libres.
 */
package web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest(classes = summermate.SummerMateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	properties = "management.server.port=0")
@ActiveProfiles("dev")
class ActuatorTest {

	@ServiceConnection
	static final PostgreSQLContainer	POSTGRES	= utilities.AbstractTest.contenedor();

	@LocalServerPort
	int									puerto;

	@LocalManagementPort
	int									puertoGestion;

	private final HttpClient			http		= HttpClient.newHttpClient();


	private HttpResponse<String> get(int port, String ruta) throws Exception {
		return http.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + ruta)).build(), HttpResponse.BodyHandlers.ofString());
	}

	@Test
	void saludYMetricasEnElPuertoDeGestion() throws Exception {
		HttpResponse<String> salud = get(puertoGestion, "/actuator/health");
		assertEquals(200, salud.statusCode());
		assertTrue(salud.body().contains("\"UP\""));

		assertEquals(200, get(puertoGestion, "/actuator/health/readiness").statusCode());
		assertEquals(200, get(puertoGestion, "/actuator/info").statusCode());

		HttpResponse<String> metricas = get(puertoGestion, "/actuator/prometheus");
		assertEquals(200, metricas.statusCode());
		assertTrue(metricas.body().contains("http_server_requests") || metricas.body().contains("jvm_memory_used_bytes"));
	}

	@Test
	void elPuertoDeLaAplicacionNoSirveActuator() throws Exception {
		assertEquals(404, get(puerto, "/actuator/health").statusCode());
		assertEquals(404, get(puerto, "/actuator/prometheus").statusCode());
	}
}
