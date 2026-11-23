/* ApiDocumentacion.java
 *
 * Descripción OpenAPI de la API del móvil (springdoc). La documentación
 * navegable está en /api/docs y el JSON en /api/docs/openapi.json, del que la
 * app puede generar su cliente con tipos.
 */

package api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class ApiDocumentacion {

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
			.info(new Info().title("Summermate API").version("v1")
				.description("""
					API de Summermate para la app móvil.

					1. `POST /api/v1/auth/login` con usuario y contraseña devuelve un token de acceso (15 min) y uno de renovación (30 días).
					2. Las peticiones llevan `Authorization: Bearer <accessToken>`.
					3. Cuando el acceso caduca (401), `POST /api/v1/auth/refresh` da un par nuevo. El de renovación es de un solo uso.

					Los errores siguen RFC 9457 (`application/problem+json`). Las listas van paginadas con `?page=` (desde 1).
					"""))
			.components(new Components().addSecuritySchemes("bearer",
				new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
			.addSecurityItem(new SecurityRequirement().addList("bearer"));
	}

}
