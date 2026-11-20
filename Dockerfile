# Imagen de SummerMate en dos etapas: se compila con Maven y se ejecuta el
# JAR de Spring Boot sobre un JRE, sin Maven ni el código fuente.
#
#   docker build -t summermate .
#   docker compose --profile app up -d     (aplicación + base de datos)

FROM maven:3.9-eclipse-temurin-21 AS compilacion
WORKDIR /src
# Primero solo el pom: las dependencias quedan en una capa que se reutiliza
# mientras no cambie
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S summermate && adduser -S summermate -G summermate
WORKDIR /app
COPY --from=compilacion /src/target/SummerMate-*.jar summermate.jar
USER summermate
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/summermate.jar"]
