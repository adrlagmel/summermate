package service;

import static org.junit.jupiter.api.Assertions.assertThrows;



import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import repositories.AdministradorRepository;
import services.AdministradorService;
import domain.Administrador;
import domain.Carpeta;
import domain.PeticionNegocio;
import domain.Playa;
import utilities.AbstractTest;

@Transactional
public class AdministradorServiceNegativeTest extends AbstractTest{
	
	// System under test ---------------------------------------
	
		@Autowired
		private AdministradorService administradorService;
		
		@Autowired
		private AdministradorRepository administradorRepository;
		
		// Other systems to run tests ------------------------------
		
		
		// Test cases ----------------------------------------------
		
			/*
			 * Register to the system as a administrador. Negative case. Not all fields.
			 */
			@Test
			public void registerAthleteNegativeCase() {
				assertThrows(ConstraintViolationException.class, () -> {
					unauthenticate();
					Administrador admin = administradorService.create();
				
					admin.getUserAccount().setUsername("athleteTest");
					admin.getUserAccount().setPassword("athleteTest");
				
					Collection<Carpeta> carpetas = new ArrayList<Carpeta>();
					Collection<Playa> playas = new ArrayList<Playa>();
					Collection<PeticionNegocio> peticiones = new ArrayList<PeticionNegocio>();
				
				
					// Sin nombre (campo @NotBlank): debe fallar la validación.
					admin.setNombre(null);
					admin.setApellidos("apellidosTest");
					admin.setEmail("test@test.test");
					admin.setCarpetas(carpetas);
					admin.setPeticionNegocios(peticiones);
					admin.setPlayas(playas);
					administradorService.save(admin);
					administradorRepository.flush();
			
				});
			}

}
