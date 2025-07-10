# Usar una imagen base de Java
FROM eclipse-temurin:21-jre

# Directorio de trabajo
WORKDIR /app

# Copiar el jar generado
COPY target/sistema-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"] 