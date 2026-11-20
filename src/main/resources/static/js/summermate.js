// summermate.js: comportamiento común de todas las páginas.

(function () {
	'use strict';

	// Aviso de cookies: se muestra hasta que se cierra una vez.
	var aviso = document.getElementById('aviso-cookies');
	var recordar = function () {
		try {
			localStorage.setItem('summermate.cookies', '1');
		} catch (e) {
			// Navegación privada o almacenamiento bloqueado: se vuelve a mostrar
		}
	};
	try {
		if (aviso && !localStorage.getItem('summermate.cookies'))
			aviso.hidden = false;
	} catch (e) {
		if (aviso)
			aviso.hidden = false;
	}
	var cerrar = document.getElementById('cerrar-cookies');
	if (cerrar)
		cerrar.addEventListener('click', function () {
			aviso.hidden = true;
			recordar();
		});

	// Confirmación antes de enviar formularios que borran o cambian datos:
	// <form data-confirmar="¿Seguro?">. El texto llega como atributo (ya
	// escapado por Thymeleaf), nunca dentro de código JavaScript.
	document.addEventListener('submit', function (evento) {
		var form = evento.target;
		var pregunta = form.getAttribute('data-confirmar');
		if (pregunta && !window.confirm(pregunta))
			evento.preventDefault();
	});
})();
