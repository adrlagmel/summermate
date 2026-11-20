#!/usr/bin/env bash
#
# medir.sh: consultas SQL y tiempo de respuesta de las páginas principales.
#
# 1. docker compose up -d
# 2. mvn compile exec:java -Dexec.mainClass=utilities.PopulateDatabase
# 3. docker exec -i summermate-db psql -U summermate -d summermate < scripts/volumen.sql
# 4. mvn jetty:run -Dlog4j.configuration=file:scripts/log4j-sql.properties
# 5. (en otra terminal) bash scripts/medir.sh
#
# Cada página se pide dos veces y se mide la segunda, para no contar el
# arranque en frío.

set -euo pipefail

BASE=${BASE:-http://localhost:8080/SummerMate}
LOG=${LOG:-target/sql.log}
TMP=$(mktemp -d)
trap 'rm -rf "$TMP"' EXIT

login() { # usuario contraseña -> fichero de cookies
	local jar="$TMP/$1" token
	token=$(curl -s -c "$jar" "$BASE/security/login.do" | grep -oE 'name="_csrf" value="[^"]+"' | sed 's/.*value="//;s/"//')
	curl -s -b "$jar" -c "$jar" -o /dev/null --data-urlencode "_csrf=$token" -d "username=$1&password=$2" "$BASE/security/login.do"
	echo "$jar"
}

consultas() { grep -c '^@@SQL' "$LOG" 2>/dev/null || echo 0; }

medir() { # cookies página
	local antes despues r
	curl -s -b "$1" -o /dev/null "$BASE/$2"
	antes=$(consultas)
	r=$(curl -s -b "$1" -o /dev/null -w '%{http_code} %{time_total}' "$BASE/$2")
	sleep 0.3
	despues=$(consultas)
	awk -v p="$2" -v r="$r" -v n=$((despues - antes)) 'BEGIN { split(r, a, " "); printf "%-52s %s %6d consultas %6.0f ms\n", p, a[1], n, a[2] * 1000 }'
}

NADIE="$TMP/anonimo"; : > "$NADIE"
ADMIN=$(login admin admin)
EMPRESARIO=$(login empresario1 empresario1)
USUARIO=$(login usuario1 usuario1)

medir "$NADIE" welcome/index.do
medir "$ADMIN" playa/list.do
medir "$NADIE" "negocio/search.do?s=chip"
medir "$NADIE" evento/list.do
medir "$USUARIO" evento/usuario/list.do
medir "$NADIE" usuario/list.do
medir "$EMPRESARIO" negocio/empresario/list.do
medir "$EMPRESARIO" reserva/empresario/lista.do
medir "$USUARIO" reserva/usuario/lista.do
medir "$ADMIN" administrador/dashboard.do
medir "$USUARIO" carpeta/actor/lista.do
medir "$NADIE" "playa/nearToMe.do?lat=36.5&lon=-6.3"
