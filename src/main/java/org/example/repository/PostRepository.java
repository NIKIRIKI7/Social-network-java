package org.example.repository;

import io.ebean.Database;
import org.example.config.DatabaseConfig;
import org.example.model.Post;
import org.example.model.User;

import java.util.List;
import java.util.UUID;

public class PostRepository {
    private final Database database;

    public PostRepository() {
        this.database = DatabaseConfig.getDatabase();
    }

    // Создание поста
    public void save(Post post) {
        database.save(post);
    }

    // Обновление поста
    public void update(Post post) {
        database.update(post);
    }

    // Удаление поста
    public void delete(Post post) {
        database.delete(post);
    }

    // Получение поста по ID
    public Post findById(UUID id) {
        return database.find(Post.class).where().idEq(id).findOne();
    }

    // Получение всех постов
    public List<Post> findAll() {
        return database.find(Post.class)
                .setOrderBy("creationDate desc")
                .findList();
    }

    // Получение постов пользователя
    public List<Post> findByAuthor(User author) {
        return database.find(Post.class)
                .where()
                .eq("author", author)
                .orderBy("creationDate desc")
                .findList();
    }

    // Поиск постов по содержимому
    public List<Post> findByContent(String contentPart) {
        return database.find(Post.class)
                .where()
                .ilike("content", "%" + contentPart + "%")
                .orderBy("creationDate desc")
                .findList();
    }

    // Получение последних постов
    public List<Post> findLatest(int limit) {
        return database.find(Post.class)
                .setMaxRows(limit)
                .orderBy("creationDate desc")
                .findList();
    }
} 