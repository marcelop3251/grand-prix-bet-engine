# API Testing Guide - Internal User

## How the System Works

The system uses the **"user1"** user internally for all operations. You don't need to send userId in requests - the system automatically uses the default user.

## Internal User Configuration

After running `./start.sh`, the test user is created automatically:

### User1 (Default User)
- **ID**: `test-user-01`
- **Username**: `user1`
- **Email**: `user1@f1betting.com`
- **Balance**: 100.00 EUR

## API Usage Examples

### 1. Check user profile

```bash
# Get current user profile (user1)
curl -X GET "http://localhost:8080/api/v1/users/me"
```

### 2. List available events

```bash
# List all events
curl -X GET "http://localhost:8080/api/v1/events"

# Filter events by year
curl -X GET "http://localhost:8080/api/v1/events?year=2024"
```

### 3. Get drivers for an event

```bash
# Replace {eventId} with a real event ID
curl -X GET "http://localhost:8080/api/v1/events/{eventId}/drivers"
```

### 4. Place a bet

```bash
# Place a bet (user is internal - user1)
curl -X POST "http://localhost:8080/api/v1/bets" \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "{eventId}",
    "driverId": "{driverId}",
    "amount": 25.00
  }'
```

### 5. List user bets

```bash
# List all bets from current user (user1)
curl -X GET "http://localhost:8080/api/v1/users/me/bets"
```

### 6. Simulate race outcome

```bash
# Simulate outcome (replace with real IDs)
curl -X POST "http://localhost:8080/api/v1/events/{eventId}/outcome" \
  -H "Content-Type: application/json" \
  -d '{
    "winnerDriverId": "{driverId}"
  }'
```

## Complete Test Flow

### 1. Start the system
```bash
./start.sh
```

### 2. Check user profile
```bash
curl -X GET "http://localhost:8080/api/v1/users/me"
```

### 3. List available events
```bash
curl -X GET "http://localhost:8080/api/v1/events"
```

### 4. Get drivers for an event
```bash
# Use a real eventId from previous response
curl -X GET "http://localhost:8080/api/v1/events/{eventId}/drivers"
```

### 5. Place a bet
```bash
curl -X POST "http://localhost:8080/api/v1/bets" \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "{eventId}",
    "driverId": "{driverId}",
    "amount": 25.00
  }'
```

### 6. Check bets
```bash
curl -X GET "http://localhost:8080/api/v1/users/me/bets"
```

### 7. Simulate outcome
```bash
curl -X POST "http://localhost:8080/api/v1/events/{eventId}/outcome" \
  -H "Content-Type: application/json" \
  -d '{
    "winnerDriverId": "{driverId}"
  }'
```

### 8. Check updated balance
```bash
curl -X GET "http://localhost:8080/api/v1/users/me"
```

## Database Verification

To verify directly in the database:

```bash
# Connect to PostgreSQL
docker-compose exec postgres psql -U f1user -d f1betting

# Check users
SELECT * FROM users;

# Check bets
SELECT * FROM bets;

# Check events
SELECT * FROM events;

# Exit PostgreSQL
\q
```

## Swagger UI

For a more user-friendly interface, access:
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html

In the Swagger interface, you can:
1. Test all endpoints
2. See complete API documentation
3. Use the test user in requests

## Application Logs

To monitor the application:

```bash
# View application logs
docker-compose logs -f app

# View PostgreSQL logs
docker-compose logs -f postgres

# View all service logs
docker-compose logs -f
``` 