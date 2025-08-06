# Etapa 1: Build del proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
# Etapa 2: Imagen final con solo el .jar
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/FerreteriaApi-0.0.1.jar app_ferreteria.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_ferreteria.jar"]