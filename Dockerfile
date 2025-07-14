FROM eclipse-temurin:21-jdk-jammy as build

WORKDIR /workspace/app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies with specific flags for ARM64 compatibility
RUN ./gradlew dependencies --no-daemon --no-watch-fs

# Copy source code
COPY src src

# Build the application
RUN ./gradlew build -x test --no-daemon --no-watch-fs

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

# Install curl for health checks
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Create app user
RUN groupadd -g 1001 app && \
    useradd -u 1001 -g app -m app

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /workspace/app/build/libs/*.jar app.jar

# Change ownership to app user
RUN chown app:app app.jar

# Switch to app user
USER app

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 