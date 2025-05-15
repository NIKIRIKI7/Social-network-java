package org.example.repository;

import io.ebean.Database;
import org.example.config.DatabaseConfig;
import org.example.model.User;

import java.util.List;
import java.util.UUID;

public class UserRepository {
    private final Database database;

    public UserRepository() {
        this.database = DatabaseConfig.getDatabase();
    }

    // Создание пользователя
    public void save(User user) {
        database.save(user);
    }

    // Обновление пользователя
    public void update(User user) {
        database.update(user);
    }

    // Удаление пользователя
    public void delete(User user) {
        database.delete(user);
    }

    // Получение пользователя по ID
    public User findById(UUID id) {
        return database.find(User.class).where().idEq(id).findOne();
    }

    // Получение всех пользователей
    public List<User> findAll() {
        return database.find(User.class).findList();
    }

    // Поиск пользователей по имени или его части
    public List<User> findByName(String namePart) {
        return database.find(User.class)
                .where()
                .ilike("fullName", "%" + namePart + "%")
                .findList();
    }

    // Поиск пользователя по логину
    public User findByLogin(String login) {
        return database.find(User.class)
                .where()
                .eq("login", login)
                .findOne();
    }

    // Поиск пользователя по email
    public User findByEmail(String email) {
        return database.find(User.class)
                .where()
                .eq("email", email)
                .findOne();
    }
} 