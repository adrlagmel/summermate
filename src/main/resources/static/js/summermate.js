// summermate.js: comportamiento común de todas las páginas.

(function () {
	'use strict';

	// Fase 6: sin aviso de cookies. Solo hay cookies técnicas (sesión, CSRF
	// e idioma), que no necesitan consentimiento (ver /terminos).

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
