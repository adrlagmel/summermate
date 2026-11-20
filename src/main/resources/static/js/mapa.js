// mapa.js: pinta con Leaflet las playas de window.SM_MAPA sobre teselas de
// OpenStreetMap.
//
// window.SM_MAPA = { puntos: [{id, nombre, latitud, longitud}], centro
// ({lat, lng} o null), zoom, enlace, textoEnlace, editable }. Los textos se
// insertan con textContent, así que un nombre con HTML se muestra tal cual y
// no se ejecuta.

(function () {
	'use strict';

	var contenedor = document.getElementById('mapa');
	if (!contenedor || typeof L === 'undefined')
		return;

	var datos = window.SM_MAPA || { puntos: [] };
	var puntos = datos.puntos || [];
	var centro = datos.centro || (puntos.length ? { lat: puntos[0].latitud, lng: puntos[0].longitud } : { lat: 36.5, lng: -6.0 });
	var mapa = L.map(contenedor).setView([centro.lat, centro.lng], datos.zoom || 7);

	// La atribución a OpenStreetMap es obligatoria por su licencia (ODbL)
	L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
		maxZoom: 19,
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
	}).addTo(mapa);

	var limites = [];
	puntos.forEach(function (p) {
		var contenido = document.createElement('div');
		var titulo = document.createElement('strong');
		var enlace = document.createElement('a');

		titulo.textContent = p.nombre;
		enlace.textContent = datos.textoEnlace;
		enlace.href = datos.enlace + encodeURIComponent(p.id);
		enlace.className = 'd-block mt-1';
		contenido.appendChild(titulo);
		contenido.appendChild(enlace);

		L.marker([p.latitud, p.longitud], { title: p.nombre }).addTo(mapa).bindPopup(contenido);
		limites.push([p.latitud, p.longitud]);
	});

	// Sin centro fijo, el mapa se ajusta para que se vean todas las playas
	if (!datos.centro && limites.length > 1)
		mapa.fitBounds(limites, { padding: [30, 30], maxZoom: 12 });

	// Para fijar la ubicación de una playa (administrador): un marcador
	// arrastrable que rellena los campos latitud y longitud del formulario.
	// Pulsar en el mapa también lo mueve.
	if (datos.editable) {
		var lat = document.getElementById('latitud');
		var lng = document.getElementById('longitud');
		var marcador = L.marker([centro.lat, centro.lng], { draggable: true, autoPan: true }).addTo(mapa);
		var copiar = function (pos) {
			lat.value = pos.lat.toFixed(6);
			lng.value = pos.lng.toFixed(6);
		};

		marcador.on('dragend', function () { copiar(marcador.getLatLng()); });
		mapa.on('click', function (e) {
			marcador.setLatLng(e.latlng);
			copiar(e.latlng);
		});
	}
})();
