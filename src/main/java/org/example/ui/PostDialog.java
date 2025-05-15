package org.example.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.Post;
import org.example.model.User;
import org.example.service.PostService;

import java.util.List;
import java.util.Optional;

public class PostDialog {
    private final Dialog<Post> dialog;
    private final Post post;
    private final PostService postService;
    private final List<User> availableUsers;
    private final boolean isNewPost;

    private ComboBox<User> authorComboBox;
    private TextArea contentArea;
    private TextField attachmentField;
    private CheckBox publicCheckBox;

    public PostDialog(Stage owner, Post post, PostService postService, List<User> availableUsers) {
        this.postService = postService;
        this.availableUsers = availableUsers;
        this.isNewPost = post == null;
        this.post = isNewPost ? new Post() : post;

        dialog = new Dialog<>();
        dialog.setTitle(isNewPost ? "Создание поста" : "Редактирование поста");
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        // Настройка кнопок
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Создание формы
        dialog.getDialogPane().setContent(createForm());
        
        // Обработка результата
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return savePost();
            }
            return null;
        });
    }

    private GridPane createForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Поля формы
        authorComboBox = new ComboBox<>();
        authorComboBox.getItems().addAll(availableUsers);
        if (post.getAuthor() != null) {
            for (User user : availableUsers) {
                if (user.getId().equals(post.getAuthor().getId())) {
                    authorComboBox.setValue(user);
                    break;
                }
            }
        } else if (!availableUsers.isEmpty()) {
            authorComboBox.setValue(availableUsers.get(0));
        }
        authorComboBox.setDisable(!isNewPost); // Запрещаем менять автора для существующего поста

        contentArea = new TextArea();
        contentArea.setPromptText("Содержимое поста");
        contentArea.setText(post.getContent());
        contentArea.setPrefRowCount(5);
        contentArea.setWrapText(true);

        attachmentField = new TextField();
        attachmentField.setPromptText("URL вложения");
        attachmentField.setText(post.getAttachmentUrl());

        publicCheckBox = new CheckBox("Публичный пост");
        publicCheckBox.setSelected(post.isPublic());

        // Добавление полей в сетку
        grid.add(new Label("Автор:"), 0, 0);
        grid.add(authorComboBox, 1, 0);
        grid.add(new Label("Содержимое:"), 0, 1);
        grid.add(contentArea, 1, 1);
        grid.add(new Label("URL вложения:"), 0, 2);
        grid.add(attachmentField, 1, 2);
        grid.add(publicCheckBox, 1, 3);

        return grid;
    }

    private Post savePost() {
        try {
            // Проверка обязательных полей
            if (authorComboBox.getValue() == null || contentArea.getText().trim().isEmpty()) {
                showAlert("Автор и содержимое обязательны для заполнения", Alert.AlertType.ERROR);
                return null;
            }

            if (isNewPost) {
                // Создание нового поста
                return postService.createPost(
                        authorComboBox.getValue().getId(),
                        contentArea.getText().trim(),
                        attachmentField.getText().trim(),
                        publicCheckBox.isSelected()
                );
            } else {
                // Обновление существующего поста
                return postService.updatePost(
                        post.getId(),
                        contentArea.getText().trim(),
                        attachmentField.getText().trim(),
                        publicCheckBox.isSelected()
                );
            }
        } catch (Exception e) {
            showAlert("Ошибка при сохранении: " + e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Optional<Post> showAndWait() {
        return dialog.showAndWait();
    }
} 