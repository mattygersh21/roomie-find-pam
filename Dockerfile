# Use an OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file into the container
COPY target/*.jar pam-0.0.1-SNAPSHOT.jar

# Expose port 8080 for the application
EXPOSE 8081

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "pam-0.0.1-SNAPSHOT.jar"]
