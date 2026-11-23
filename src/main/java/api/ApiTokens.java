/* ApiTokens.java
 *
 * Inicio de sesión de la API del móvil (Fase 8).
 *   - Token de acceso: JWT firmado con HMAC-SHA256, 15 minutos. Lleva el id
 *     de la cuenta (sub), el nombre de usuario y los roles.
 *   - Token de renovación: 32 bytes aleatorios, 30 días, de un solo uso.
 * La clave de firma sale de JWT_SECRET (al menos 32 caracteres). Si falta, se
 * genera una al arrancar y los tokens dejan de valer al reiniciar: vale para
 * desarrollo, no para producción.
 */

package api;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import domain.TokenRefresco;
import repositories.TokenRefrescoRepository;
import security.UserAccount;
import security.UserAccountRepository;

@Service
// Un token rechazado no deshace lo que se haya hecho antes de rechazarlo (por
// ejemplo, anular la familia de un token reutilizado).
@Transactional(noRollbackFor = ApiTokens.NoAutorizado.class)
public class ApiTokens {

	public static final Duration	DURACION_ACCESO		= Duration.ofMinutes(15);
	public static final Duration	DURACION_RENOVACION	= Duration.ofDays(30);
	public static final String		EMISOR				= "summermate";

	private static final Logger		log					= LoggerFactory.getLogger(ApiTokens.class);
	private static final SecureRandom	aleatorio		= new SecureRandom();

	public record Par(String accessToken, String refreshToken, long expiraEn, String tipo) {

		public Par(String accessToken, String refreshToken) {
			this(accessToken, refreshToken, DURACION_ACCESO.toSeconds(), "Bearer");
		}
	}

	// Credenciales o token de renovación no válidos
	public static class NoAutorizado extends RuntimeException {

		private static final long serialVersionUID = 1L;


		public NoAutorizado(String mensaje) {
			super(mensaje);
		}
	}


	private final SecretKey					clave;
	private final JwtEncoder				encoder;
	private final JwtDecoder				decoder;
	private final TokenRefrescoRepository	tokens;
	private final UserAccountRepository		cuentas;
	private final AuthenticationProvider	autenticacion;


	public ApiTokens(@Value("${summermate.api.jwt-secret:}") String secreto, TokenRefrescoRepository tokens, UserAccountRepository cuentas,
		AuthenticationProvider autenticacion) {
		byte[] bytes;
		if (secreto == null || secreto.isBlank()) {
			log.warn("JWT_SECRET no está definido: se usa una clave aleatoria y los tokens de la API caducan al reiniciar");
			bytes = new byte[32];
			aleatorio.nextBytes(bytes);
		} else {
			bytes = secreto.getBytes(StandardCharsets.UTF_8);
			if (bytes.length < 32)
				throw new IllegalStateException("JWT_SECRET debe tener al menos 32 caracteres");
		}
		this.clave = new SecretKeySpec(bytes, "HmacSHA256");
		this.encoder = new NimbusJwtEncoder(new ImmutableSecret<>(clave));
		this.decoder = NimbusJwtDecoder.withSecretKey(clave).macAlgorithm(MacAlgorithm.HS256).build();
		this.tokens = tokens;
		this.cuentas = cuentas;
		this.autenticacion = autenticacion;
	}

	public JwtDecoder decoder() {
		return decoder;
	}

	public Par login(String username, String password) {
		UserAccount cuenta;
		try {
			cuenta = (UserAccount) autenticacion.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password)).getPrincipal();
		} catch (AuthenticationException mal) {
			throw new NoAutorizado("Usuario o contraseña incorrectos");
		}
		return emitir(cuenta, UUID.randomUUID().toString());
	}

	public Par renovar(String refreshToken) {
		TokenRefresco token = refreshToken == null ? null : tokens.findByHash(hash(refreshToken));
		if (token == null)
			throw new NoAutorizado("Token de renovación no válido");
		if (token.isRevocado()) {
			// Reutilización de un token ya gastado: posible robo. Fuera la sesión entera.
			tokens.revocarFamilia(token.getFamilia());
			throw new NoAutorizado("Token de renovación ya usado");
		}
		if (token.getExpira().isBefore(Instant.now()))
			throw new NoAutorizado("Token de renovación caducado");

		token.setRevocado(true);
		return emitir(token.getUserAccount(), token.getFamilia());
	}

	public void logout(String refreshToken) {
		TokenRefresco token = refreshToken == null ? null : tokens.findByHash(hash(refreshToken));
		if (token != null)
			tokens.revocarFamilia(token.getFamilia());
	}

	// Al borrar la cuenta o cambiar la contraseña, todas las sesiones del móvil se cierran
	public void revocarTodo(int userAccountId) {
		tokens.revocarDeCuenta(userAccountId);
	}

	// Se llama desde el filtro de seguridad, fuera de cualquier sesión de
	// Hibernate: los roles se cargan aquí.
	@Transactional(readOnly = true)
	public UserAccount cuenta(int id) {
		UserAccount cuenta = cuentas.findById(id).orElse(null);
		if (cuenta != null)
			org.hibernate.Hibernate.initialize(cuenta.getAuthorities());
		return cuenta;
	}

	private Par emitir(UserAccount cuenta, String familia) {
		Instant ahora = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
			.issuer(EMISOR)
			.subject(String.valueOf(cuenta.getId()))
			.issuedAt(ahora)
			.expiresAt(ahora.plus(DURACION_ACCESO))
			.claim("username", cuenta.getUsername())
			.claim("roles", cuenta.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
			.build();
		String acceso = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();

		byte[] bytes = new byte[32];
		aleatorio.nextBytes(bytes);
		String renovacion = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

		TokenRefresco token = new TokenRefresco();
		token.setHash(hash(renovacion));
		token.setFamilia(familia);
		token.setCreado(ahora);
		token.setExpira(ahora.plus(DURACION_RENOVACION));
		token.setUserAccount(cuenta);
		tokens.save(token);

		return new Par(acceso, renovacion);
	}

	static String hash(String token) {
		try {
			return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException imposible) {
			throw new IllegalStateException(imposible);
		}
	}

}
