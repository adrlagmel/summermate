# SummerMate

Aplicación web para descubrir playas y reservar en los negocios de playa
(chiringuitos, heladerías…), con eventos, valoraciones y mensajería entre
usuarios, empresarios y administradores.

> El proyecto se está modernizando por fases. Stack actual: Java 21,
> Spring Boot 3.5 (Spring MVC 6, Spring Security 6, Hibernate 6), Thymeleaf
> con Bootstrap 5, Leaflet con OpenStreetMap para los mapas, Flyway y
> PostgreSQL 16.

## Requisitos

- JDK 21
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

Perfiles de Spring:

| Perfil | Para qué                                                        |
|--------|-----------------------------------------------------------------|
| `dev`  | Carga los datos de ejemplo y desactiva la caché de plantillas   |
| `sql`  | Escribe cada consulta SQL en `target/sql.log` (para medir)      |

Para cambiar la configuración de Docker Compose, copia `.env.example` como
`.env`. Maven **no** lee `.env`: exporta las variables en la terminal si
cambias la URL, el usuario o la contraseña.

El esquema de la base de datos lo gestiona Flyway: cualquier cambio es una
migración nueva (`V3__...sql`), nunca una edición de las que ya existen.

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
