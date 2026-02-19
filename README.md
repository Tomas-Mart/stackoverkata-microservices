# 🚀 StackOverKata Microservices Platform

## 📋 О ПРОЕКТЕ

**StackOverKata** — это масштабируемая микросервисная платформа, построенная на современном реактивном стеке (Spring WebFlux + R2DBC) с полной Docker-контейнеризацией. Проект демонстрирует промышленный подход к разработке распределенных систем, включая централизованную конфигурацию, service discovery, асинхронную обработку и мониторинг.

**Роль в проекте:** Разработчик / Архитектор решения

---

## ✅ КЛЮЧЕВЫЕ РЕЗУЛЬТАТЫ

* **Разработано 8 микросервисов**, охватывающих ключевые бизнес-функции (аутентификация, профили, ресурсы, уведомления) и инфраструктурные компоненты (Gateway, Config Server, Discovery).
* **Оркестрировано 12 Docker-контейнеров**, включая специализированные базы данных PostgreSQL для каждого сервиса и брокеры сообщений RabbitMQ и Kafka.
* **Создана гибкая система конфигурации** с 4 профилями окружения (dev, docker, test, local), управляемая через единый `.env`-файл, содержащий более 120 параметров.
* **Устранено свыше 15 критических технических проблем**, включая конфликты версий Spring Cloud, ошибки аутентификации PostgreSQL, проблемы с healthcheck'ами в Alpine-образах и некорректную работу R2DBC-драйверов.
* **Инфраструктура полностью развернута и настроена в течение 1 недели**, что подтверждает высокую эффективность выбранного технологического стека и подхода к контейнеризации.

---

## 🏗 АРХИТЕКТУРА РЕШЕНИЯ

```
┌─────────────────────────────────────────────────────────────┐
│                    API GATEWAY (порт 8080)                   │
│                  Spring Cloud Gateway                        │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│  AUTH SERVICE │    │PROFILE SERVICE│    │RESOURCE SERVICE│
│   (порт 8180) │    │  (порт 8182) │    │  (порт 8181)  │
│    JWT + R2DBC│    │    R2DBC     │    │ R2DBC + Kafka │
└───────────────┘    └───────────────┘    └───────────────┘
        │                     │                     │
        ▼                     ▼                     ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│  PostgreSQL   │    │  PostgreSQL   │    │  PostgreSQL   │
│    auth_db    │    │  profile_db   │    │  resource_db  │
└───────────────┘    └───────────────┘    └───────────────┘
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
            ┌───────────────┐    ┌───────────────┐
            │   RabbitMQ    │    │     Kafka     │
            │  (очереди)    │    │  (стриминг)   │
            └───────────────┘    └───────────────┘
                    │                   │
                    ▼                   ▼
            ┌───────────────┐    ┌───────────────┐
            │ EMAIL SERVICE │    │     (events)  │
            │  (порт 8183)  │    │               │
            └───────────────┘    └───────────────┘

                    INFRASTRUCTURE
            ┌─────────────────────────────┐
            │   Eureka Server (порт 8761) │
            │   Config Server (порт 8888) │
            └─────────────────────────────┘
```

---

## ✅ ВЫПОЛНЕННЫЕ ЗАДАЧИ

### 1. ИНФРАСТРУКТУРНЫЕ КОМПОНЕНТЫ

* **Config Server** на базе **Spring Cloud Config** обеспечивает централизованное управление конфигурациями всех микросервисов.
* **Eureka Server (Netflix Eureka)** реализует автоматическую регистрацию и обнаружение сервисов.
* **API Gateway** на основе **Spring Cloud Gateway** служит единой точкой входа с маршрутизацией и балансировкой нагрузки.
* **PostgreSQL 17** с реактивным драйвером **R2DBC** обеспечивает неблокирующие подключения к базам данных для всех сервисов.
* **RabbitMQ** используется для асинхронной обработки сообщений.
* **Apache Kafka** задействована для стриминга событий в Resource Service.

### 2. БИЗНЕС-МИКРОСЕРВИСЫ

* **Auth Service (порт 8180)** — сервис аутентификации, отвечающий за регистрацию пользователей и выдачу JWT-токенов.
* **Profile Service (порт 8182)** — управляет профилями пользователей.
* **Resource Service (порт 8181)** — обеспечивает работу с файловыми ресурсами и интегрирован с Kafka и RabbitMQ.
* **Email Service (порт 8183)** — отправляет email-уведомления через очереди RabbitMQ.
* **Web Client (порт 8085)** — фронтенд-приложение на React/Vite, собранное в Nginx.

### 3. DOCKER-КОНТЕЙНЕРИЗАЦИЯ

✅ **Dockerfile для всех 8+ сервисов** — многоступенчатые сборки, оптимизация размера образов
✅ **docker-compose.yml** — оркестрация всех сервисов с зависимостями и healthcheck'ами
✅ **Сеть `stackover-network`** — изоляция и безопасное взаимодействие контейнеров
✅ **Volumes** — персистентное хранение данных PostgreSQL и RabbitMQ
✅ **Healthchecks** — автоматический контроль состояния каждого сервиса

### 4. РЕШЕНИЕ ТЕХНИЧЕСКИХ ПРОБЛЕМ

#### Зависимости и конфигурация
✅ **Исправление Bean Validation API** — добавление Hibernate Validator
✅ **Устранение конфликтов версий Spring Cloud** — миграция с 2021.0.1 на 2023.0.1
✅ **Исправление groupId для R2DBC PostgreSQL** — с `io.r2dbc` на `org.postgresql`
✅ **Настройка Spring Security** — корректная JWT аутентификация

#### Сеть и доступ
✅ **Зеркало Timeweb** — обход блокировок Docker Hub (`dockerhub.timeweb.cloud`)
✅ **Замена curl на wget** — решение проблем с healthcheck'ами в Alpine-образах
✅ **Миграция на Eclipse Temurin** — замена устаревших OpenJDK образов
✅ **Настройка WSL 2** — корректная сетевая интеграция Windows и Linux

#### Базы данных
✅ **Исправление ошибок аутентификации PostgreSQL** (28P01, 3D000)
✅ **Настройка R2DBC подключений** — реактивные драйверы
✅ **Создание и инициализация БД** — `auth_db`, `profile_db`, `resource_db`

#### RabbitMQ и очереди
✅ **Исправление NumberFormatException** — добавление значений по умолчанию (${RABBITMQ_PORT:5672})
✅ **Корректная передача переменных окружения**

#### Eureka Service Discovery
✅ **Устранение проблем с healthcheck'ами** — встроенные `HEALTHCHECK` в Dockerfile
✅ **Настройка регистрации сервисов** — корректная работа с именами контейнеров

### 5. КОНФИГУРАЦИЯ И УПРАВЛЕНИЕ

✅ **Единый `.env` файл** — централизованное управление 120+ переменными
✅ **Профили Spring** — dev, docker, test, local для разных окружений
✅ **Унификация конфигураций** — единый подход `DB_HOST`/`DB_PORT` для всех сервисов
✅ **Скрипты автоматизации** — `start.sh` для развертывания одним касанием

---

## 🎨 ФРОНТЕНД-РАЗРАБОТКА (WEB-CLIENT)

**Создание полноценного клиентского приложения в стиле StackOverflow**

✅ **Разработана полная клиентская часть на React/TypeScript** с нуля, включая:
* Настройка Vite для быстрой сборки и горячей перезагрузки
* Интеграция с Material-UI (MUI) для профессионального UI/UX
* Создание единой системы типов (TypeScript интерфейсы), полностью соответствующей backend-сущностям

✅ **Структурирование проекта:**
* Разработана модульная архитектура с разделением на компоненты, страницы, сервисы и типы
* Создана система роутинга с защищёнными маршрутами (PrivateRoute)
* Реализована многоуровневая структура папок (components, pages, services, types, styles)

✅ **Созданы ключевые компоненты:**
* `Layout`, `Header`, `Sidebar` — базовая структура страницы
* `QuestionList`, `QuestionDetail`, `QuestionForm` — работа с вопросами
* `AnswerList`, `AnswerForm` — система ответов
* `LoginPage`, `RegisterPage` — аутентификация
* `UsersPage`, `ProfilePage` — профили пользователей

✅ **API-интеграция:**
* Настроен axios-клиент с интерцепторами для обработки JWT-токенов
* Созданы сервисы для всех backend-эндпоинтов (auth, questions, answers, users, tags)
* Реализована обработка ошибок и автоматический редирект при 401 статусе

✅ **Управление состоянием:**
* Интеграция React Query для кэширования и синхронизации данных
* Оптимистичные обновления при голосовании и отправке ответов
* Пагинация на странице вопросов

✅ **UI/UX решения:**
* Адаптивный дизайн под все устройства
* Система голосования (лайки/дизлайки) для вопросов и ответов
* Отметка "принятый ответ" с визуальным выделением
* Отображение тегов в стиле StackOverflow
* Форматирование дат с библиотекой date-fns и локализацией на русский язык

✅ **Типизация данных:**
* Созданы TypeScript интерфейсы для всех backend-сущностей (User, Question, Answer, Tag, Chat, Reputation)
* Полная синхронизация с Java-моделями из resource-service
* Enum'ы для VoteType, RoleName, ReputationType

---

## 🛠 ИТОГОВЫЙ ТЕХНИЧЕСКИЙ СТЕК

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
├── React 18 + TypeScript
├── Vite (сборщик)
├── Material-UI (MUI) компоненты
├── React Router DOM
├── React Query (@tanstack/react-query)
├── Axios (HTTP-клиент)
├── date-fns (форматирование дат)
└── Nginx

DevOps:
├── Docker / Docker Compose
├── Maven
├── Prometheus + Grafana
├── Zipkin (трассировка)
└── WSL 2 (Windows Subsystem for Linux)
```

---

## 🏆 КЛЮЧЕВЫЕ ДОСТИЖЕНИЯ

* **Реактивный стек**: Выполнена полная миграция с блокирующего JPA на реактивный R2DBC, что обеспечило неблокирующую работу с базами данных и повысило общую производительность системы.

* **Отказоустойчивость**: Во все микросервисы внедрён Resilience4j circuit breaker для graceful degradation при сбоях и таймаутах.

* **Масштабируемость**: Разработана архитектура, включающая 8 бизнес-микросервисов и 4 инфраструктурных компонента, полностью контейнеризированных в Docker.

* **Асинхронность**: Реализована асинхронная обработка сообщений через RabbitMQ и стриминг событий через Apache Kafka.

* **Мониторинг**: Настроен полный стек observability (Prometheus для метрик, Grafana для визуализации, Zipkin для распределённой трассировки).

* **Безопасность**: Внедрена централизованная JWT-аутентификация на уровне API Gateway.

* **Конфигурация**: Создана гибкая система управления конфигурацией с единым `.env`-файлом (120+ переменных) и 4 профилями окружения (dev, docker, test, local).

* **Docker-инфраструктура**: 12 контейнеров оркестрируются одной командой `docker-compose up`, что обеспечивает лёгкость развёртывания и воспроизводимость окружения.

* **Полноценный frontend-клон StackOverflow**: Создано приложение, полностью повторяющее функционал и внешний вид известной платформы.

* **TypeScript-синхронизация**: Разработана система типов, полностью соответствующая backend-сущностям, что исключает ошибки несоответствия данных.

---

## 🚀 БЫСТРЫЙ СТАРТ

```bash
# 1. Клонировать репозиторий
git clone https://github.com/Tomas-Mart/stackoverkata-microservices.git
cd stackoverkata-microservices

# 2. Собрать все сервисы
mvn clean install -DskipTests

# 3. Запустить бэкенд через Docker Compose
docker-compose up -d

# 4. Запустить фронтенд
cd stackoverkata-web-client
npm install
npm run dev

# 5. Открыть в браузере
Eureka Dashboard: http://localhost:8761
API Gateway: http://localhost:8080
Web Client: http://localhost:5173
RabbitMQ Management: http://localhost:15672 (guest/guest)
```

---

## 📁 СТРУКТУРА ПРОЕКТА

```
stackoverkata-microservices/
├── stackoverkata-auth-service/          # Сервис аутентификации
├── stackoverkata-profile-service/       # Сервис профилей
├── stackoverkata-resource-service/      # Сервис ресурсов
├── stackoverkata-email-service/         # Email сервис
├── stackoverkata-gateway/               # API Gateway
├── stackoverkata-eureka-server/         # Service Discovery
├── stackoverkata-cloud-config-service/  # Config Server
├── stackoverkata-web-client/            # React фронтенд
├── stackoverkata-config-repo/           # Конфигурации для Config Server
├── docker-compose.yaml                   # Оркестрация всех сервисов
├── .env                                  # Переменные окружения
└── README.md                             # Документация
```

---

## 🔧 ПЕРЕМЕННЫЕ ОКРУЖЕНИЯ

Основные переменные в `.env` файле:

| Переменная | Значение | Описание |
|------------|----------|----------|
| `ACTIVE_PROFILE` | `docker` | Активный профиль Spring |
| `AUTH_SERVICE_PORT` | `8180` | Порт Auth Service |
| `PROFILE_SERVICE_PORT` | `8182` | Порт Profile Service |
| `RESOURCE_SERVICE_PORT` | `8181` | Порт Resource Service |
| `EMAIL_SERVICE_PORT` | `8183` | Порт Email Service |
| `GATEWAY_PORT` | `8080` | Порт Gateway |
| `EUREKA_SERVER_PORT` | `8761` | Порт Eureka Server |
| `CONFIG_SERVICE_PORT` | `8888` | Порт Config Server |
| `WEB_CLIENT_PORT` | `8085` | Порт Web Client (Docker) |
| `POSTGRES_USER` | `postgres` | Пользователь PostgreSQL |
| `POSTGRES_PASSWORD` | `root` | Пароль PostgreSQL |
| `RABBITMQ_USER` | `guest` | Пользователь RabbitMQ |
| `RABBITMQ_PASSWORD` | `guest` | Пароль RabbitMQ |

---

## 🐳 ЗАПУСК ЧЕРЕЗ DOCKER

```bash
# Запуск всех контейнеров
docker-compose up -d

# Просмотр логов
docker-compose logs -f

# Остановка всех контейнеров
docker-compose down

# Остановка с удалением volumes (очистка БД)
docker-compose down -v

# Перезапуск конкретного сервиса
docker-compose restart auth-service

# Масштабирование сервиса
docker-compose up -d --scale auth-service=3
```

---

## 🧪 ТЕСТИРОВАНИЕ API

### Auth Service
```bash
# Регистрация
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user","email":"user@test.com","password":"pass"}'

# Вход
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"pass"}'
```

### Email Service
```bash
# Отправка простого письма
curl -X POST http://localhost:8183/api/email/send \
  -H "Content-Type: application/json" \
  -d '{"to":"test@example.com","subject":"Test","text":"Hello"}'

# Отправка шаблонного письма
curl -X POST http://localhost:8183/api/email/send/template \
  -H "Content-Type: application/json" \
  -d '{"to":"test@example.com","templateName":"invite","templateData":{"name":"Иван"}}'
```

---

## 📊 МОНИТОРИНГ

| Сервис | URL | Логин/Пароль |
|--------|-----|--------------|
| Eureka Dashboard | http://localhost:8761 | - |
| RabbitMQ Management | http://localhost:15672 | guest/guest |
| Prometheus | http://localhost:9090 | - |
| Grafana | http://localhost:3000 | admin/admin |
| Zipkin | http://localhost:9411 | - |

---

## 🎯 РЕШЁННЫЕ ТЕХНИЧЕСКИЕ ПРОБЛЕМЫ

| Проблема | Решение |
|----------|---------|
| Блокировка Docker Hub | Настройка зеркала Timeweb (`dockerhub.timeweb.cloud`) |
| Отсутствие curl в Alpine | Замена на `wget` в healthcheck'ах |
| Конфликт версий Spring Cloud | Миграция с 2021.0.1 на 2023.0.1 |
| Ошибка 28P01 PostgreSQL | Исправление аутентификации |
| NumberFormatException RabbitMQ | Добавление значений по умолчанию (`:5672`) |
| Два primary бина MailSender | Удаление лишнего `@Primary` |
| Отсутствие JavaMailSender | Создание конфигурационного класса |
| Ошибка валидации DTO | Исправление типов (`Map<String, Object>` вместо `Object`) |

---

## 📝 ЛИЦЕНЗИЯ

MIT License

---

## 👩‍💻 АВТОР

**Ксения Томас-Март**  
Email: amina17101984@gmail.com  
GitHub: [Tomas-Mart](https://github.com/Tomas-Mart)

---

## ⚡ ЦИТАТА ИЗ ПРОЕКТА

> *"Этот проект демонстрирует навыки проектирования сложных распределённых систем, работы с современным Spring стеком, Docker-контейнеризации и решения реальных инфраструктурных задач."*

---

## 🎯 ИТОГ

✅ **8 микросервисов** — Auth, Profile, Resource, Email, Gateway, Eureka, Config, Web Client  
✅ **12 Docker контейнеров** — PostgreSQL, RabbitMQ, Kafka и сервисы  
✅ **120+ переменных конфигурации** — в едином `.env` файле  
✅ **4 профиля окружения** — dev, docker, test, local  
✅ **15+ решённых технических проблем**  
✅ **Полностью реактивный стек** — WebFlux + R2DBC  
✅ **Асинхронная обработка** — RabbitMQ + Kafka  
✅ **Мониторинг** — Prometheus + Grafana + Zipkin

**Готовая к деплою микросервисная платформа с полным циклом разработки!** 🚀