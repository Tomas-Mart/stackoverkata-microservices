# StackoverKata - Auth-Service

# Руководство по настройке и запуску проекта stackoverkata-auth-service

## 📁 Структура конфигурации

├── src/main/resources/
│ ├── application.yml # Основной конфиг
│ ├── application-dev.yml # Конфигурация для профиля DEV
│ ├── application-local.yml # Конфигурация для профиля LOCAL
│ └── ... # Другие ресурсы

## 🛠 Настройка профилей

### 1. Выбор активного профиля

Используйте VM Options для указания нужного профиля:

```bash
-Dspring.profiles.active=dev    # Для разработки
-Dspring.profiles.active=local  # Для локальной работы

2. Настройка переменных окружения
Добавьте переменные в Environment Variables для каждого профиля:

Для профиля DEV:

DB_PROTOCOL=jdbc:postgresql
DB_DRIVER=org.postgresql.Driver
HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
DB_HOST=ваш_хост
DB_PORT=5432
DB_NAME=dev_auth_service
DB_USER=ваш_пользователь
DB_PASSWORD=ваш_пароль

Для профиля LOCAL:

LOCAL_DB_PROTOCOL=jdbc:postgresql
LOCAL_DB_DRIVER=org.postgresql.Driver
LOCAL_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
LOCAL_DB_HOST=localhost
LOCAL_DB_PORT=5432
LOCAL_DB_NAME=local_auth_service
LOCAL_DB_USER=postgres
LOCAL_DB_PASSWORD=root

💻 Настройка в IntelliJ IDEA
Откройте Run/Debug Configurations

В поле VM options укажите:

-Dspring.profiles.active=ваш_профиль

В секции Environment variables добавьте переменные для выбранного профиля

🚀 Запуск приложения
Для профиля DEV:

# VM Options:
-Dspring.profiles.active=dev

# Environment Variables:
DB_HOST=127.0.0.1
DB_USER=admin
DB_PASSWORD=SecurePass123!

# VM Options:
-Dspring.profiles.active=local

# Environment Variables (опционально):
LOCAL_DB_USER=custom_user
LOCAL_DB_PASSWORD=mysecret

🔍 Проверка конфигурации
После запуска проверьте в логах:

INFO  [main] o.s.c.a.AnnotationConfigApplicationContext : Active profiles: dev
INFO  [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
INFO  [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.

⚙ Особенности профилей
Профиль DEV (рекомендуется для разработки):
Подключение к внешней БД

Включен Eureka Client

Использует Liquibase для миграций

Валидация схемы БД (ddl-auto: validate)

Профиль LOCAL (для локальной работы):
Встроенная PostgreSQL

Отключен Eureka

Автообновление схемы (ddl-auto: update)

Уменьшенный размер пула соединений

❗ Важные примечания
Все чувствительные данные (пароли) должны храниться ТОЛЬКО в переменных окружения

Для смены СУБД измените соответствующие параметры:

DB_DRIVER=com.mysql.cj.jdbc.Driver
HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect

Для кастомизации параметров подключения добавьте query-параметры в URL:

DB_PROTOCOL=jdbc:postgresql://host:port/db?ssl=true&prepareThreshold=0

🆘 Поддержка
При возникновении проблем:

Проверьте логи запуска

Убедитесь в доступности БД:

telnet $DB_HOST $DB_PORT

Проверьте права пользователя БД

Убедитесь в соответствии версий СУБД и драйвера

## 🌐 Использование файла .env в проекте

### 📌 Зачем нужен .env файл?
Файл `.env` позволяет хранить все переменные окружения в одном месте, что:
- Упрощает управление конфигурацией
- Делает настройки переносимыми между средами
- Позволяет избежать хранения чувствительных данных в коде
- Упрощает совместную работу над проектом

### 🛠 Настройка .env файла

1. Создайте файл `.env` в корне проекта:
```bash
touch .env

Добавьте файл в .gitignore:
```bash
echo ".env" >> .gitignore

Пример содержимого для разных профилей:

# Общие настройки
SPRING_PROFILES_ACTIVE=dev

########################
### Для профиля DEV ###
########################
DB_PROTOCOL=jdbc:postgresql
DB_DRIVER=org.postgresql.Driver
HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
DB_HOST=localhost
DB_PORT=5432
DB_NAME=dev_auth_service
DB_USER=dev_user
DB_PASSWORD=Strong!Password123

##########################
### Для профиля LOCAL ###
##########################
LOCAL_DB_PROTOCOL=jdbc:postgresql
LOCAL_DB_DRIVER=org.postgresql.Driver
LOCAL_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
LOCAL_DB_HOST=localhost
LOCAL_DB_PORT=5432
LOCAL_DB_NAME=local_auth_service
LOCAL_DB_USER=local_user
LOCAL_DB_PASSWORD=LocalPass789

🔧 Интеграция с IntelliJ IDEA
Установите плагин .env files support через Marketplace

Настройте запуск:

Откройте Run/Debug Configurations

В секции "Environment variables" нажмите 🖍️

Выберите "Include parent environment variables"

Добавьте путь к вашему .env файлу

🚀 Запуск с использованием .env файла
Способ 1: Через IDE
В Run/Debug Configurations укажите:

VM Options: -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}

Запустите приложение - переменные автоматически подхватятся из .env

Способ 2: Через терминал (Linux/macOS)

export $(grep -v '^#' .env | xargs) && \
java -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
     -jar target/your-application.jar
     
Способ 3: Для Windows (PowerShell)

Get-Content .env | ForEach-Object {
  if ($_ -match "^\s*([^#]\S+)\s*=\s*(.*)") {
    [Environment]::SetEnvironmentVariable($matches[1], $matches[2])
  }
}

java -Dspring.profiles.active=$env:SPRING_PROFILES_ACTIVE `
     -jar target/your-application.jar
     
🔄 Динамическое переключение профилей
Измените значение в .env файле:

SPRING_PROFILES_ACTIVE=local

Перезапустите приложение - конфигурация автоматически обновится

🛡 Безопасность
Никогда не коммитьте .env файл в репозиторий

Используйте разные .env файлы для разных окружений:

.env.dev
.env.local
.env.prod

Для production используйте секретные хранилища:

AWS Secrets Manager

HashiCorp Vault

Kubernetes Secrets

💡 Советы по работе с .env
Используйте комментарии для группировки настроек

Поддерживайте одинаковую структуру для всех окружений

Для сложных значений используйте кавычки:

DB_PASSWORD="Complex!Pass#with$pecialChars"

Для многострочных значений используйте """:

SSL_CERT="""-----BEGIN CERTIFICATE-----
MIIDdzCCAl+gAwIBAgIEAgAAuTANBgkqh...
-----END CERTIFICATE-----"""

❗ Устранение неполадок
Если переменные не подгружаются:

Проверьте путь к .env файлу

Убедитесь, что в файле нет синтаксических ошибок

Проверьте права доступа к файлу

Для Windows: используйте CRLF вместо LF

Перезапустите IDE после изменения .env файла

Пример данных для подключения в профиле dev (кардинально изменены, включая смену СУБД на MySQL):     
#######################################
###     Настройки для dev профиля   ###
#######################################
# Тип JDBC подключения (изменено на MySQL)
DB_PROTOCOL=jdbc:mysql
# Драйвер базы данных (изменено на MySQL)
DB_DRIVER=com.mysql.cj.jdbc.Driver
# Диалект Hibernate (изменено на MySQL 8)
HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
# Хост базы данных (можно оставить localhost)
DB_HOST=localhost
# Порт базы данных (стандартный для MySQL)
DB_PORT=3306
# Имя базы данных (новое уникальное название)
DB_NAME=dev_application_db
# Пользователь БД (другой логин)
DB_USER=dev_user
# Пароль БД (сложный пароль)
DB_PASSWORD=Str0ngP@ss!2023

# Пример полного JDBC URL для MySQL:
# spring.datasource.url=${DB_PROTOCOL}://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&serverTimezone=UTC

