# Use a base image with Java runtime
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the compiled JAR file from the target directory (adjust if using Gradle)
# Assumes you run `mvn package` first which creates the jar in 'target/'
COPY target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]