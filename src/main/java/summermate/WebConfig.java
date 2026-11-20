/* WebConfig.java
 *
 * Fase 4: idioma de la interfaz (antes en spring/servlet.xml). Se cambia con
 * ?language=es o ?language=en y se recuerda en una cookie.
 */

package summermate;

import java.time.Duration;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver("language");

		// La aplicación es para playas de Andalucía: español por defecto
		resolver.setDefaultLocale(Locale.forLanguageTag("es"));
		resolver.setCookieMaxAge(Duration.ofDays(365));

		return resolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();

		interceptor.setParamName("language");
		registry.addInterceptor(interceptor);
	}

}
