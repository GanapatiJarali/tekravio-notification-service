FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy jar file from target folder
COPY target/tekravio-notification-service.jar tekravio-notification-service.jar

# Expose port
EXPOSE 8090

# Run the jar
ENTRYPOINT ["java", "-jar", "e-commerce-application.jar"]

