# Simplified Setup Guide

## Architecture Overview

The system now uses a simplified architecture without authentication (Kong/Keycloak removed):

**Previous:** Kong (Gateway) → Keycloak (Auth) → Spring Boot → PostgreSQL  
**Current:** Spring Boot → PostgreSQL

## 🚀 Quick Start Options

### 1. Local Development (Recommended)

```bash
# Start PostgreSQL only
./start-dev.sh
```

This script:
- Starts PostgreSQL in container
- Runs the application locally with `./gradlew bootRun`

### 2. Complete Docker Environment

```bash
# Build and start everything
./start.sh
```

This option:
- Builds the application image
- Starts PostgreSQL + Application in containers
- Suitable for production-like testing

## 🐳 Docker Commands

### Basic Commands
```bash
# Start only database
docker-compose up -d postgres

# Start everything
docker-compose up -d

# Stop everything
docker-compose down

# Rebuild application
docker-compose build app --no-cache

# View logs
docker-compose logs -f app
```

### Database Management
```bash
# Connect to database
docker-compose exec postgres psql -U f1user -d f1betting

# Check database status
docker-compose exec postgres pg_isready -U f1user -d f1betting

# View database logs
docker-compose logs postgres
```

## ⚙️ Configuration

### Available Profiles

1. **local** (default) - Local development with PostgreSQL local
2. **docker** - Container execution

### Environment Variables

```bash
# Database (Docker)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/f1betting
SPRING_DATASOURCE_USERNAME=f1user
SPRING_DATASOURCE_PASSWORD=f1pass
```

### Application Properties

Key configurations in `application.yml`:

```yaml
# Database Configuration
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/f1betting
    username: f1user
    password: f1pass
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  
  flyway:
    enabled: true
    locations: classpath:db/migrations

# OpenF1 API Configuration
openf1:
  base-url: https://api.openf1.org/v1
  timeout: 5000

# Server Configuration
server:
  port: 8080
  servlet:
    context-path: /api/v1
```

## 🔧 Development

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test category
./gradlew test --tests "com.f1betting.application.usecases.*"

# With coverage
./gradlew test koverHtmlReport
```

### Code Quality

```bash
# Format code
./gradlew spotlessApply

# Check code formatting
./gradlew spotlessCheck

# Build without tests
./gradlew build -x test
```

## 🌐 Endpoints

After starting the system, access:

- **Application**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html
- **Health Check**: http://localhost:8080/api/v1/actuator/health

## 📊 Main API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/events` | List F1 events |
| GET | `/events/{id}` | Get event details |
| GET | `/events/{id}/drivers` | Get event drivers |
| POST | `/bets` | Place a bet |
| GET | `/users/me` | Get user profile |
| GET | `/users/me/bets` | Get user bets |
| POST | `/events/{id}/outcome` | Simulate race outcome |

## 🔍 Troubleshooting

### Common Issues

1. **Port 8080 in use**
   ```bash
   # Find process using port
   lsof -i :8080
   
   # Kill process
   kill -9 <PID>
   ```

2. **Database connection issues**
   ```bash
   # Check PostgreSQL status
   docker-compose exec postgres pg_isready -U f1user -d f1betting
   
   # Restart database
   docker-compose restart postgres
   ```

3. **Build failures**
   ```bash
   # Clean build
   ./gradlew clean build --no-cache
   
   # Reset Docker
   docker-compose down
   docker system prune -f
   ```

### System Verification

Use the verification script:
```bash
./check-system.sh
```

This script verifies:
- Services are running
- Database connectivity
- API endpoints are responding
- Health checks pass

## 📝 Additional Resources

- [Quick Start Guide](QUICK_START.md) - Get started quickly
- [API Testing Guide](API_TESTING.md) - Detailed testing examples
- [Postman Collection](../f1-event.postman_collection.json) - Import into Postman for easy testing
- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html 