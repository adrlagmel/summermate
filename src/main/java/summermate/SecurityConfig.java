/* SecurityConfig.java
 *
 * Fase 4: sustituye a spring/config/security.xml. Las reglas siguen la nueva
 * estructura de URLs: lo público bajo /playas, /negocios, /eventos...; lo de
 * cada rol bajo /mis (usuario), /empresario y /admin. Cualquier ruta que no
 * aparezca aquí queda denegada, como hacía el antiguo "/**" denyAll.
 *
 * Las autoridades del proyecto no llevan el prefijo ROLE_, así que se usa
 * hasAuthority en lugar de hasRole.
 */

package summermate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import security.Authority;
import security.LoginService;

@Configuration
public class SecurityConfig {

	private static final String	ADMIN		= Authority.ADMININISTRADOR;
	private static final String	USUARIO		= Authority.USUARIO;
	private static final String	EMPRESARIO	= Authority.EMPRESARIO;


	// Con loginService como UserDetailsPasswordService, cada hash MD5 antiguo
	// se reescribe en BCrypt la primera vez que su usuario inicia sesión.
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(LoginService loginService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);

		provider.setUserDetailsService(loginService);
		provider.setUserDetailsPasswordService(loginService);

		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider daoAuthenticationProvider) throws Exception {
		http.authenticationProvider(daoAuthenticationProvider);

		http.authorizeHttpRequests(auth -> auth
			// Recursos estáticos y páginas de error
			.requestMatchers("/webjars/**", "/css/**", "/js/**", "/images/**", "/favicon.ico", "/error").permitAll()

			// Portada, acceso y registro
			.requestMatchers(HttpMethod.GET, "/", "/login", "/terminos").permitAll()
			.requestMatchers("/registro/**").anonymous()

			// Catálogo público
			.requestMatchers(HttpMethod.GET, "/playas", "/playas/cercanas", "/playas/{id}", "/playas/{id}/negocios").permitAll()
			.requestMatchers(HttpMethod.GET, "/negocios", "/negocios/{id}", "/negocios/{id}/valoraciones").permitAll()
			.requestMatchers(HttpMethod.GET, "/eventos", "/eventos/{id}").permitAll()
			.requestMatchers(HttpMethod.GET, "/usuarios").permitAll()
			.requestMatchers(HttpMethod.GET, "/imagenes/**").permitAll()

			// Usuario
			.requestMatchers("/mis/**").hasAuthority(USUARIO)
			.requestMatchers("/negocios/{id}/reservas/**", "/negocios/{id}/reservas").hasAuthority(USUARIO)
			.requestMatchers("/playas/{id}/valoraciones/**", "/playas/{id}/valoraciones").hasAuthority(USUARIO)
			.requestMatchers("/eventos/{id}/inscripcion").hasAuthority(USUARIO)

			// Empresario y administrador
			.requestMatchers("/denuncias/**", "/denuncias").hasAnyAuthority(USUARIO, EMPRESARIO)
			.requestMatchers("/empresario/**").hasAuthority(EMPRESARIO)
			.requestMatchers("/admin/**").hasAuthority(ADMIN)

			// Cualquier actor con sesión: perfil y mensajería
			.requestMatchers("/perfil/**", "/perfil", "/carpetas/**", "/carpetas", "/mensajes/**", "/mensajes").authenticated()

			.anyRequest().denyAll());

		http.formLogin(form -> form
			.loginPage("/login")
			.failureUrl("/login?error")
			.defaultSuccessUrl("/", false)
			.permitAll());

		// Logout por POST con el token CSRF (lo exige Spring Security por defecto)
		http.logout(logout -> logout
			.logoutUrl("/logout")
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true));

		return http.build();
	}

}
