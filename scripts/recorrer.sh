#!/usr/bin/env bash
# recorrer.sh: entra con cada rol y pide las páginas principales, mostrando el
# código HTTP de cada una. Sirve para comprobar de un vistazo que la
# aplicación arrancada responde bien.
#
# Uso: scripts/recorrer.sh [http://localhost:8080]
# Necesita los datos de ejemplo (perfil dev).

BASE=${1:-http://localhost:8080}
TMP=$(mktemp -d)
trap 'rm -rf "$TMP"' EXIT
FALLOS=0

# Inicia sesión y deja la cookie en $TMP/$1.cookies
entrar() {
	local usuario=$1 jar="$TMP/$1.cookies" csrf
	csrf=$(curl -s -c "$jar" "$BASE/login" | grep -o 'name="_csrf" value="[^"]*"' | head -1 | sed 's/.*value="//; s/"$//')
	curl -s -o /dev/null -b "$jar" -c "$jar" --data-urlencode "username=$usuario" --data-urlencode "password=$usuario" \
		--data-urlencode "_csrf=$csrf" "$BASE/login"
}

pedir() {
	local quien=$1 ruta=$2 esperado=${3:-200} codigo
	if [ "$quien" = anonimo ]; then
		codigo=$(curl -s -o /dev/null -w '%{http_code}' "$BASE$ruta")
	else
		codigo=$(curl -s -o /dev/null -w '%{http_code}' -b "$TMP/$quien.cookies" "$BASE$ruta")
	fi
	if [ "$codigo" = "$esperado" ]; then
		printf '  ok   %-12s %s %s\n' "$quien" "$codigo" "$ruta"
	else
		printf '  MAL  %-12s %s %s (esperado %s)\n' "$quien" "$codigo" "$ruta" "$esperado"
		FALLOS=$((FALLOS + 1))
	fi
}

echo "Sin sesión"
for r in / /login /terminos /playas /playas/1 /playas/1/negocios /playas/cercanas "/playas/cercanas?lat=36.7&lon=-6.4&radio=50" \
	/negocios "/negocios?ciudad=chip" /negocios/1 /eventos "/eventos?cuando=proximos" /eventos/1 /usuarios "/usuarios?apellidos=a" \
	/registro/usuario /registro/empresario "/playas?sort=nombre&dir=desc&page=2"; do
	pedir anonimo "$r"
done
pedir anonimo /mis/reservas 302
pedir anonimo /admin/dashboard 302
# Cualquier ruta no declarada se deniega (como el antiguo "/**" denyAll): sin sesión, al login
pedir anonimo /no-existe 302

entrar usuario1
echo "Usuario (usuario1)"
for r in / /playas /mis/reservas /mis/eventos /mis/valoraciones/playas /mis/valoraciones/negocios /negocios/1/reservas/nueva \
	/playas/1/valoraciones/nueva /perfil /perfil/editar /perfil/contrasena /perfil/foto /carpetas /mensajes/nuevo /eventos /usuarios; do
	pedir usuario1 "$r"
done
# Recursos propios y ajenos (ids de los datos de ejemplo)
for r in /mis/valoraciones/playas/1/editar /mis/valoraciones/negocios/1/editar /carpetas/5; do
	pedir usuario1 "$r"
done
pedir usuario1 /mis/valoraciones/playas/2/editar 403
pedir usuario1 /carpetas/1 403
pedir usuario1 /admin/dashboard 403
pedir usuario1 /empresario/negocios 403

entrar empresario1
echo "Empresario (empresario1)"
for r in / /empresario/negocios /empresario/negocios/nuevo /empresario/eventos /empresario/eventos/nuevo /empresario/reservas \
	"/empresario/reservas?fecha=2016-08-10" /empresario/denuncias /empresario/peticion /perfil /perfil/editar /carpetas /negocios; do
	pedir empresario1 "$r"
done
for r in /empresario/negocios/1/editar /empresario/negocios/1/imagen /empresario/eventos/1/participantes \
	/empresario/valoraciones/1/denuncias/nueva /empresario/valoraciones/1/denuncias /empresario/denuncias/1/editar; do
	pedir empresario1 "$r"
done
# Los eventos de ejemplo ya se celebraron: no se pueden editar (regla de EventoService)
pedir empresario1 /empresario/eventos/1/editar 403
pedir empresario1 /empresario/negocios/2/editar 403
pedir empresario1 /empresario/eventos/2/participantes 403
pedir empresario1 /mis/reservas 403

entrar admin
echo "Administrador (admin)"
for r in / /admin/dashboard /admin/playas/nueva /admin/peticiones /admin/administradores/nuevo /playas /perfil /carpetas; do
	pedir admin "$r"
done
for r in /admin/playas/1/editar /admin/playas/1/imagen /admin/playas/3/ubicacion /admin/peticiones/3/aceptacion /admin/peticiones/3/rechazo; do
	pedir admin "$r"
done
# Playa con negocios: no se puede mover. Playa de otro administrador: no se puede tocar.
pedir admin /admin/playas/1/ubicacion 403
pedir admin /admin/playas/4/editar 403
pedir admin /mis/reservas 403

echo
[ $FALLOS -eq 0 ] && echo "Todo bien" || echo "$FALLOS fallos"
exit $FALLOS
