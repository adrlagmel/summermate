/* ApiSecurityConfig.java
 *
 * Seguridad de la API del móvil (/api/**), separada de la web (Fase 8):
 * sin sesión ni cookies, sin CSRF (no hay cookies que robar) y con
 * "Authorization: Bearer <JWT>". El token se traduce a la misma UserAccount
 * que usa la web, así que los servicios (LoginService.getPrincipal, las
 * comprobaciones de propietario...) funcionan igual en los dos sitios.
 * Los errores de autenticación salen como problem+json (RFC 9457).
 */

package api;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.SecurityFilterChain;

import security.Authority;
import security.UserAccount;

@Configuration
public class ApiSecurityConfig {

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http, ApiTokens tokens) throws Exception {
		http.securityMatcher("/api/**")
			.csrf(csrf -> csrf.disable())
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.requestCache(c -> c.disable())
			.formLogin(f -> f.disable())
			.httpBasic(b -> b.disable())
			.logout(l -> l.disable());

		http.authorizeHttpRequests(auth -> auth
			.requestMatchers("/api/v1/auth/**").permitAll()
			.requestMatchers("/api/docs/**", "/api/docs").permitAll()
			// Consulta pública, como en la web
			.requestMatchers(HttpMethod.GET, "/api/v1/playas", "/api/v1/playas/**", "/api/v1/negocios", "/api/v1/negocios/**",
				"/api/v1/eventos", "/api/v1/eventos/**").permitAll()
			.requestMatchers("/api/v1/yo/reservas/**", "/api/v1/yo/reservas", "/api/v1/yo/eventos", "/api/v1/eventos/*/inscripcion",
				"/api/v1/playas/*/valoraciones").hasAuthority(Authority.USUARIO)
			.requestMatchers("/api/v1/valoraciones/negocios/*/util").hasAnyAuthority(Authority.USUARIO, Authority.EMPRESARIO)
			.requestMatchers("/api/v1/yo", "/api/v1/yo/**").authenticated()
			.anyRequest().denyAll());

		http.oauth2ResourceServer(o -> o
			.jwt(jwt -> jwt.decoder(tokens.decoder()).jwtAuthenticationConverter(token -> {
				UserAccount cuenta = tokens.cuenta(Integer.parseInt(token.getSubject()));
				// Cuenta borrada (anonimizada) o inexistente: el token ya no vale
				if (cuenta == null || cuenta.getUsername().startsWith("borrado-"))
					throw new InvalidBearerTokenException("La cuenta ya no existe");
				return UsernamePasswordAuthenticationToken.authenticated(cuenta, token, cuenta.getAuthorities());
			}))
			.authenticationEntryPoint((request, response, e) -> problema(response, HttpStatus.UNAUTHORIZED, "Hace falta iniciar sesión", e.getMessage()))
			.accessDeniedHandler((request, response, e) -> problema(response, HttpStatus.FORBIDDEN, "No tienes permiso", null)));
		http.exceptionHandling(e -> e
			.authenticationEntryPoint((request, response, ex) -> problema(response, HttpStatus.UNAUTHORIZED, "Hace falta iniciar sesión", null))
			.accessDeniedHandler((request, response, ex) -> problema(response, HttpStatus.FORBIDDEN, "No tienes permiso", null)));

		return http.build();
	}

	private static void problema(jakarta.servlet.http.HttpServletResponse response, HttpStatus estado, String titulo, String detalle) throws java.io.IOException {
		response.setStatus(estado.value());
		response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		if (estado == HttpStatus.UNAUTHORIZED)
			response.setHeader("WWW-Authenticate", "Bearer");
		String json = "{\"type\":\"about:blank\",\"title\":" + texto(titulo) + ",\"status\":" + estado.value()
			+ (detalle == null ? "" : ",\"detail\":" + texto(detalle)) + "}";
		response.getWriter().write(json);
	}

	private static String texto(String s) {
		return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ") + "\"";
	}


}
