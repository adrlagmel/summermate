/* ApiAuthController.java
 *
 *   POST /api/v1/auth/login     {username, password}  -> tokens
 *   POST /api/v1/auth/refresh   {refreshToken}        -> tokens nuevos (el anterior deja de valer)
 *   POST /api/v1/auth/logout    {refreshToken}        -> 204, cierra la sesión de ese dispositivo
 */

package api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dto.Dtos.Login;
import api.dto.Dtos.Renovacion;

@RestController
@RequestMapping("/api/v1/auth")
public class ApiAuthController {

	private final ApiTokens			tokens;
	private final LimiteIntentos	limite;


	public ApiAuthController(ApiTokens tokens, LimiteIntentos limite) {
		this.tokens = tokens;
		this.limite = limite;
	}

	@PostMapping("/login")
	public ApiTokens.Par login(@Valid @RequestBody Login login, HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if (limite.bloqueado(login.username(), ip))
			throw new DemasiadosIntentos();
		try {
			ApiTokens.Par par = tokens.login(login.username(), login.password());
			limite.exito(login.username());
			return par;
		} catch (ApiTokens.NoAutorizado mal) {
			limite.fallo(login.username(), ip);
			throw mal;
		}
	}

	@PostMapping("/refresh")
	public ApiTokens.Par renovar(@Valid @RequestBody Renovacion renovacion) {
		return tokens.renovar(renovacion.refreshToken());
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@Valid @RequestBody Renovacion renovacion) {
		tokens.logout(renovacion.refreshToken());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	public static class DemasiadosIntentos extends RuntimeException {

		private static final long serialVersionUID = 1L;
	}

}
