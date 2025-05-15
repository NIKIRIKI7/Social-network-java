package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    // Регистрация нового пользователя
    public User registerUser(String login, String email, String fullName) {
        // Проверка, что логин и email уникальны
        if (userRepository.findByLogin(login) != null) {
            throw new IllegalArgumentException("Логин уже занят");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email уже зарегистрирован");
        }

        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setFullName(fullName);
        
        userRepository.save(user);
        return user;
    }

    // Обновление данных пользователя
    public User updateUser(UUID id, String fullName, String bio, String avatarUrl) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        user.setFullName(fullName);
        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);
        
        userRepository.update(user);
        return user;
    }

    // Удаление пользователя
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        
        userRepository.delete(user);
    }

    // Деактивация пользователя
    public User deactivateUser(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        
        user.setActive(false);
        userRepository.update(user);
        return user;
    }

    // Получение пользователя по ID
    public User getUserById(UUID id) {
        return userRepository.findById(id);
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Поиск пользователей по имени или его части
    public List<User> searchUsersByName(String namePart) {
        return userRepository.findByName(namePart);
    }

    // Получение пользователя по логину
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
} 