# SummerMate

Aplicación web para descubrir playas y reservar en los negocios de playa
(chiringuitos, heladerías…), con eventos, valoraciones y mensajería entre
usuarios, empresarios y administradores.

> El proyecto se está modernizando por fases. Stack actual: Java 17,
> Spring MVC 5.3, Spring Security 5.8, Hibernate 5.6 (JPA), JSP + Tiles 3 y
> PostgreSQL 16.

## Requisitos

- JDK 17 o superior
- Maven 3.6+
- Docker (para la base de datos y para los tests)

## Arrancar en local

1. Levanta PostgreSQL:

   ```bash
   docker compose up -d
   ```

2. Crea el esquema y carga los datos de ejemplo (`PopulateDatabase.xml`).
   **Borra todo lo que haya en la base de datos**:

   ```bash
   mvn compile exec:java -Dexec.mainClass=utilities.PopulateDatabase
   ```

3. Arranca la aplicación en <http://localhost:8080/SummerMate/>:

   ```bash
   mvn jetty:run
   ```

Usuarios de ejemplo (la contraseña es igual que el nombre de usuario):
`admin`, `administrador2`, `usuario1`…`usuario8` y `empresario1`…`empresario7`
(salvo `empresario4`, cuya contraseña es `empresario`). Se guardan con BCrypt;
`java utilities.HashPassword` genera hashes nuevos.

## Configuración

La conexión se lee de propiedades del sistema o de variables de entorno, y
si no están definidas usa los valores del `docker-compose.yml`:

| Variable      | Valor por defecto                              |
|---------------|------------------------------------------------|
| `DB_URL`      | `jdbc:postgresql://localhost:5432/summermate`  |
| `DB_USER`     | `summermate`                                   |
| `DB_PASSWORD` | `summermate`                                   |

Para cambiar la configuración de Docker Compose, copia `.env.example` como
`.env`. Maven y Jetty **no** leen `.env`: exporta las variables en la
terminal si cambias la URL, el usuario o la contraseña.

## Tests

```bash
mvn test
```

Los tests arrancan un PostgreSQL desechable con
[Testcontainers](https://testcontainers.com/), así que solo necesitan
Docker en marcha: no usan la base de datos de `docker compose`. Antes de
cada test se recrea el esquema y se cargan los datos de ejemplo.

## Medir el rendimiento

Los datos de ejemplo son pocos (6 eventos, 8 usuarios) y con ellos no se
ven los problemas de rendimiento. Para medir con un volumen realista:

1. Carga los datos de ejemplo y añade volumen (500 usuarios, 400 negocios,
   600 eventos, 10.000 reservas…):

   ```bash
   mvn compile exec:java -Dexec.mainClass=utilities.PopulateDatabase
   ```

   ```bash
   docker exec -i summermate-db psql -U summermate -d summermate < scripts/volumen.sql
   ```

2. Arranca la aplicación registrando cada consulta SQL en `target/sql.log`:

   ```bash
   mvn jetty:run -Dlog4j.configuration=file:scripts/log4j-sql.properties
   ```

3. En otra terminal, mide consultas y tiempo por página:

   ```bash
   bash scripts/medir.sh
   ```

Resultado de la Fase 3 con ese volumen (consultas SQL por página):

| Página                        | Antes | Después |
|-------------------------------|------:|--------:|
| `evento/list.do`              |   407 |       9 |
| `usuario/list.do`             |   518 |       2 |
| `reserva/empresario/lista.do` | 1.776 |       8 |
| `administrador/dashboard.do`  |   523 |       7 |
