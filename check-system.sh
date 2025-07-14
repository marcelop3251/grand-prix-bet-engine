#!/bin/bash

# F1 Betting Engine - System Check Script
# Verifica se o sistema está funcionando corretamente após remoção do Kong/Keycloak

echo "🔍 Verificando F1 Betting Engine..."
echo "================================="

# Function to check if a service is running
check_service() {
    local service_name=$1
    local url=$2
    local expected_code=${3:-200}
    
    echo -n "Checking $service_name... "
    
    if curl -s -o /dev/null -w "%{http_code}" "$url" | grep -q "$expected_code"; then
        echo "✅ OK"
        return 0
    else
        echo "❌ FAIL"
        return 1
    fi
}

# Check if services are running
echo "📊 Checking running services..."
if docker ps | grep -q "f1betting-postgres"; then
    echo "✅ PostgreSQL is running"
else
    echo "❌ PostgreSQL is not running"
    echo "💡 Try: docker-compose up -d postgres"
    exit 1
fi

if docker ps | grep -q "f1betting-app"; then
    echo "✅ Application container is running"
    app_running=true
else
    echo "⚠️  Application container is not running"
    echo "💡 Try: docker-compose up -d app"
    app_running=false
fi

echo ""
echo "🌐 Checking API endpoints..."

# Wait a bit for services to be ready
sleep 5

# Check main endpoints
if check_service "Health Check" "http://localhost:8080/api/v1/actuator/health"; then
    echo "✅ Application is healthy"
else
    echo "❌ Application health check failed"
    echo "💡 Check logs: docker-compose logs app"
    exit 1
fi

if check_service "Swagger UI" "http://localhost:8080/api/v1/swagger-ui.html"; then
    echo "✅ Swagger UI is accessible"
else
    echo "❌ Swagger UI is not accessible"
fi

if check_service "Events API" "http://localhost:8080/api/v1/events"; then
    echo "✅ Events API is working"
else
    echo "❌ Events API is not working"
fi

echo ""
echo "🗄️  Checking database connection..."

if docker-compose exec -T postgres pg_isready -U f1user -d f1betting > /dev/null 2>&1; then
    echo "✅ PostgreSQL is ready and accepting connections"
    
    # Check if tables exist
    table_count=$(docker-compose exec -T postgres psql -U f1user -d f1betting -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | tr -d ' ')
    
    if [ "$table_count" -gt 0 ]; then
        echo "✅ Database tables exist (count: $table_count)"
    else
        echo "⚠️  No tables found in database"
        echo "💡 Flyway migrations might need to run"
    fi
else
    echo "❌ PostgreSQL is not ready"
    echo "💡 Check logs: docker-compose logs postgres"
fi

echo ""
echo "🔧 System Summary:"
echo "=================="
echo "✅ PostgreSQL: Running on port 5432"
if [ "$app_running" = true ]; then
    echo "✅ Application: Running on port 8080"
else
    echo "⚠️  Application: Not running in Docker"
fi

echo ""
echo "🌐 Available URLs:"
echo "=================="
echo "📊 Application: http://localhost:8080"
echo "📚 Swagger UI: http://localhost:8080/api/v1/swagger-ui.html"
echo "🔍 Health Check: http://localhost:8080/api/v1/actuator/health"
echo "📋 API Base: http://localhost:8080/api/v1"

echo ""
echo "✅ System check completed!"
echo "💡 To start the system: ./start.sh or ./start-dev.sh" 