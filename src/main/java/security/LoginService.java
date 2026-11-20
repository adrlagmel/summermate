/* LoginService.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utilities.Assert;

@Service
@Transactional
public class LoginService implements UserDetailsService, UserDetailsPasswordService {
	
	// Managed repository -----------------------------------------------------

	@Autowired
	UserAccountRepository userRepository;
	
	// Business methods -------------------------------------------------------

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Assert.notNull(username);

		UserDetails result;

		result = userRepository.findByUsername(username);
		Assert.notNull(result);		
		// WARNING: The following sentences prevent lazy initialisation problems!
		Assert.notNull(result.getAuthorities());
		result.getAuthorities().size();

		return result;
	}

	// Spring Security llama a este método tras un login correcto cuando el
	// hash guardado no usa el algoritmo actual (p. ej. un MD5 antiguo), para
	// guardarlo de nuevo con BCrypt.
	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		UserAccount account;

		account = userRepository.findByUsername(user.getUsername());
		Assert.notNull(account);
		account.setPassword(newPassword);

		return userRepository.save(account);
	}

	// Hay una sesión con un usuario de la aplicación (no anónima)
	public static boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.getPrincipal() instanceof UserAccount;
	}

	public static UserAccount getPrincipal() {
		UserAccount result;
		SecurityContext context;
		Authentication authentication;
		Object principal;

		// If the asserts in this method fail, then you're
		// likely to have your Tomcat's working directory
		// corrupt. Please, clear your browser's cache, stop
		// Tomcat, update your Maven's project configuration,
		// clean your project, clean Tomcat's working directory,
		// republish your project, and start it over.

		context = SecurityContextHolder.getContext();
		Assert.notNull(context);
		authentication = context.getAuthentication();
		Assert.notNull(authentication);
		principal = authentication.getPrincipal();
		Assert.isTrue(principal instanceof UserAccount);
		result = (UserAccount) principal;
		Assert.notNull(result);
		Assert.isTrue(result.getId() != 0);

		return result;
	}

}
