OtpService

Это простой сервис, который генерит и проверяет одноразовые коды (OTP) для разных задач.

Что тут есть

Регистрация и логин пользователей (JWT).

Роли: ADMIN и USER.

Генерация кода и его отправка по:

Email (через MailHog)

SMS (файл-эмулятор)

Telegram (мок-тест)

Сохранение в файл

Проверка кода.

Запуск за пару минут

Поднял докер:

docker-compose up -d

Запустил приложение:

./mvnw spring-boot:run

Всё. Сервер на http://localhost:8080, MailHog на http://localhost:8025.

Как юзать

Регистрация

curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"bob","password":"1234","role":"USER"}'

Логин

curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"bob","password":"1234"}'
# Получишь { "token": "..." }

Генерация кода

# email:
curl "http://localhost:8080/api/otp/generate?operationId=OP1&via=email&target=bob@example.com" \
  -H "Authorization: Bearer <TOKEN>"
# file (sms/emulator):
curl "http://localhost:8080/api/otp/generate?operationId=OP2&via=file&target=sms.log" \
  -H "Authorization: Bearer <TOKEN>"

Потом смотри MailHog или tail -f sms.log.

Проверка кода

curl "http://localhost:8080/api/otp/validate?operationId=OP1&code=123456" \
  -H "Authorization: Bearer <TOKEN>"

Тесты

./mvnw test

Прогонятся все юнит- и интеграционные тесты, включая проверку Telegram.

Ну вот, больше ничего не надо.

Если что скрины по работоспособности лежат в папке проекта.