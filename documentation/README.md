# F1 Betting Engine - Documentation

This folder contains comprehensive documentation for the F1 Betting Engine application.

## 📚 Available Documents

### Getting Started
- **[🚀 Quick Start Guide](QUICK_START.md)** - Get up and running in minutes
- **[⚙️ Simplified Setup](SIMPLIFIED_SETUP.md)** - Complete setup guide without authentication

### Testing & Development
- **[🧪 API Testing Guide](API_TESTING.md)** - Detailed examples for testing all endpoints
- **[📋 Postman Collection](../f1-event.postman_collection.json)** - Import into Postman for easy API testing

## 🔗 Quick Navigation

### For New Users
1. Start with **[Quick Start Guide](QUICK_START.md)** to get the system running quickly
2. Use **[API Testing Guide](API_TESTING.md)** to test the endpoints
3. Import the **[Postman Collection](../f1-event.postman_collection.json)** for GUI testing

### For Developers
1. Follow the **[Simplified Setup](SIMPLIFIED_SETUP.md)** for complete environment setup
2. Use **[API Testing Guide](API_TESTING.md)** for comprehensive testing examples
3. Check the main **[README](../README.md)** for architecture and technology details

## 🛠️ Tools & Resources

- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html (when running)
- **Postman Collection**: [f1-event.postman_collection.json](../f1-event.postman_collection.json)
- **Health Check**: http://localhost:8080/actuator/health (when running)

## 🏃‍♂️ Quick Commands

```bash
# Quick start
./start.sh

# Development mode
./start-dev.sh

# Check system health
./check-system.sh

# View logs
docker-compose logs -f app
```

---

For more information, see the main [README](../README.md) file. 