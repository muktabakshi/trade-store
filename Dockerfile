# ===== Stage 1: Build the app =====
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app

# Copy source code
COPY . .

# Build application JAR
RUN ./gradlew clean bootJar --no-daemon

# ===== Stage 2: Run the app =====
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
