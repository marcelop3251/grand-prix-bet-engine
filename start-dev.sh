#!/bin/bash

echo "🚀 Starting F1 Betting Engine in Development Mode"
echo ""

# Check if PostgreSQL is running
if ! docker ps | grep -q postgres; then
    echo "📦 Starting PostgreSQL..."
    docker-compose up -d postgres
    echo "⏳ Waiting for PostgreSQL to be ready..."
    sleep 10
fi

echo "🔧 Starting application..."
echo ""

# Start the application
./gradlew bootRun

echo ""
echo "✅ Application started!"
echo "🌐 API: http://localhost:8080/api/v1"
echo "📚 Swagger UI: http://localhost:8080/api/v1/swagger-ui.html"
echo ""
echo "💡 You can now test endpoints:"
echo "   GET  /api/v1/events"
echo "   GET  /api/v1/events/{id}/drivers"
echo "   POST /api/v1/bets"
echo "   GET  /api/v1/bets"
echo ""
echo "🛑 Press Ctrl+C to stop" 