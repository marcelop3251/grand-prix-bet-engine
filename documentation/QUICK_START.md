# Quick Start Guide

## 🚀 How It Works

The system automatically uses the **"user1"** user for all operations. You don't need to send userId in requests - everything is handled internally!

## 🏁 Quick Test

### 1. Start the system
```bash
./start.sh
```

### 2. Check user profile
```bash
curl -X GET "http://localhost:8080/api/v1/users/me"
```

**Response:**
```json
{
  "id": "test-user-01",
  "username": "user1",
  "email": "user1@f1betting.com",
  "balance": 100.00
}
```

### 3. List events
```bash
curl -X GET "http://localhost:8080/api/v1/events"
```

### 4. Place a bet
```bash
curl -X POST "http://localhost:8080/api/v1/bets" \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "EVENT_ID_HERE",
    "driverId": "DRIVER_ID_HERE", 
    "amount": 25.00
  }'
```

### 5. View your bets
```bash
curl -X GET "http://localhost:8080/api/v1/users/me/bets"
```

### 6. Check balance after betting
```bash
curl -X GET "http://localhost:8080/api/v1/users/me"
```

## 📋 Main Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users/me` | Current user profile |
| GET | `/api/v1/users/me/bets` | User bets |
| GET | `/api/v1/events` | List events |
| GET | `/api/v1/events/{id}/drivers` | Event drivers |
| POST | `/api/v1/bets` | Place bet |
| POST | `/api/v1/events/{id}/outcome` | Simulate outcome |

## 💡 Tips

- ✅ System uses "user1" automatically
- ✅ No need to send userId in requests
- ✅ Access Swagger: http://localhost:8080/api/v1/swagger-ui.html
- ✅ Initial balance: 100.00 EUR

## 🔍 Swagger UI

For a more user-friendly interface:
http://localhost:8080/api/v1/swagger-ui.html