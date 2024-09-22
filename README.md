# Prism messenger app

## Функции приложения

- [x] Авторизация и регистрация
- [x] Регистрация с подтверждением почты
- [x] Изменение данных аккаунта
- [x] Личный профиль
- [ ] Группы пользователей
- [ ] Чат между двумя пользователями
- [ ] Система отношений между пользователями
  - [ ] Добавление в друзья
  - [ ] Удаление из друзей
  - [ ] Блокировка

## Запуск приложения

- Добавить файлы переменных окружения backend.env и frontend.env
- docker network create prism-network
- docker compose -f ./Backend/docker-compose.yml --env-file backend.env up
- docker compose -f ./Backend/Authentication/docker-compose.yml --env-file backend.env up
- docker compose -f ./Backend/Messenger/docker-compose.yml --env-file backend.env up
- Запустить файл index.html из папки Frontend

## Полезные ссылки

- [Grafana monitoring](http://localhost:3030)
- [API endpoints](http://localhost:2222)

## Backend технологии

- **Java Spring** - фреймворк для разработки
  приложений на языке Java, позволяющий создавать
  масштабируемые, модульные и тестируемые приложения.
- **Redis** - система кэширования и хранения данных
- **Postgres** - реляционная база данных.
- **Neo4j** - графическая база данных,
  использующая свойства графов для организации и
  управления данными.
- **Minio** - S3 база данных для хранения файлов пользователей
- **MongoDB** - NoSQL база данных, для хранения документоориентированных данных.
- **RabbitMQ** - система обмена сообщениями, которая
  используется для организации коммуникации и координации сервисов.
- **Docker** - платформа для создания,
  развертывания и управления контейнерами.
- **Prometheus** - система мониторинга и
  уведомлений.
- **Grafana** - платформа для визуализации данных мониторинга
- **Swagger/UI** - инструмент для создания,
  документирования и тестирования API

## Frontend технологии

- **NextJs** - фреймворк для создания server-side rendered (SSR) приложений на React.
- **SCSS** - препроцессор CSS, который позволяет использовать variables, functions, mixins, imports и другие функции для упрощения написания CSS.
- **Typescript** - статически типизированный язык программирования, который компилируется в JavaScript.
- **Redux** - библиотека для управления состоянием в приложении, которая позволяет центализовать логику приложения и упрощает работу с состоянием.
