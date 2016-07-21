package service;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import domain.Administrador;
import domain.Playa;
import domain.PeticionNegocio;
import domain.Carpeta;
import services.AdministradorService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback=false)
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
