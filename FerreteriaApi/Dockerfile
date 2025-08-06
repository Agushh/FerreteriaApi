FROM eclipse-temurin:24.0.2_12-jdk
ARG JAR_FILE=target/FerreteriaApi-0.0.1.jar
COPY ${JAR_FILE} app_ferreteria.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_ferreteria.jar"]
