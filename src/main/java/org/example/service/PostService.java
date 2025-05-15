package org.example.service;

import org.example.model.Post;
import org.example.model.User;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;

import java.util.List;
import java.util.UUID;

public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService() {
        this.postRepository = new PostRepository();
        this.userRepository = new UserRepository();
    }

    // Создание нового поста
    public Post createPost(UUID authorId, String content, String attachmentUrl, boolean isPublic) {
        User author = userRepository.findById(authorId);
        if (author == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(content);
        post.setAttachmentUrl(attachmentUrl);
        post.setPublic(isPublic);
        
        postRepository.save(post);
        return post;
    }

    // Обновление поста
    public Post updatePost(UUID postId, String content, String attachmentUrl, boolean isPublic) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Пост не найден");
        }

        post.setContent(content);
        post.setAttachmentUrl(attachmentUrl);
        post.setPublic(isPublic);
        
        postRepository.update(post);
        return post;
    }

    // Удаление поста
    public void deletePost(UUID postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Пост не найден");
        }
        
        postRepository.delete(post);
    }

    // Лайк поста
    public Post likePost(UUID postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Пост не найден");
        }
        
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.update(post);
        return post;
    }

    // Получение поста по ID
    public Post getPostById(UUID postId) {
        return postRepository.findById(postId);
    }

    // Получение всех постов
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Получение постов по автору
    public List<Post> getPostsByAuthor(UUID authorId) {
        User author = userRepository.findById(authorId);
        if (author == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        
        return postRepository.findByAuthor(author);
    }

    // Поиск постов по содержимому
    public List<Post> searchPostsByContent(String contentPart) {
        return postRepository.findByContent(contentPart);
    }

    // Получение последних постов
    public List<Post> getLatestPosts(int limit) {
        return postRepository.findLatest(limit);
    }
} 