#!/bin/bash

echo "========================================="
echo "🚀 ЗАПУСК МИКРОСЕРВИСОВ"
echo "========================================="
echo "📁 Проект: stackoverkata-microservices"
echo "========================================="

# Создать папку для логов
mkdir -p logs
echo "📝 Логи будут сохраняться в папке logs/"

# Функция для принудительной очистки портов
clean_ports() {
    echo "🧹 Очистка занятых портов..."
    for port in 8761 8888 8080 9001 9002 9003 9004; do
        if lsof -ti:$port >/dev/null 2>&1; then
            echo "   Освобождаю порт $port..."
            lsof -ti:$port | xargs kill -9 2>/dev/null
        fi
    done
    sleep 3
}

# Функция для запуска сервиса с проверкой
start_service() {
    local service=$1
    local log_name=$2
    local port=$3
    local wait_time=${4:-10}

    echo ""
    echo "📌 Запуск $service на порту $port..."

    # Очищаем порт если занят
    if lsof -ti:$port >/dev/null 2>&1; then
        echo "⚠️  Порт $port занят. Освобождаю..."
        lsof -ti:$port | xargs kill -9 2>/dev/null
        sleep 2
    fi

    # Запускаем сервис и сохраняем PID
    (cd "$service" && mvn spring-boot:run > "../logs/$log_name.log" 2>&1) &
    local pid=$!

    # Ждем запуска
    sleep $wait_time

    # Проверяем, запустился ли сервис
    if kill -0 $pid 2>/dev/null; then
        echo "✅ $service запущен (PID: $pid)"
        echo "   Лог: tail -f logs/$log_name.log"
        return 0
    else
        echo "❌ $service НЕ ЗАПУСТИЛСЯ"
        echo "   Проверьте лог: cat logs/$log_name.log"
        echo "   Последние 10 строк лога:"
        tail -10 "logs/$log_name.log" 2>/dev/null || echo "   Лог файл пуст"
        return 1
    fi
}

# Функция ожидания порта
wait_for_port() {
    local port=$1
    local service=$2
    local max_attempts=${3:-30}
    local attempt=1

    echo -n "   Ожидание $service на порту $port"
    while ! nc -z localhost $port 2>/dev/null; do
        if [ $attempt -ge $max_attempts ]; then
            echo " ⏰ Таймаут ожидания $service"
            return 1
        fi
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done
    echo " ✅ ГОТОВО"
    return 0
}

# Очищаем все порты перед запуском
clean_ports

echo ""
echo "========================================="
echo "🚀 ЭТАП 1: Запуск Eureka Server"
echo "========================================="

# Сначала соберем Eureka Server
echo "📦 Сборка Eureka Server..."
cd stackoverkata-eureka-server
mvn clean install -DskipTests
cd ..

# Запускаем Eureka Server
start_service "stackoverkata-eureka-server" "eureka" "8761" 15

# Проверяем, запустился ли Eureka
if ! wait_for_port "8761" "Eureka Server" 30; then
    echo "❌ Критическая ошибка: Eureka Server не запустился"
    echo "   Проверьте лог: cat logs/eureka.log"
    exit 1
fi

echo ""
echo "========================================="
echo "🚀 ЭТАП 2: Запуск Config Server"
echo "========================================="

# Собираем Config Server
cd stackoverkata-cloud-config-service
mvn clean install -DskipTests
cd ..

# Запускаем Config Server
start_service "stackoverkata-cloud-config-service" "config" "8888" 15

if ! wait_for_port "8888" "Config Server" 30; then
    echo "❌ Config Server не запустился"
    echo "   Проверьте лог: cat logs/config.log"
    exit 1
fi

echo ""
echo "========================================="
echo "🚀 ЭТАП 3: Запуск Gateway"
echo "========================================="

# Собираем Gateway
cd stackoverkata-gateway
mvn clean install -DskipTests
cd ..

start_service "stackoverkata-gateway" "gateway" "8080" 15
wait_for_port "8080" "Gateway" 30

echo ""
echo "========================================="
echo "🚀 ЭТАП 4: Запуск бизнес-сервисов"
echo "========================================="

# Auth Service
cd stackoverkata-auth-service
mvn clean install -DskipTests
cd ..
start_service "stackoverkata-auth-service" "auth" "9001" 10
wait_for_port "9001" "Auth Service" 20

# Profile Service
cd stackoverkata-profile-service
mvn clean install -DskipTests
cd ..
start_service "stackoverkata-profile-service" "profile" "9002" 10
wait_for_port "9002" "Profile Service" 20

# Resource Service
cd stackoverkata-resource-service
mvn clean install -DskipTests
cd ..
start_service "stackoverkata-resource-service" "resource" "9003" 10
wait_for_port "9003" "Resource Service" 20

# Email Service
cd stackoverkata-email-service
mvn clean install -DskipTests
cd ..
start_service "stackoverkata-email-service" "email" "9004" 10
wait_for_port "9004" "Email Service" 20

echo ""
echo "========================================="
echo "✅ ЗАПУСК ЗАВЕРШЕН"
echo "========================================="

# Показываем статус всех запущенных процессов
echo ""
echo "📊 СТАТУС ЗАПУЩЕННЫХ ПРОЦЕССОВ:"
ps aux | grep spring-boot | grep -v grep

echo ""
echo "🌐 EUREKA DASHBOARD: http://localhost:8761"
echo "   Здесь должны появиться все сервисы через 1-2 минуты"

echo ""
echo "========================================="
echo "🛑 ОСТАНОВКА: ./stop-services.sh"
echo "========================================="

# Создаем улучшенный скрипт остановки
cat > stop-services.sh << 'STOPEOF'
#!/bin/bash

echo "========================================="
echo "🛑 Остановка всех микросервисов"
echo "========================================="

# Находим все Java процессы Spring Boot
PIDS=$(pgrep -f spring-boot:run)

if [ -z "$PIDS" ]; then
    echo "✅ Нет запущенных сервисов"
else
    echo "Останавливаю процессы: $PIDS"
    kill $PIDS 2>/dev/null
    sleep 3

    # Проверяем, остановились ли
    PIDS=$(pgrep -f spring-boot:run)
    if [ ! -z "$PIDS" ]; then
        echo "⚠️  Принудительное завершение: $PIDS"
        kill -9 $PIDS 2>/dev/null
    fi

    echo "✅ Все сервисы остановлены"
fi

# Дополнительно очищаем порты
for port in 8761 8888 8080 9001 9002 9003 9004; do
    if lsof -ti:$port >/dev/null 2>&1; then
        echo "🧹 Очищаю порт $port"
        lsof -ti:$port | xargs kill -9 2>/dev/null
    fi
done

echo "========================================="
STOPEOF

chmod +x stop-services.sh

echo ""
echo "📝 Для просмотра логов используйте:"
echo "  tail -f logs/eureka.log"
echo "  tail -f logs/config.log"
echo "  tail -f logs/gateway.log"