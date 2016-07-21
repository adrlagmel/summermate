package service;



import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import repositories.AdministradorRepository;
import services.AdministradorService;
import domain.Administrador;
import domain.Carpeta;
import domain.PeticionNegocio;
import domain.Playa;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback=true)
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
			@Test(expected=ConstraintViolationException.class)
			public void registerAthleteNegativeCase(){
				unauthenticate();
				Administrador admin = administradorService.create();
				
				admin.getUserAccount().setUsername("athleteTest");
				admin.getUserAccount().setPassword("athleteTest");
				
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
				administradorRepository.flush();
			}

}
