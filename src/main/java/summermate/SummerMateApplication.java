/* SummerMateApplication.java
 *
 * Fase 4: punto de entrada de la aplicación Spring Boot (JAR ejecutable con
 * Tomcat embebido). Sustituye a web.xml y a los ficheros XML de spring/.
 *
 * Los paquetes del proyecto original (domain, services, repositories...) no
 * cuelgan de un paquete raíz común, así que se indican explícitamente.
 */

package summermate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "summermate", "controllers", "services", "repositories", "security", "forms", "utilities" })
@EntityScan({ "domain", "security" })
@EnableJpaRepositories({ "repositories", "security" })
public class SummerMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummerMateApplication.class, args);
	}

}
