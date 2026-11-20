package services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ColaboracionTest {

	@Test
	void puntosYNiveles() {
		assertEquals(0, new Colaboracion(0, 0, 0, 0).puntos());
		assertEquals(Colaboracion.Nivel.TURISTA, new Colaboracion(0, 0, 0, 0).nivel());
		assertEquals(50, new Colaboracion(0, 0, 0, 0).faltan());

		Colaboracion veraneante = new Colaboracion(3, 2, 0, 0);
		assertEquals(50, veraneante.puntos());
		assertEquals(Colaboracion.Nivel.VERANEANTE, veraneante.nivel());
		assertEquals(100, veraneante.faltan());

		Colaboracion embajador = new Colaboracion(20, 20, 10, 4);
		assertEquals(440, embajador.puntos());
		assertEquals(Colaboracion.Nivel.EMBAJADOR, embajador.nivel());
		assertEquals(0, embajador.faltan());
	}
}
