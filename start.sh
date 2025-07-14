#!/bin/bash

# F1 Betting Engine Startup Script
# This script starts all required services for the F1 betting application

set -e

echo "🏎️  Starting F1 Betting Engine..."
echo "=================================="

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if Docker Compose is available
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

echo "🐳 Starting infrastructure services (PostgreSQL)..."
docker-compose up -d postgres

echo "⏳ Waiting for services to be ready..."
sleep 15

echo "🔍 Checking service health..."

# Check PostgreSQL
if docker-compose exec -T postgres pg_isready -U f1user -d f1betting > /dev/null 2>&1; then
    echo "✅ PostgreSQL is ready"
else
    echo "❌ PostgreSQL is not ready. Please check the logs: docker-compose logs postgres"
    exit 1
fi

echo ""
echo "🚀 Starting Spring Boot application..."
echo "📝 You can monitor the application logs with: docker-compose logs -f app"

# Start the application in background
docker-compose up -d app

echo "⏳ Waiting for application to start and run database migrations..."
sleep 10

# Wait for application to be ready
echo "🔍 Checking application health..."
for i in {1..30}; do
    if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo "✅ Application is ready"
        break
    fi
    echo "⏳ Waiting for application... (${i}/30)"
    sleep 3
done

# Check if users were created
echo "🔍 Verifying test users creation..."
USER_COUNT=$(docker-compose exec -T postgres psql -U f1user -d f1betting -t -c "SELECT COUNT(*) FROM users;" | xargs)
if [ "$USER_COUNT" -gt 0 ]; then
    echo "✅ Test users created successfully ($USER_COUNT users found)"
    
    # Show test users
    echo ""
    echo "👥 Available test users:"
    docker-compose exec -T postgres psql -U f1user -d f1betting -c "SELECT username, email, balance FROM users;"
else
    echo "⚠️  No test users found. Database migrations may not have run properly."
fi

echo ""
echo "🎉 F1 Betting Engine is running!"
echo "=================================="
echo "📊 API Documentation: http://localhost:8080/api/v1/swagger-ui.html"
echo "🌐 Application: http://localhost:8080"
echo "🗄️  PostgreSQL: localhost:5432"
echo ""
echo "📋 Available endpoints:"
echo "  GET  /api/v1/events - List F1 events"
echo "  GET  /api/v1/events/{id} - Get event details"
echo "  GET  /api/v1/events/{id}/drivers - Get drivers for event"
echo "  POST /api/v1/bets - Place a bet"
echo "  GET  /api/v1/bets - List user bets"
echo "  POST /api/v1/events/{id}/outcome - Simulate race outcome"
echo "  GET  /api/v1/users/me - Get user profile"
echo ""
echo "💡 Testing tips:"
echo "  - The system uses 'user1' internally (no userId needed in requests)"
echo "  - Example: GET /api/v1/users/me"
echo "  - Example: GET /api/v1/users/me/bets"
echo "  - For more examples, see: documentation/QUICK_START.md or documentation/API_TESTING.md"
echo ""
echo "🛑 To stop all services: docker-compose down"
echo "📝 To view logs: docker-compose logs -f app" 