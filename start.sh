#!/bin/bash
docker-compose down
docker network create stackover-network 2>/dev/null

echo "🚀 Запуск Eureka Server..."
docker-compose up -d eureka-server && sleep 60

echo "🚀 Запуск Config Server..."
docker-compose up -d config-service && sleep 30

echo "🚀 Запуск баз данных..."
docker-compose up -d postgres-auth postgres-profile postgres-resource rabbitmq && sleep 30

echo "🚀 Запуск бизнес-сервисов..."
docker-compose up -d auth-service profile-service resource-service email-service && sleep 60

echo "🚀 Запуск Gateway..."
docker-compose up -d gateway-service && sleep 30

echo "🚀 Запуск Web Client..."
docker-compose up -d web-client

echo "✅ Готово! Проверьте:"
echo "Eureka: http://localhost:8761"
echo "Web Client: http://localhost:8085"
docker-compose ps