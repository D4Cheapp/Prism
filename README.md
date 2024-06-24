## Prism messenger app

### Функции приложения

- [ ]  Авторизация/регистрация/удаление пользователей
- [ ]  Обновление пароля/логина
- [ ]  Добавление/удаление чатов и групп
- [ ]  Добавление/изменение/удаление названия/иконки чата
- [ ]  Написание, пересылка, изменение, поиск и сохранение сообщений
- [ ]  Добавление/удаление пользователей (и себя) в группу
- [ ]  Администратор группы. Возможность его изменения
- [ ]  Система уведомлений
- [ ]  Меню вложений
- [ ]  Система друзья\контакты
- [ ]  Энвелоуп

### Запуск приложения

- Добавить файл переменных окружения .env
  в папку Backend
- cd Backend
- docker compose up --env-file .env
- Запустить файл index.html из папки Frontend

### Полезные ссылки

- [Grafana monitoring](http://localhost:3030)
- [Check server status](http://localhost:8080/prism/v1/actuator/health)
- [API endpoints](http://localhost:8080/prism/v1/swagger-ui/index.html)

### Backend технологии

- **Java Spring** - фреймворк для разработки
  приложений на языке Java, позволяющий создавать
  масштабируемые, модульные и тестируемые приложения.
- **Redis** - система кэширования и хранения данных
- **Postgres** - реляционная база данных.
- **Neo4j** - графическая база данных,
  использующая свойства графов для организации и
  управления данными.
- **Docker** - платформа для создания,
  развертывания и управления контейнерами.
- **Prometheus** - система мониторинга и
  алертинга.
- **Grafana** - платформа для визуализации данных мониторинга
- **Swagger/UI** - инструмент для создания,
  документирования и тестирования API

### Frontend технологии

- **HTML** - стандартный язык разметки для
  структурирования содержимого веб-страниц.
- **JS** - скриптовый язык программирования,
- используемый для создания динамических веб-страниц.
- **CSS** - язык описания стилей, используемый
  для задания внешнего вида веб-страниц.
