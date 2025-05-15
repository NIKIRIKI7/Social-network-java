package org.example.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.User;
import org.example.service.UserService;

import java.util.Optional;

public class UserDialog {
    private final Dialog<User> dialog;
    private final User user;
    private final UserService userService;
    private final boolean isNewUser;

    private TextField loginField;
    private TextField emailField;
    private TextField nameField;
    private TextArea bioArea;
    private TextField avatarField;

    public UserDialog(Stage owner, User user, UserService userService) {
        this.userService = userService;
        this.isNewUser = user == null;
        this.user = isNewUser ? new User() : user;

        dialog = new Dialog<>();
        dialog.setTitle(isNewUser ? "Создание пользователя" : "Редактирование пользователя");
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
                return saveUser();
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
        loginField = new TextField();
        loginField.setPromptText("Логин");
        loginField.setText(user.getLogin());
        loginField.setDisable(!isNewUser); // Запрещаем менять логин для существующего пользователя
        
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setText(user.getEmail());
        emailField.setDisable(!isNewUser); // Запрещаем менять email для существующего пользователя

        nameField = new TextField();
        nameField.setPromptText("Полное имя");
        nameField.setText(user.getFullName());

        bioArea = new TextArea();
        bioArea.setPromptText("О себе");
        bioArea.setText(user.getBio());
        bioArea.setPrefRowCount(3);

        avatarField = new TextField();
        avatarField.setPromptText("URL аватара");
        avatarField.setText(user.getAvatarUrl());

        // Добавление полей в сетку
        grid.add(new Label("Логин:"), 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Полное имя:"), 0, 2);
        grid.add(nameField, 1, 2);
        grid.add(new Label("О себе:"), 0, 3);
        grid.add(bioArea, 1, 3);
        grid.add(new Label("URL аватара:"), 0, 4);
        grid.add(avatarField, 1, 4);

        return grid;
    }

    private User saveUser() {
        try {
            // Проверка обязательных полей
            if (loginField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                showAlert("Логин и Email обязательны для заполнения", Alert.AlertType.ERROR);
                return null;
            }

            if (isNewUser) {
                // Создание нового пользователя
                return userService.registerUser(
                        loginField.getText().trim(),
                        emailField.getText().trim(),
                        nameField.getText().trim()
                );
            } else {
                // Обновление существующего пользователя
                return userService.updateUser(
                        user.getId(),
                        nameField.getText().trim(),
                        bioArea.getText().trim(),
                        avatarField.getText().trim()
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

    public Optional<User> showAndWait() {
        return dialog.showAndWait();
    }
} 