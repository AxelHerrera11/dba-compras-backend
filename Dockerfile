# ============================
# Etapa 1: build con Maven
# ============================
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Descarga las dependencias primero (aprovecha cache de Docker si el pom.xml no cambia)
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================
# Etapa 2: imagen final, solo con el JRE
# ============================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render asigna el puerto dinámicamente via la variable PORT
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
