# Etapa de construcción
FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia el JAR de la etapa 'build' a la etapa actual
COPY --from=build /app/target/Reservas-MS-Auth-Service.jar Reservas-MS-Auth-Service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Reservas-MS-Auth-Service.jar"]