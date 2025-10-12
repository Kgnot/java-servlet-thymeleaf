# Etapa 1: Construcción del proyecto con Gradle
FROM gradle:8.9-jdk21 AS build

# Copia todo el código fuente y el wrapper de Gradle
WORKDIR /app
COPY . .

# Compila el proyecto (sin tests)
RUN ./gradlew clean build -x test

# Etapa 2: Imagen ligera de ejecución
FROM eclipse-temurin:21-jdk-jammy

# Directorio de trabajo
WORKDIR /app

# Copiamos el JAR compilado desde la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto en el que Jetty escuchará
EXPOSE 8080

# Comando para ejecutar tu app
ENTRYPOINT ["java", "-jar", "app-jetty.jar"]
