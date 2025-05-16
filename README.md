# Социальная сеть

Простое приложение социальной сети с использованием Java, JavaFX, PostgreSQL и EBean ORM.

## Требования для запуска

1. Java 24 (или совместимая версия)
2. PostgreSQL 12+ 
3. Maven

## Настройка базы данных

1. Создайте базу данных в PostgreSQL:
```sql
CREATE DATABASE socialnetwork;
```

2. Настройте пользователя и пароль в файле `src/main/resources/ebean.properties`:
```
datasource.db.username=postgres
datasource.db.password=postgres
datasource.db.databaseUrl=jdbc:postgresql://localhost:5432/socialnetwork
```

## Запуск приложения

### Через Maven
```bash
mvn clean javafx:run
```

### Через IDE
Запустите класс `org.example.MainApplication` в вашей IDE.

## Функциональность

- Управление пользователями (создание, редактирование, удаление)
- Управление постами (создание, редактирование, удаление)
- Поиск пользователей по имени
- Поиск постов по содержимому
- Лайки постов

## Структура проекта

- `model` - Модели данных (User, Post)
- `repository` - Репозитории для доступа к данным
- `service` - Сервисы для бизнес-логики
- `ui` - Компоненты пользовательского интерфейса
- `config` - Конфигурация приложения

## База данных

- Таблица `users`: Хранит информацию о пользователях
- Таблица `posts`: Хранит посты, связанные с пользователями через внешний ключ

## Технологии

- Java 24
- JavaFX - GUI фреймворк
- EBean ORM - Объектно-реляционное отображение
- PostgreSQL - Реляционная СУБД (socialnetwork)