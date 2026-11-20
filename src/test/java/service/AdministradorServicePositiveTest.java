package service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import domain.Administrador;
import domain.Playa;
import domain.PeticionNegocio;
import domain.Carpeta;
import services.AdministradorService;
import utilities.AbstractTest;

@Transactional
@Rollback(false)
public class AdministradorServicePositiveTest extends AbstractTest{
	
	// System under test ---------------------------------------
	
	@Autowired
	private AdministradorService administradorService;
	
	// Other systems to run tests ------------------------------
	
	
	// Test cases ----------------------------------------------
	
		/*
		 * Register to the system as a administrador. Positive case.
		 */
		@Test
		public void registerAdministradorPositiveCase(){
			unauthenticate();
			Administrador admin=administradorService.create();
			
			admin.getUserAccount().setUsername("adminTest");
			admin.getUserAccount().setPassword("adminTest");
			
			Collection<Carpeta> carpetas = new ArrayList<Carpeta>();
			Collection<Playa> playas = new ArrayList<Playa>();
			Collection<PeticionNegocio> peticiones = new ArrayList<PeticionNegocio>();
			
			
			admin.setNombre("AdministradorTest");
			admin.setApellidos("apellidosTest");
			admin.setEmail("test@test.test");
			admin.setCarpetas(carpetas);
			admin.setPeticionNegocios(peticiones);
			admin.setPlayas(playas);
			
			administradorService.save(admin);
		}

}
