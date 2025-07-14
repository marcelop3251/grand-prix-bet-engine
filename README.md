# Formula 1 Betting Engine

A comprehensive Formula 1 betting backend built with Kotlin, Spring Boot, and Clean Architecture. This application allows users to place bets on F1 events, view race information, and simulate race outcomes.

## 🏎️ Features

- **Event Management**: List F1 events from OpenF1 API with filtering capabilities
- **Betting System**: Place bets on drivers with randomly generated odds (2x, 3x, 4x)
- **User Management**: Simple user management for testing
- **Balance Management**: Fixed 100 EUR starting balance per user
- **Race Simulation**: Simulate race outcomes and update user balances
- **Clean Architecture**: Well-structured codebase following Clean Architecture principles

**Key Considerations:**
- 🔒 **Authentication**: Currently uses internal user system for testing
- 💾 **Database**: Uses H2 for tests, PostgreSQL for runtime
- 🎯 **Purpose**: Demonstration of Clean Architecture and F1 betting concepts
- 🧪 **Testing**: Comprehensive unit and integration test coverage

## 📚 Documentation

- **[🚀 Quick Start Guide](documentation/QUICK_START.md)** - Get started quickly with the API
- **[⚙️ Simplified Setup](documentation/SIMPLIFIED_SETUP.md)** - Complete setup guide without authentication
- **[🧪 API Testing Guide](documentation/API_TESTING.md)** - Detailed testing examples
- **[📋 Postman Collection](f1-event.postman_collection.json)** - Import into Postman for easy testing

## 🏗️ Architecture

The project follows Clean Architecture principles with the following layers:

```
src/main/kotlin/
├── domain/           # Business entities and rules
├── application/      # Use cases and application services
├── infrastructure/   # External APIs, repositories, JPA entities
└── interfaces/       # REST controllers and DTOs
```

## 🚀 Quick Start

### Prerequisites

- Docker and Docker Compose
- Java 21 or higher (LTS version recommended)
- Gradle (optional, project includes wrapper)

### Important Notes

- The application runs on port 8080 with context path `/api/v1`
- All API endpoints are prefixed with `/api/v1`
- Swagger UI is available at `/api/v1/swagger-ui.html`
- No authentication required for simplified testing

### Spring Profiles

The application supports different profiles for different environments:

- **`local`** (default): Local development
- **`docker`**: Production-like environment with Docker services

To run for development:
```bash
# Option 1: Use the development script
./start-dev.sh

# Option 2: Use Gradle directly
./gradlew bootRun
```

### Running the Application

> 💡 **Quick Start**: For a faster setup, check out the [Quick Start Guide](documentation/QUICK_START.md) or [Simplified Setup](documentation/SIMPLIFIED_SETUP.md).

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd grand-prix-bet-engine
   ```

2. **Start the infrastructure services**
   ```bash
   docker-compose up -d postgres
   ```

3. **Wait for services to be ready** (about 30 seconds)

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

5. **Access the application**
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/api/v1/swagger-ui.html

## 📋 API Endpoints

### API Access
All endpoints are available without authentication for simplified testing:

### Events
- `GET /api/v1/events` - List F1 events with optional filters
  - Query parameters: `year`, `country`, `sessionType`
- `GET /api/v1/events/{id}` - Get specific event details

### Drivers
- `GET /api/v1/events/{eventId}/drivers` - List drivers for an event with their odds

### Bets
- `POST /api/v1/bets` - Place a new bet
  ```json
  {
    "eventId": "123",
    "driverId": "456",
    "amount": 50.0
  }
  ```
- `GET /api/v1/bets` - List user's bets
- `GET /api/v1/bets/{id}` - Get specific bet details

### Outcomes
- `POST /api/v1/events/{id}/outcome` - Simulate race outcome
  ```json
  {
    "winnerDriverId": "456"
  }
  ```

### Users
- `GET /api/v1/users/me` - Get current user profile and balance
- `GET /api/v1/users/me/bets` - Get current user's betting history

## 🐳 Docker Services

The application uses the following Docker services:

- **PostgreSQL**: Database for storing application data
- **Application**: Spring Boot application

### Service URLs
- Application API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
- PostgreSQL: localhost:5432

## 🔧 Configuration

### Environment Variables

Key configuration options in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/f1betting
    username: f1user
    password: f1pass
  
openf1:
  base-url: https://api.openf1.org/v1
  timeout: 5000


```



## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Integration Tests
```bash
./gradlew integrationTest
```

### API Tests
```bash
./gradlew test --tests "*IntegrationTest"
```

## 📊 Database Schema

The application uses the following main entities:

- **User**: User information and balance
- **Event**: F1 race events
- **Driver**: F1 drivers
- **Bet**: User bets on events
- **Outcome**: Race results

## 🔒 Security

- Input validation and sanitization
- SQL injection prevention via JPA

## 📈 Monitoring

- Health check endpoint: `/api/v1/actuator/health`
- Metrics endpoint: `/api/v1/actuator/metrics`
- Application logs with structured logging

## 🛠️ Technology Stack

This application is built with modern technologies and best practices:

### Core Technologies
- **[Kotlin](https://kotlinlang.org/)** - Primary programming language
- **[Java 21](https://openjdk.org/projects/jdk/21/)** - Runtime environment with modern JVM features
- **[Spring Boot 3.2.3](https://spring.io/projects/spring-boot)** - Application framework
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)** - Data persistence layer
- **[PostgreSQL](https://www.postgresql.org/)** - Primary database
- **[H2 Database](https://www.h2database.com/)** - In-memory database for testing

### Development & Testing
- **[JUnit 5](https://junit.org/junit5/)** - Testing framework
- **[MockK](https://mockk.io/)** - Mocking library for Kotlin

- **[Kover](https://github.com/Kotlin/kotlinx-kover)** - Code coverage reporting
- **[Flyway](https://flywaydb.org/)** - Database migration tool

### API & Documentation
- **[SpringDoc OpenAPI](https://springdoc.org/)** - API documentation (Swagger)
- **[Bean Validation](https://beanvalidation.org/)** - Request validation
- **[WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)** - Reactive HTTP client

### Build & Deployment
- **[Gradle](https://gradle.org/)** - Build automation
- **[Docker](https://www.docker.com/)** - Containerization
- **[Docker Compose](https://docs.docker.com/compose/)** - Multi-container orchestration

## 🚀 Future Improvements

This application provides a solid foundation but could benefit from the following enhancements:

### Performance Optimizations
- **Database Connection Pooling**: Conduct performance tests to configure optimal connection pool settings
- **Caching Strategy**: Implement Redis or in-memory caching for frequently accessed data (events, drivers)
- **Query Optimization**: Add database indexes and optimize N+1 query patterns

### Resilience & Reliability
- **Circuit Breaker**: Implement circuit breaker pattern for external API calls (OpenF1)
- **Rate Limiting**: Add rate limiting to prevent API abuse
- **Bulkhead**: Isolate different types of operations (reads vs writes)
- **Retry Logic**: Implement exponential backoff for transient failures

### Security & Authentication
- **JWT Authentication**: Implement proper JWT-based authentication
- **Role-Based Access Control**: Add user roles and permissions
- **API Security**: Implement proper API key management and rate limiting

### Monitoring & Observability
- **Metrics**: Add custom metrics using Micrometer
- **Logging**: Implement structured logging with correlation IDs
- **Health Checks**: Enhance health check endpoints
- **Distributed Tracing**: Add tracing for better debugging

### Data & Analytics
- **Event Sourcing**: Consider event sourcing for audit trails
- **Analytics**: Add betting analytics and reporting features
- **Data Validation**: Implement comprehensive data validation rules

## 📋 Related Documents

- [🚀 Quick Start Guide](documentation/QUICK_START.md) - Get up and running in minutes
- [⚙️ Simplified Setup](documentation/SIMPLIFIED_SETUP.md) - Complete setup without authentication
- [🧪 API Testing Guide](documentation/API_TESTING.md) - Detailed testing examples
- [📋 Postman Collection](f1-event.postman_collection.json) - Import into Postman for easy API testing

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
