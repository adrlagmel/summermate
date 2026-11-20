#!/usr/bin/env bash
#
# medir.sh: consultas SQL y tiempo de respuesta de las páginas principales.
#
# 1. docker compose up -d db
# 2. mvn spring-boot:run -Dspring-boot.run.profiles=dev,sql
#    (perfil dev: datos de ejemplo; perfil sql: cada consulta en target/sql.log)
# 3. docker exec -i summermate-db psql -U summermate -d summermate < scripts/volumen.sql
# 4. (en otra terminal) bash scripts/medir.sh
#
# Fase 5: rutas nuevas. Entre paréntesis, la ruta equivalente de la Fase 3.
#
# Cada página se pide dos veces y se mide la segunda, para no contar el
# arranque en frío.

set -euo pipefail

BASE=${BASE:-http://localhost:8080}
LOG=${LOG:-target/sql.log}
TMP=$(mktemp -d)
trap 'rm -rf "$TMP"' EXIT

login() { # usuario contraseña -> fichero de cookies
	local jar="$TMP/$1" token
	token=$(curl -s -c "$jar" "$BASE/login" | grep -oE 'name="_csrf" value="[^"]+"' | head -1 | sed 's/.*value="//;s/"//')
	curl -s -b "$jar" -c "$jar" -o /dev/null --data-urlencode "_csrf=$token" -d "username=$1&password=$2" "$BASE/login"
	echo "$jar"
}

# Solo las líneas de org.hibernate.SQL (el perfil sql les pone el prefijo @@SQL)
consultas() { grep -ciE '^@@SQL +(select|insert|update|delete|with)' "$LOG" 2>/dev/null || echo 0; }

medir() { # cookies página
	local antes despues r
	curl -s -b "$1" -o /dev/null "$BASE$2"
	antes=$(consultas)
	r=$(curl -s -b "$1" -o /dev/null -w '%{http_code} %{time_total}' "$BASE$2")
	sleep 0.3
	despues=$(consultas)
	awk -v p="$2" -v r="$r" -v n=$((despues - antes)) 'BEGIN { split(r, a, " "); printf "%-52s %s %6d consultas %6.0f ms\n", p, a[1], n, a[2] * 1000 }'
}

NADIE="$TMP/anonimo"; : > "$NADIE"
ADMIN=$(login admin admin)
EMPRESARIO=$(login empresario1 empresario1)
USUARIO=$(login usuario1 usuario1)

medir "$NADIE" /                                          # welcome/index.do
medir "$ADMIN" /playas                                     # playa/list.do
medir "$NADIE" "/negocios?ciudad=chip"                     # negocio/search.do?s=chip
medir "$NADIE" /eventos                                    # evento/list.do
medir "$USUARIO" /eventos                                  # evento/usuario/list.do
medir "$NADIE" /usuarios                                   # usuario/list.do
medir "$EMPRESARIO" /empresario/negocios                   # negocio/empresario/list.do
medir "$EMPRESARIO" /empresario/reservas                   # reserva/empresario/lista.do
medir "$USUARIO" /mis/reservas                             # reserva/usuario/lista.do
medir "$ADMIN" /admin/dashboard                            # administrador/dashboard.do
medir "$USUARIO" /carpetas                                 # carpeta/actor/lista.do
medir "$NADIE" "/playas/cercanas?lat=36.5&lon=-6.3"        # playa/nearToMe.do
