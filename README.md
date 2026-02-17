
# 🚀 StackOverKata Microservices Platform

## 📋 О ПРОЕКТЕ

Масштабируемая микросервисная платформа на Spring Cloud с реактивным стеком (WebFlux + R2DBC). Проект демонстрирует современный подход к разработке распределенных систем с использованием Java 17, Spring Boot 3.2 и Docker-контейнеризации.

---

## 🏗 АРХИТЕКТУРА

```
┌─────────────────────────────────────────────────────────────┐
│                        API GATEWAY                          │
│                         (порт 8080)                          │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│ AUTH SERVICE  │    │PROFILE SERVICE│    │RESOURCE SERVICE│
│   (порт 8181) │    │  (порт 8182)  │    │  (порт 8183)  │
└───────────────┘    └───────────────┘    └───────────────┘
        │                     │                     │
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│  PostgreSQL   │    │  PostgreSQL   │    │  PostgreSQL   │
│   auth_db     │    │  profile_db   │    │  resource_db  │
└───────────────┘    └───────────────┘    └───────────────┘
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
            ┌───────────────┐    ┌───────────────┐
            │   RabbitMQ    │    │     Kafka     │
            │  (очереди)    │    │  (стриминг)   │
            └───────────────┘    └───────────────┘
```

---

## ✅ **ВЫПОЛНЕННЫЕ ЗАДАЧИ**

### **1. НАСТРОЙКА ИНФРАСТРУКТУРЫ**
- ✅ Установка и настройка PostgreSQL 17
- ✅ Конфигурация Maven с зеркалом Aliyun для ускорения сборки
- ✅ Настройка окружения Windows + WSL для разработки
- ✅ Создание единого `.env` файла с 120+ переменными для всех окружений

### **2. КОНФИГУРАЦИОННЫЙ СЕРВЕР (Config Server)**
- ✅ Исправление ошибки Bean Validation API
- ✅ Настройка Spring Cloud Config Server с Git-репозиторием
- ✅ Интеграция с Eureka для service discovery

### **3. SERVICE DISCOVERY (Eureka Server)**
- ✅ Запуск и настройка Eureka Server
- ✅ Конфигурация сетевых интерфейсов для WSL
- ✅ Устранение проблем с подключением клиентов

### **4. API GATEWAY**
- ✅ Исправление конфликтов версий Spring Cloud (2021.0.1 → 2023.0.1)
- ✅ Настройка маршрутизации для всех микросервисов
- ✅ Интеграция с Eureka для динамического обнаружения

### **5. AUTH SERVICE**
- ✅ Миграция с JPA на реактивный R2DBC стек
- ✅ Исправление groupId для PostgreSQL драйвера
- ✅ Настройка JWT аутентификации
- ✅ Создание профилей: dev, docker, test, local
- ✅ Интеграция с PostgreSQL (устранение ошибок 28P01 и 3D000)

### **6. PROFILE SERVICE**
- ✅ Очистка от JPA/Hibernate конфигураций
- ✅ Настройка реактивных репозиториев
- ✅ Создание полноценной конфигурации для всех окружений

### **7. RESOURCE SERVICE**
- ✅ Интеграция с RabbitMQ и Kafka
- ✅ Настройка реактивного стека с R2DBC
- ✅ Конфигурация для работы с файловыми ресурсами

### **8. EMAIL SERVICE**
- ✅ Базовая конфигурация для отправки email
- ✅ Интеграция с RabbitMQ для асинхронной обработки

### **9. WEB CLIENT**
- ✅ Настройка Vite + React фронтенда
- ✅ Интеграция с Gateway API

### **10. DOCKER-КОНТЕЙНЕРИЗАЦИЯ**
- ✅ Создание Dockerfile для всех сервисов
- ✅ Разработка docker-compose.yml с 10+ сервисами
- ✅ Настройка сетей и volume'ов для PostgreSQL
- ✅ Создание профилей для разных окружений

---

## 🛠 **ТЕХНИЧЕСКИЙ СТЕК**

```
Backend:
├── Java 17
├── Spring Boot 3.2.4
├── Spring Cloud 2023.0.1
├── Spring WebFlux (реактивный)
├── Spring Security + JWT
├── Spring Data R2DBC
├── PostgreSQL 17
├── RabbitMQ
├── Apache Kafka
├── Eureka Discovery
├── Spring Cloud Gateway
├── Resilience4j (circuit breaker)
└── OpenFeign

Frontend:
├── React/Vite
└── Axios

DevOps:
├── Docker / Docker Compose
├── Maven
├── Grafana
├── Prometheus
└── Zipkin
```

---

## 📊 **КЛЮЧЕВЫЕ ДОСТИЖЕНИЯ**

| № | Достижение | Описание |
|---|------------|----------|
| 1 | **Реактивный стек** | Миграция с блокирующего JPA на реактивный R2DBC |
| 2 | **Отказоустойчивость** | Внедрение Resilience4j circuit breaker |
| 3 | **Масштабируемость** | 10+ микросервисов с Docker-контейнеризацией |
| 4 | **Асинхронность** | Интеграция RabbitMQ и Kafka |
| 5 | **Мониторинг** | Prometheus + Grafana + Zipkin |
| 6 | **Безопасность** | JWT аутентификация через Gateway |

---

## 🚀 **БЫСТРЫЙ СТАРТ**

```bash
# 1. Клонировать репозиторий
git clone https://github.com/your-repo/stackoverkata-microservices.git

# 2. Собрать все сервисы
mvn clean install -DskipTests

# 3. Запустить через Docker Compose
docker-compose up --build

# 4. Открыть в браузере
Eureka Dashboard: http://localhost:8761
API Gateway: http://localhost:8080
Web Client: http://localhost:8085
```

---

## 📁 **СТРУКТУРА ПРОЕКТА**

```
stackoverkata-microservices/
├── stackoverkata-cloud-config-service/
├── stackoverkata-eureka-server/
├── stackoverkata-gateway/
├── stackoverkata-auth-service/
├── stackoverkata-profile-service/
├── stackoverkata-resource-service/
├── stackoverkata-email-service/
├── stackoverkata-web-client/
├── docker-compose.yml
├── .env
└── README.md
```

---

## 📧 **КОНТАКТЫ**

По вопросам сотрудничества: [ваш email]

---

## ⚖️ **ЛИЦЕНЗИЯ**

MIT License

---

### ✨ **Этот проект демонстрирует навыки:**
- Проектирование микросервисной архитектуры
- Реактивное программирование с Spring WebFlux + R2DBC
- Docker-контейнеризация и оркестрация
- Работа с очередями сообщений (RabbitMQ, Kafka)
- Настройка мониторинга и трейсинга
- Решение сложных инфраструктурных задач

---

## ✅ **ИТОГ:**
**Готовая к деплою микросервисная платформа с полным циклом разработки!** 🚀