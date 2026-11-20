package service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import services.PlayaService;
import utilities.AbstractTest;
import domain.Playa;

/*
 * "Playas cerca de mí" se calcula en la base de datos. Distancias reales
 * desde La Caleta (Cádiz) a las playas de PopulateDatabase.xml: Playa de
 * Regla 25,7 km, Calas de Poniente 29,5 km, Matalascañas 55,5 km, La
 * Malagueta 170,4 km y De Nueva Almería 345,3 km.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
public class PlayaCercanaTest extends AbstractTest {

	private static final double	LAT	= 36.530933;
	private static final double	LON	= -6.304980;

	@Autowired
	private PlayaService		playaService;

	@Test
	public void devuelveLasPlayasDentroDelRadioOrdenadasPorDistancia() {
		assertEquals(Arrays.asList("La Caleta", "Playa de Regla", "Calas de Poniente"), nombres(playaService.nearToMe(LAT, LON, 30)));
	}

	@Test
	public void elRadioPorDefectoEsDe25Km() {
		assertEquals(Arrays.asList("La Caleta"), nombres(playaService.nearToMe(LAT, LON)));
	}

	@Test
	public void unRadioMayorIncluyeMasPlayas() {
		assertEquals(5, playaService.nearToMe(LAT, LON, 200).size());
	}

	// Antes el umbral era 10.000.000 (en km) y devolvía todas las playas.
	@Test
	public void lasPlayasLejanasNoAparecen() {
		assertEquals(Arrays.asList("La Malagueta"), nombres(playaService.nearToMe(36.717268, -4.410057, 25)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void rechazaRadiosFueraDeRango() {
		playaService.nearToMe(LAT, LON, 5000);
	}

	private static List<String> nombres(java.util.Collection<Playa> playas) {
		List<String> result = new ArrayList<String>();
		for (Playa p : playas)
			result.add(p.getNombre());
		return result;
	}
}
