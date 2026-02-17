# StackoverKata - GateWay

Эндпоинты для тестирования в Postman:
Auth Service:
POST /api/auth/signup - Регистрация нового пользователя

Тело запроса (SignupRequestDto):

json
{
"email": "user@example.com",
"password": "password123",
"roleName": "USER",
"fullName": "Иван Иванов",
"city": "Москва"
}
POST /api/auth/login - Вход пользователя

Тело запроса (LoginRequestDto):

json
{
"email": "user@example.com",
"password": "password123"
}
POST /api/auth/refresh - Обновление токена

Тело запроса:

json
{
"token": "ваш_refresh_token"
}
GET /api/internal/account/{id} - Получение информации об аккаунте (внутренний)

Заголовок: Authorization: Bearer ваш_jwt_token

GET /api/internal/account/{id}/exists - Проверка существования аккаунта (внутренний)

Заголовок: Authorization: Bearer ваш_jwt_token

Profile Service:
GET /api/profile/{id} - Получение профиля пользователя

Заголовок: Authorization: Bearer ваш_jwt_token

Resource Service:
GET /api/resource/{id} - Получение ресурса

Заголовок: Authorization: Bearer ваш_jwt_token

3. Инструкция для тестирования в Postman:
   Сначала выполните регистрацию пользователя через /api/auth/signup

Затем войдите в систему через /api/auth/login и сохраните полученный токен

Для доступа к защищенным endpoint-ам добавьте заголовок:

Authorization: Bearer ваш_jwt_token
Для тестирования ролевой модели:

Создайте пользователя с ролью ADMIN

Попробуйте получить доступ к endpoint-ам, требующим разных ролей
