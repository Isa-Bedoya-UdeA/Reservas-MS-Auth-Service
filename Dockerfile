# Etapa de construcción
FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia el JAR de la etapa 'build' a la etapa actual
COPY --from=build /app/target/reservas-ms-auth-service.jar reservas-ms-auth-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","reservas-ms-auth-service.jar"]