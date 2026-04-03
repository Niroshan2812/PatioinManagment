# Stage 1: Build the application
# FIX: Spelled 'eclipse' correctly and used lowercase 'builder'
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory inside the builder container
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies offline for faster subsequent builds
RUN mvn dependency:go-offline -B

# Copy the actual source code
COPY src ./src

# Package the application (added -DskipTests to make the Docker build faster)
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre AS runner

# Set the working directory for the runtime container
WORKDIR /app

# FIX: Changed source path from './app/target/...' to the absolute path '/app/target/...'
# Copied the generated JAR from the 'builder' stage into this image as 'app.jar'
COPY --from=builder /app/target/*.jar app.jar
# Document that the container listens on port 4000
EXPOSE 4004

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]