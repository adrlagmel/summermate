# SummerMate

Aplicación web para descubrir playas y reservar en los negocios de playa
(chiringuitos, heladerías…), con eventos, valoraciones y mensajería entre
usuarios, empresarios y administradores.

> El proyecto se está modernizando por fases. Stack actual: Java 25,
> Spring Boot 4.1 (Spring MVC 7, Spring Security 7, Hibernate 7), Thymeleaf
> con Bootstrap 5, Leaflet con OpenStreetMap para los mapas, Flyway y
> PostgreSQL 17.

## Requisitos

- JDK 25
- Maven 3.9+
- Docker (para la base de datos y para los tests)

## Arrancar en local

1. Levanta PostgreSQL:

   ```bash
   docker compose up -d
   ```

2. Arranca la aplicación en <http://localhost:8080/>:

   ```bash
   mvn spring-boot:run
   ```

   `spring-boot:run` activa el perfil `dev`: Flyway crea el esquema
   (`src/main/resources/db/migration`) y carga los datos de ejemplo
   (`src/main/resources/db/dev`). Sin ese perfil solo se crea el esquema.

También se puede empaquetar y arrancar el JAR:

```bash
mvn package
```

```bash
java -jar target/SummerMate-2.0.0-SNAPSHOT.jar --spring.profiles.active=dev
```

O todo con Docker (imagen en dos etapas, ver `Dockerfile`):

```bash
docker compose --profile app up -d --build
```

Con Docker Compose también arranca [Mailpit](https://mailpit.axllent.org/):
los avisos por correo de la aplicación se ven en <http://localhost:8025> y no
salen a ningún sitio.

Si tu base de datos de desarrollo es anterior a la Fase 4 (creada por
Hibernate, sin tabla `flyway_schema_history`), Flyway no arrancará sobre ella.
Bórrala y deja que la cree de nuevo:

```bash
docker compose down -v
```

Lo mismo si vienes de la Fase 6 o anterior: la base de datos pasó de
PostgreSQL 16 a 17 (Fase 7) y los datos de un volumen de la 16 no los abre la
17. En desarrollo basta con borrar el volumen; con datos reales habría que
volcarlos con `pg_dump` y cargarlos en la 17.

Usuarios de ejemplo (la contraseña es igual que el nombre de usuario):
`admin`, `administrador2`, `usuario1`…`usuario8` y `empresario1`…`empresario7`
(salvo `empresario4`, cuya contraseña es `empresario`). Se guardan con BCrypt;
`java utilities.HashPassword` genera hashes nuevos.

## Configuración

`src/main/resources/application.yml` lee la conexión de variables de entorno
y, si no están definidas, usa los valores del `docker-compose.yml`:

| Variable      | Valor por defecto                              |
|---------------|------------------------------------------------|
| `DB_URL`      | `jdbc:postgresql://localhost:5432/summermate`  |
| `DB_USER`     | `summermate`                                   |
| `DB_PASSWORD` | `summermate`                                   |
| `CORREO_ACTIVO` | `false`: los correos solo se escriben en el log |
| `MAIL_HOST`, `MAIL_PORT` | `localhost`, `1025` (Mailpit)          |
| `URL_BASE`    | `http://localhost:8080` (enlaces de los correos) |
| `PAGOS_PASARELA` | `simulada`: no cobra nada                   |
| `LEGAL_TITULAR`, `LEGAL_CONTACTO` | Titular y contacto del aviso legal |

Perfiles de Spring:

| Perfil | Para qué                                                        |
|--------|-----------------------------------------------------------------|
| `dev`  | Carga los datos de ejemplo y desactiva la caché de plantillas   |
| `sql`  | Escribe cada consulta SQL en `target/sql.log` (para medir)      |

Para cambiar la configuración de Docker Compose, copia `.env.example` como
`.env`. Maven **no** lee `.env`: exporta las variables en la terminal si
cambias la URL, el usuario o la contraseña.

El esquema de la base de datos lo gestiona Flyway: cualquier cambio es una
migración nueva (`V8__...sql`), nunca una edición de las que ya existen. Los
datos de ejemplo van en `db/dev` con números a partir de `V1000`; el perfil
`dev` usa `out-of-order` para que una base ya creada reciba las migraciones
nuevas del esquema.

## Funciones añadidas en la Fase 6

Sacadas de la memoria del TFG, que describía más de lo que se llegó a
programar:

- **Calendario del negocio** (`/empresario/negocios/{id}/calendario`):
  horario propio (puede cerrar de madrugada), duración de cada reserva, mesas
  y días cerrados. Con mesas, cada reserva ocupa la mesa libre más pequeña en
  la que cabe; sin mesas, se limita por aforo. Muestra la ocupación por
  franjas de media hora.
- **Borrar** eventos sin inscritos y negocios sin eventos ni reservas
  pendientes; **retirar** una petición de negocio pendiente.
- **Denuncias** de valoraciones por cualquier usuario o empresario, una vez
  por valoración, y **moderación** del administrador (`/admin/denuncias`,
  `/admin/negocios`).
- **Votos de "comentario útil"** en las valoraciones de negocio y **nivel de
  colaboración** del usuario (Turista, Veraneante, Local, Embajador).
- **Cuenta premium** del empresario: sus negocios salen como destacados. La
  pasarela de pago es simulada (`PasarelaSimulada`, no cobra nada); para
  cobrar de verdad hay que añadir otra implementación de `PasarelaPago`, por
  ejemplo con Stripe Checkout y su webhook.

Y para ponerla al día:

- **RGPD**: desde el perfil se descargan los datos (JSON) y se borra la
  cuenta (se anonimiza). Aviso legal y privacidad nuevos; sin aviso de
  cookies porque solo hay cookies técnicas.
- **Avisos por correo**: reserva confirmada, petición resuelta y mensaje
  nuevo.
- **Importar playas** desde un CSV (`/admin/playas/importacion`), pensado
  para la guía de playas del catálogo de datos abiertos. Ejemplo en
  `scripts/playas-ejemplo.csv`.

## Tests

```bash
mvn test
```

Los tests arrancan un PostgreSQL desechable con
[Testcontainers](https://testcontainers.com/), así que solo necesitan
Docker en marcha: no usan la base de datos de `docker compose`. Antes de
cada test se vacían las tablas y se cargan los datos de ejemplo. Además de
los tests de servicios, `src/test/java/web` prueba la capa web con MockMvc:
rutas por rol, CSRF, login y logout, y cada formulario.

Con la aplicación arrancada, `scripts/recorrer.sh` entra con cada rol y
comprueba el código HTTP de las páginas principales:

```bash
bash scripts/recorrer.sh http://localhost:8080
```

## Salud y métricas

Actuator escucha en un puerto aparte, el 8081, que no se publica a Internet
(en Docker Compose solo en `127.0.0.1`):

| Ruta | Para qué |
|------|----------|
| `/actuator/health` | Estado general (UP o DOWN) |
| `/actuator/health/liveness`, `/actuator/health/readiness` | Si el proceso vive y si puede atender peticiones (lo usa el `HEALTHCHECK` de la imagen) |
| `/actuator/info` | Versión y fecha de compilación |
| `/actuator/prometheus` | Métricas para Prometheus: peticiones HTTP, JVM, conexiones a la base de datos… |

```bash
curl -s localhost:8081/actuator/health
```

## Integración continua

`.github/workflows/ci.yml` pasa todos los tests en cada push y pull request
(GitHub trae Docker, así que Testcontainers funciona igual que en local) y
comprueba que la imagen de Docker se construye. Dependabot
(`.github/dependabot.yml`) abre pull requests cuando salen versiones nuevas de
las dependencias, las acciones o las imágenes base.

## Medir el rendimiento

Los datos de ejemplo son pocos (6 eventos, 8 usuarios) y con ellos no se
ven los problemas de rendimiento. Para medir con un volumen realista:

1. Arranca la aplicación con los datos de ejemplo y registrando cada
   consulta SQL en `target/sql.log`:

   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev,sql
   ```

2. Añade volumen (500 usuarios, 400 negocios, 600 eventos, 10.000
   reservas…):

   ```bash
   docker exec -i summermate-db psql -U summermate -d summermate < scripts/volumen.sql
   ```

3. En otra terminal, mide consultas y tiempo por página:

   ```bash
   bash scripts/medir.sh
   ```

Consultas SQL por página con ese volumen:

| Página (Fase 5)            | Antes (Fase 3)                | Fase 0 | Fase 3 | Fase 5 |
|----------------------------|-------------------------------|-------:|-------:|-------:|
| `/eventos`                 | `evento/list.do`              |    407 |      9 |      2 |
| `/usuarios`                | `usuario/list.do`             |    518 |      2 |      2 |
| `/empresario/reservas`     | `reserva/empresario/lista.do` |  1.776 |      8 |      3 |
| `/empresario/negocios`     | `negocio/empresario/list.do`  |        |     11 |      6 |
| `/playas` (administrador)  | `playa/list.do`               |        |      7 |      4 |
| `/admin/dashboard`         | `administrador/dashboard.do`  |    523 |      7 |      7 |

En la Fase 3 las listas tenían 5 filas por página; desde la Fase 5, 10.

## Rutas

Desde la Fase 5 las URLs siguen un estilo REST y las acciones que cambian
datos son `POST` (o `PUT`/`DELETE` con un campo oculto `_method`), siempre
con el token CSRF. La cabecera de cada controlador (`src/main/java/controllers`)
indica qué ruta antigua (`*.do`) sustituye cada una.

| Zona                | Prefijo                                                      |
|---------------------|--------------------------------------------------------------|
| Pública             | `/`, `/playas`, `/negocios`, `/eventos`, `/usuarios`, `/registro` |
| Usuario             | `/mis/...`, `/negocios/{id}/reservas`, `/playas/{id}/valoraciones`, `/eventos/{id}/inscripcion` |
| Empresario          | `/empresario/...`                                            |
| Administrador       | `/admin/...`                                                 |
| Cualquier actor     | `/perfil`, `/carpetas`, `/mensajes`                          |
