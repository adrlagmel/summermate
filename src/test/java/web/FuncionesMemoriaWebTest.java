/* FuncionesMemoriaWebTest.java
 *
 * Fase 6: funciones descritas en la memoria del TFG que no estaban hechas.
 */
package web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

class FuncionesMemoriaWebTest extends AbstractWebTest {

	private static String dentroDeDias(int dias, int hora) {
		return LocalDate.now().plusDays(dias).atTime(hora, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	private Integer id(String sql) {
		return jdbc().queryForObject(sql, Integer.class);
	}

	// Eliminar evento (UC0004) --------------------------------------------------

	@Test
	void borrarEventoSinInscritos() throws Exception {
		mvc.perform(post("/empresario/eventos").with(como("empresario1")).with(csrf())
			.param("nombre", "Efímero").param("descripcion", "x").param("fechaCelebracion", dentroDeDias(10, 22))
			.param("precio", "0").param("negocio", "1"));
		Integer evento = id("select id from evento where nombre = 'Efímero'");

		mvc.perform(post("/empresario/eventos/" + evento).with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/eventos")).andExpect(flash().attribute("aviso", "fase5.borrado"));
		assertEquals(0, contar("select count(*) from evento where id = ?", evento));
	}

	@Test
	void noSeBorraUnEventoConInscritos() throws Exception {
		mvc.perform(post("/empresario/eventos/1").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/eventos")).andExpect(flash().attribute("message", "evento.borrar.noSePuede"));
		assertEquals(1, contar("select count(*) from evento where id = 1"));
	}

	@Test
	void noSeBorraUnEventoAjeno() throws Exception {
		mvc.perform(post("/empresario/eventos/2").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(status().isForbidden());
	}

	// Eliminar negocio (UC0009) -------------------------------------------------

	@Test
	void borrarNegocioSinEventosNiReservas() throws Exception {
		mvc.perform(post("/empresario/negocios").with(como("empresario1")).with(csrf())
			.param("nombre", "Temporal").param("tipo", "Bar").param("descripcion", "x")
			.param("telefono", "+34956000000").param("aforo", "40").param("playa", "3"));
		Integer negocio = id("select id from negocio where nombre = 'Temporal'");

		mvc.perform(post("/empresario/negocios/" + negocio).with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/negocios")).andExpect(flash().attribute("aviso", "fase5.borrado"));
		assertEquals(0, contar("select count(*) from negocio where id = ?", negocio));
	}

	@Test
	void noSeBorraUnNegocioConEventos() throws Exception {
		mvc.perform(post("/empresario/negocios/1").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/negocios")).andExpect(flash().attribute("message", "negocio.borrar.noSePuede"));
		assertEquals(1, contar("select count(*) from negocio where id = 1"));
	}

	// Cancelar petición -------------------------------------------------------

	@Test
	void cancelarUnaPeticionPendiente() throws Exception {
		// empresario3 tiene la petición 3, pendiente
		mvc.perform(post("/empresario/peticion").with(como("empresario3")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/peticion")).andExpect(flash().attribute("aviso", "peticion.cancelada"));
		assertEquals(0, contar("select count(*) from peticionnegocio where id = 3"));
	}

	@Test
	void noSeCancelaUnaPeticionAceptada() throws Exception {
		mvc.perform(post("/empresario/peticion").with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(status().isForbidden());
		assertEquals(1, contar("select count(*) from peticionnegocio where id = 1"));
	}

	// Moderación del administrador ------------------------------------------------

	@Test
	void elAdministradorVeLasDenuncias() throws Exception {
		mvc.perform(get("/admin/denuncias").with(como("admin"))).andExpect(status().isOk());
		mvc.perform(get("/admin/negocios").with(como("admin"))).andExpect(status().isOk());
		mvc.perform(get("/admin/denuncias").with(como("empresario1"))).andExpect(status().isForbidden());
	}

	@Test
	void descartarUnaDenunciaDejaLaValoracion() throws Exception {
		mvc.perform(post("/admin/denuncias/1").with(como("admin")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/admin/denuncias"));
		assertEquals(0, contar("select count(*) from denunciavaloracion where id = 1"));
		assertEquals(1, contar("select count(*) from valoracionnegocio where id = 2"));
	}

	@Test
	void retirarLaValoracionDenunciadaRecalculaLaMedia() throws Exception {
		// Comida Fast: valoraciones 1 (7) y 2 (3, denunciada)
		mvc.perform(post("/admin/valoraciones/negocios/2").with(como("admin")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/admin/denuncias"));
		assertEquals(0, contar("select count(*) from valoracionnegocio where id = 2"));
		assertEquals(0, contar("select count(*) from denunciavaloracion where valoracionnegocio_id = 2"));
		assertEquals(7, contar("select round(valoracionmedia)::int from negocio where id = 1"));
	}

	@Test
	void elAdministradorSuspendeYReactivaCualquierNegocio() throws Exception {
		mvc.perform(post("/admin/negocios/2/suspension").with(como("admin")).with(csrf()))
			.andExpect(redirectedUrl("/admin/negocios"));
		assertEquals(0, contar("select count(*) from negocio where id = 2 and negocioactivo"));
		mvc.perform(post("/admin/negocios/2/suspension").with(como("admin")).with(csrf()).param("_method", "delete"));
		assertEquals(1, contar("select count(*) from negocio where id = 2 and negocioactivo"));
	}

	// Votos de "comentario útil" ---------------------------------------------------

	@Test
	void votarYQuitarElVotoUtil() throws Exception {
		// La valoración 1 es de usuario1; vota usuario2
		mvc.perform(post("/valoraciones/negocios/1/util").with(como("usuario2")).with(csrf()))
			.andExpect(redirectedUrl("/negocios/1#valoraciones"));
		assertEquals(1, contar("select comentarioutil from valoracionnegocio where id = 1"));

		mvc.perform(get("/negocios/1").with(como("usuario2"))).andExpect(status().isOk());

		mvc.perform(post("/valoraciones/negocios/1/util").with(como("usuario2")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/negocios/1#valoraciones"));
		assertEquals(0, contar("select comentarioutil from valoracionnegocio where id = 1"));
	}

	@Test
	void nadieVotaDosVecesNiSuPropiaValoracion() throws Exception {
		mvc.perform(post("/valoraciones/negocios/1/util").with(como("empresario1")).with(csrf()));
		mvc.perform(post("/valoraciones/negocios/1/util").with(como("empresario1")).with(csrf()))
			.andExpect(status().isForbidden());
		mvc.perform(post("/valoraciones/negocios/1/util").with(como("usuario1")).with(csrf()))
			.andExpect(status().isForbidden());
		assertEquals(1, contar("select comentarioutil from valoracionnegocio where id = 1"));
	}

	@Test
	void laFichaDelNegocioSinSesion() throws Exception {
		mvc.perform(get("/negocios/1")).andExpect(status().isOk());
	}

	// Nivel de colaboración --------------------------------------------------------

	@Test
	void elPerfilMuestraElNivelDeColaboracion() throws Exception {
		// usuario1: una valoración de playa, una de negocio y una reserva pasada = 25 puntos
		mvc.perform(get("/perfil").with(como("usuario1")))
			.andExpect(status().isOk())
			.andExpect(model().attribute("colaboracion", new services.Colaboracion(1, 1, 0, 1)));
	}

	// Cuenta premium (pasarela simulada) -------------------------------------------

	@Test
	void contratarPremiumConLaPasarelaSimulada() throws Exception {
		String pasarela = mvc.perform(post("/empresario/premium").param("plan", "MENSUAL").with(como("empresario1")).with(csrf()))
			.andExpect(status().is3xxRedirection()).andReturn().getResponse().getRedirectedUrl();
		assertEquals(1, contar("select count(*) from pagopremium where estado = 'PENDIENTE' and importe = 9.99"));

		mvc.perform(get(pasarela).with(como("empresario1"))).andExpect(status().isOk());
		// Otro empresario no puede ver ni pagar el pago ajeno
		mvc.perform(post(pasarela).with(como("empresario2")).with(csrf())).andExpect(status().isForbidden());

		mvc.perform(post(pasarela).with(como("empresario1")).with(csrf()))
			.andExpect(redirectedUrl("/empresario/premium"));
		assertEquals(1, contar("select count(*) from empresario where id = 3 and premiumhasta = current_date + interval '1 month' - interval '1 day'"));

		// La ficha del negocio lo muestra como destacado
		mvc.perform(get("/negocios/1")).andExpect(status().isOk())
			.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("bi-star-fill")));

		// Un pago ya pagado no se paga dos veces
		mvc.perform(post(pasarela).with(como("empresario1")).with(csrf())).andExpect(status().isForbidden());
	}

	@Test
	void cancelarElPagoNoActivaPremium() throws Exception {
		String pasarela = mvc.perform(post("/empresario/premium").param("plan", "ANUAL").with(como("empresario1")).with(csrf()))
			.andReturn().getResponse().getRedirectedUrl();
		mvc.perform(post(pasarela).with(como("empresario1")).with(csrf()).param("_method", "delete"))
			.andExpect(redirectedUrl("/empresario/premium"));
		assertEquals(1, contar("select count(*) from pagopremium where estado = 'CANCELADO'"));
		assertEquals(0, contar("select count(*) from empresario where id = 3 and premiumhasta is not null"));
	}

	// Calendario: horario, mesas y días cerrados -------------------------------------

	private org.springframework.test.web.servlet.ResultActions reservar(String usuario, String fecha, int comensales, String comentario) throws Exception {
		return mvc.perform(post("/negocios/1/reservas").with(como(usuario)).with(csrf())
			.param("fecha", fecha).param("comensales", String.valueOf(comensales)).param("fumadores", "false").param("comentarios", comentario));
	}

	@Test
	void elCalendarioSeVe() throws Exception {
		mvc.perform(get("/empresario/negocios/1/calendario").with(como("empresario1"))).andExpect(status().isOk());
		mvc.perform(get("/empresario/negocios/1/calendario").param("dia", LocalDate.now().plusDays(3).toString()).with(como("empresario1")))
			.andExpect(status().isOk());
		mvc.perform(get("/empresario/negocios/2/calendario").with(como("empresario1"))).andExpect(status().isForbidden());
	}

	@Test
	void conMesasCadaReservaOcupaUna() throws Exception {
		mvc.perform(post("/empresario/negocios/1/mesas").with(como("empresario1")).with(csrf()).param("nombre", "Terraza 1").param("capacidad", "4"));
		mvc.perform(post("/empresario/negocios/1/mesas").with(como("empresario1")).with(csrf()).param("nombre", "Salón").param("capacidad", "8"));
		String hora = dentroDeDias(4, 14);

		// 2 personas: la mesa pequeña; 3 personas: la grande; la tercera ya no cabe
		reservar("usuario1", hora, 2, "Mesa A").andExpect(redirectedUrl("/mis/reservas"));
		reservar("usuario2", hora, 3, "Mesa B").andExpect(redirectedUrl("/mis/reservas"));
		reservar("usuario1", hora, 2, "Mesa C").andExpect(status().isOk())
			.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.model().attribute("message", "reserva.error.sinMesa"));

		assertEquals(1, contar("select count(*) from reserva r join mesa m on m.id = r.mesa_id where r.comentarios = 'Mesa A' and m.nombre = 'Terraza 1'"));
		assertEquals(1, contar("select count(*) from reserva r join mesa m on m.id = r.mesa_id where r.comentarios = 'Mesa B' and m.nombre = 'Salón'"));

		// Una hora y media después (la reserva dura 60 min) vuelve a haber mesa
		reservar("usuario1", dentroDeDias(4, 14).replace("T14:00", "T15:30"), 2, "Mesa D").andExpect(redirectedUrl("/mis/reservas"));
	}

	@Test
	void unDiaCerradoNoAdmiteReservas() throws Exception {
		LocalDate dia = LocalDate.now().plusDays(6);
		mvc.perform(post("/empresario/negocios/1/cierres").with(como("empresario1")).with(csrf()).param("fecha", dia.toString()).param("motivo", "Inventario"))
			.andExpect(redirectedUrl("/empresario/negocios/1/calendario?dia=" + dia));

		reservar("usuario1", dentroDeDias(6, 14), 2, "Cerrado").andExpect(status().isOk())
			.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.model().attribute("message", "reserva.error.cerrado"));
	}

	@Test
	void noSeCierraUnDiaConReservas() throws Exception {
		reservar("usuario1", dentroDeDias(7, 14), 2, "Antes");
		mvc.perform(post("/empresario/negocios/1/cierres").with(como("empresario1")).with(csrf()).param("fecha", LocalDate.now().plusDays(7).toString()))
			.andExpect(flash().attribute("message", "calendario.cierre.conReservas"));
		assertEquals(0, contar("select count(*) from cierre"));
	}

	@Test
	void elHorarioDecideCuandoSeReserva() throws Exception {
		// Abre de 20:00 a 02:00 (cierra al día siguiente)
		mvc.perform(post("/empresario/negocios/1/horario").with(como("empresario1")).with(csrf()).param("_method", "put")
			.param("horaApertura", "20:00").param("horaCierre", "02:00").param("duracionReserva", "90"))
			.andExpect(redirectedUrl("/empresario/negocios/1/calendario"));

		reservar("usuario1", dentroDeDias(5, 14), 2, "Mediodía").andExpect(status().isOk());
		reservar("usuario1", dentroDeDias(5, 23), 2, "Noche").andExpect(redirectedUrl("/mis/reservas"));
		reservar("usuario1", dentroDeDias(6, 1), 2, "Madrugada").andExpect(redirectedUrl("/mis/reservas"));
		assertEquals(0, contar("select count(*) from reserva where comentarios = 'Mediodía'"));
	}
}
