/* ImportadorPlayas.java
 *
 * Importa playas desde un CSV (Fase 6). Pensado para el catálogo público de
 * playas (Guía de Playas del Ministerio para la Transición Ecológica, en
 * datos.gob.es), que cubre la mejora "extender a toda la costa" de la
 * memoria. Como los nombres de columna cambian entre versiones del fichero,
 * se reconocen por alias (sin tildes ni mayúsculas):
 *
 *   nombre        nombre, nombre_playa, playa
 *   provincia     provincia
 *   ciudad        municipio, termino_municipal, localidad, ciudad
 *   latitud       latitud, lat, coordenada_y
 *   longitud      lon, lng, coordenada_x, longitud_geo      (coordenada)
 *   extension     longitud, longitud_m, extension            (metros de playa)
 *   composicion   composicion, tipo_de_arena, arena
 *   descripcion   descripcion, observaciones
 *   servicios     servicios
 *   codigoPostal  codigo_postal, cp      (si falta, el de la capital de provincia)
 *
 * Separador ";" o ",", texto en UTF-8 o Latin-1. Una playa que ya existe (mismo
 * nombre y ciudad) o una fila incompleta se omite y se dice por qué.
 */

package services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Playa;
import repositories.PlayaRepository;

@Service
@Transactional
public class ImportadorPlayas {

	public record Omitida(int fila, String nombre, String motivo) {
	}

	public record Resultado(int creadas, List<Omitida> omitidas) {
	}


	private static final Map<String, String[]>	ALIAS			= Map.ofEntries(
		Map.entry("nombre", new String[] { "nombre", "nombre_playa", "playa" }),
		Map.entry("provincia", new String[] { "provincia" }),
		Map.entry("ciudad", new String[] { "municipio", "termino_municipal", "localidad", "ciudad" }),
		Map.entry("latitud", new String[] { "latitud", "lat", "coordenada_y" }),
		Map.entry("longitud", new String[] { "lon", "lng", "coordenada_x", "longitud_geo" }),
		Map.entry("extension", new String[] { "longitud", "longitud_m", "extension" }),
		Map.entry("composicion", new String[] { "composicion", "tipo_de_arena", "arena" }),
		Map.entry("descripcion", new String[] { "descripcion", "observaciones" }),
		Map.entry("servicios", new String[] { "servicios" }),
		Map.entry("codigoPostal", new String[] { "codigo_postal", "cp" }));

	// Código postal de la capital de cada provincia (los dos primeros dígitos
	// son el código de la provincia).
	private static final Map<String, Integer>	CP_PROVINCIA	= cpProvincias();

	@Autowired
	private PlayaService						playaService;

	@Autowired
	private PlayaRepository						playaRepository;

	@Autowired
	private Validator							validator;


	public Resultado importar(byte[] contenido) throws IOException {
		List<String> lineas = lineas(contenido);
		if (lineas.isEmpty())
			return new Resultado(0, List.of(new Omitida(0, "", "fichero vacío")));

		char separador = lineas.get(0).chars().filter(c -> c == ';').count() >= lineas.get(0).chars().filter(c -> c == ',').count() ? ';' : ',';
		List<String> cabecera = campos(lineas.get(0), separador);
		Map<String, Integer> columnas = columnas(cabecera);

		List<Omitida> omitidas = new ArrayList<>();
		if (!columnas.containsKey("nombre") || !columnas.containsKey("latitud") || !columnas.containsKey("longitud"))
			return new Resultado(0, List.of(new Omitida(1, "", "faltan columnas: hacen falta nombre, latitud y longitud (coordenada)")));

		Set<String> existentes = new HashSet<>();
		for (Playa p : playaRepository.findAll())
			existentes.add(clave(p.getNombre(), p.getLocalizacion().getCiudad()));

		int creadas = 0;
		for (int i = 1; i < lineas.size(); i++) {
			if (lineas.get(i).isBlank())
				continue;
			List<String> fila = campos(lineas.get(i), separador);
			String nombre = valor(fila, columnas, "nombre");
			try {
				Playa playa = playa(fila, columnas);
				String clave = clave(playa.getNombre(), playa.getLocalizacion().getCiudad());
				if (existentes.contains(clave)) {
					omitidas.add(new Omitida(i + 1, nombre, "ya existe"));
					continue;
				}
				Set<ConstraintViolation<Playa>> errores = validator.validate(playa);
				if (!errores.isEmpty()) {
					ConstraintViolation<Playa> e = errores.iterator().next();
					omitidas.add(new Omitida(i + 1, nombre, e.getPropertyPath() + ": " + e.getMessage()));
					continue;
				}
				playaService.save(playa);
				existentes.add(clave);
				creadas++;
			} catch (IllegalArgumentException incompleta) {
				omitidas.add(new Omitida(i + 1, nombre, incompleta.getMessage()));
			}
		}
		return new Resultado(creadas, omitidas);
	}

	private Playa playa(List<String> fila, Map<String, Integer> columnas) {
		String nombre = obligatorio(fila, columnas, "nombre");
		String provincia = valor(fila, columnas, "provincia");
		String ciudad = valor(fila, columnas, "ciudad");
		if (ciudad.isEmpty())
			ciudad = provincia;
		if (ciudad.isEmpty())
			throw new IllegalArgumentException("falta el municipio");

		Playa playa = playaService.create();
		playa.setNombre(nombre);
		playa.setDescripcion(o(valor(fila, columnas, "descripcion"), nombre));
		playa.setComposicion(o(valor(fila, columnas, "composicion"), "Sin datos"));
		playa.setServicios(o(valor(fila, columnas, "servicios"), "Sin datos"));
		playa.setExtension(entero(valor(fila, columnas, "extension"), 0));

		playa.getLocalizacion().setVia("Playa");
		playa.getLocalizacion().setNombreVia(nombre);
		playa.getLocalizacion().setCiudad(ciudad);
		playa.getLocalizacion().setProvincia(o(provincia, ciudad));
		playa.getLocalizacion().setLatitud(decimal(obligatorio(fila, columnas, "latitud"), "latitud"));
		playa.getLocalizacion().setLongitud(decimal(obligatorio(fila, columnas, "longitud"), "longitud"));

		Integer cp = entero(valor(fila, columnas, "codigoPostal"), null);
		if (cp == null)
			cp = CP_PROVINCIA.get(normalizar(provincia));
		if (cp == null)
			throw new IllegalArgumentException("falta el código postal y no se reconoce la provincia");
		playa.getLocalizacion().setCodigoPostal(cp);

		return playa;
	}

	// Lectura del CSV --------------------------------------------------------

	private static List<String> lineas(byte[] contenido) throws IOException {
		String texto;
		try {
			texto = StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(contenido)).toString();
		} catch (CharacterCodingException noEsUtf8) {
			texto = new String(contenido, StandardCharsets.ISO_8859_1);
		}
		if (texto.startsWith("﻿"))
			texto = texto.substring(1);
		List<String> result = new ArrayList<>();
		try (BufferedReader r = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(texto.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8))) {
			String linea;
			StringBuilder pendiente = null;
			while ((linea = r.readLine()) != null) {
				// Un campo entre comillas puede tener saltos de línea
				String actual = pendiente == null ? linea : pendiente.append('\n').append(linea).toString();
				if (actual.chars().filter(c -> c == '"').count() % 2 == 1) {
					pendiente = new StringBuilder(actual);
					continue;
				}
				pendiente = null;
				result.add(actual);
			}
		}
		return result;
	}

	static List<String> campos(String linea, char separador) {
		List<String> result = new ArrayList<>();
		StringBuilder campo = new StringBuilder();
		boolean comillas = false;
		for (int i = 0; i < linea.length(); i++) {
			char c = linea.charAt(i);
			if (c == '"') {
				if (comillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
					campo.append('"');
					i++;
				} else
					comillas = !comillas;
			} else if (c == separador && !comillas) {
				result.add(campo.toString().trim());
				campo.setLength(0);
			} else
				campo.append(c);
		}
		result.add(campo.toString().trim());
		return result;
	}

	private static Map<String, Integer> columnas(List<String> cabecera) {
		Map<String, Integer> posiciones = new HashMap<>();
		for (int i = 0; i < cabecera.size(); i++)
			posiciones.putIfAbsent(normalizar(cabecera.get(i)), i);

		Map<String, Integer> result = new HashMap<>();
		for (Map.Entry<String, String[]> e : ALIAS.entrySet())
			for (String alias : e.getValue())
				if (posiciones.containsKey(alias)) {
					result.put(e.getKey(), posiciones.get(alias));
					break;
				}
		return result;
	}

	static String normalizar(String s) {
		if (s == null)
			return "";
		String sinTildes = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		return sinTildes.toLowerCase(Locale.ROOT).trim().replaceAll("[^a-z0-9]+", "_").replaceAll("^_|_$", "");
	}

	private static String valor(List<String> fila, Map<String, Integer> columnas, String campo) {
		Integer i = columnas.get(campo);
		return i == null || i >= fila.size() ? "" : fila.get(i).trim();
	}

	private static String obligatorio(List<String> fila, Map<String, Integer> columnas, String campo) {
		String v = valor(fila, columnas, campo);
		if (v.isEmpty())
			throw new IllegalArgumentException("falta " + campo);
		return v;
	}

	private static String o(String valor, String porDefecto) {
		return valor == null || valor.isBlank() ? porDefecto : valor;
	}

	private static double decimal(String v, String campo) {
		try {
			return Double.parseDouble(v.replace(',', '.'));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(campo + " no es un número: " + v);
		}
	}

	// "1.200 m" -> 1200
	private static Integer entero(String v, Integer porDefecto) {
		String digitos = v == null ? "" : v.replaceAll("[^0-9]", "");
		if (digitos.isEmpty())
			return porDefecto;
		try {
			return Integer.valueOf(digitos);
		} catch (NumberFormatException e) {
			return porDefecto;
		}
	}

	private static String clave(String nombre, String ciudad) {
		return normalizar(nombre) + "|" + normalizar(ciudad);
	}

	private static Map<String, Integer> cpProvincias() {
		String[][] datos = {
			{ "alava", "01001" }, { "araba", "01001" }, { "albacete", "02001" }, { "alicante", "03001" }, { "alacant", "03001" },
			{ "almeria", "04001" }, { "avila", "05001" }, { "badajoz", "06001" }, { "illes_balears", "07001" }, { "baleares", "07001" },
			{ "islas_baleares", "07001" }, { "barcelona", "08001" }, { "burgos", "09001" }, { "caceres", "10001" }, { "cadiz", "11001" },
			{ "castellon", "12001" }, { "castello", "12001" }, { "ciudad_real", "13001" }, { "cordoba", "14001" }, { "a_coruna", "15001" },
			{ "coruna", "15001" }, { "la_coruna", "15001" }, { "cuenca", "16001" }, { "girona", "17001" }, { "gerona", "17001" },
			{ "granada", "18001" }, { "guadalajara", "19001" }, { "gipuzkoa", "20001" }, { "guipuzcoa", "20001" }, { "huelva", "21001" },
			{ "huesca", "22001" }, { "jaen", "23001" }, { "leon", "24001" }, { "lleida", "25001" }, { "lerida", "25001" },
			{ "la_rioja", "26001" }, { "lugo", "27001" }, { "madrid", "28001" }, { "malaga", "29001" }, { "murcia", "30001" },
			{ "navarra", "31001" }, { "ourense", "32001" }, { "orense", "32001" }, { "asturias", "33001" }, { "palencia", "34001" },
			{ "las_palmas", "35001" }, { "pontevedra", "36001" }, { "salamanca", "37001" }, { "santa_cruz_de_tenerife", "38001" },
			{ "cantabria", "39001" }, { "segovia", "40001" }, { "sevilla", "41001" }, { "soria", "42001" }, { "tarragona", "43001" },
			{ "teruel", "44001" }, { "toledo", "45001" }, { "valencia", "46001" }, { "valladolid", "47001" }, { "bizkaia", "48001" },
			{ "vizcaya", "48001" }, { "zamora", "49001" }, { "zaragoza", "50001" }, { "ceuta", "51001" }, { "melilla", "52001" } };
		Map<String, Integer> result = new HashMap<>();
		for (String[] d : datos)
			result.put(d[0], Integer.valueOf(d[1]));
		return result;
	}

}
