package org.example.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.model.Post;
import org.example.model.User;
import org.example.service.PostService;
import org.example.service.UserService;

import java.util.List;
import java.util.Optional;

public class MainWindow {
    private final Stage stage;
    private final UserService userService;
    private final PostService postService;
    
    private TableView<User> userTable;
    private TableView<Post> postTable;
    private TextField searchField;
    private User currentUser;

    public MainWindow(Stage stage) {
        this.stage = stage;
        this.userService = new UserService();
        this.postService = new PostService();
        
        initialize();
    }

    private void initialize() {
        stage.setTitle("Социальная сеть");
        
        // Создание панели вкладок
        TabPane tabPane = new TabPane();
        
        // Вкладка пользователей
        Tab usersTab = new Tab("Пользователи");
        usersTab.setClosable(false);
        usersTab.setContent(createUsersPanel());
        
        // Вкладка постов
        Tab postsTab = new Tab("Посты");
        postsTab.setClosable(false);
        postsTab.setContent(createPostsPanel());
        
        tabPane.getTabs().addAll(usersTab, postsTab);
        
        // Создание основной сцены
        Scene scene = new Scene(tabPane, 800, 600);
        stage.setScene(scene);
        
        // Загрузка данных
        refreshUsers();
        refreshPosts();
    }

    private VBox createUsersPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        
        // Панель поиска
        HBox searchPanel = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Поиск по имени...");
        searchField.setPrefWidth(300);
        
        Button searchButton = new Button("Поиск");
        searchButton.setOnAction(e -> searchUsers());
        
        searchPanel.getChildren().addAll(new Label("Поиск:"), searchField, searchButton);
        
        // Таблица пользователей
        userTable = new TableView<>();
        userTable.setPlaceholder(new Label("Нет данных"));
        
        TableColumn<User, String> loginColumn = new TableColumn<>("Логин");
        loginColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLogin()));
        
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        
        TableColumn<User, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        
        userTable.getColumns().addAll(loginColumn, emailColumn, nameColumn);
        userTable.setPrefHeight(400);
        
        // Панель кнопок
        HBox buttonPanel = new HBox(10);
        
        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> showUserDialog(null));
        
        Button editButton = new Button("Редактировать");
        editButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                showUserDialog(selectedUser);
            } else {
                showAlert("Выберите пользователя для редактирования", Alert.AlertType.WARNING);
            }
        });
        
        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                deleteUser(selectedUser);
            } else {
                showAlert("Выберите пользователя для удаления", Alert.AlertType.WARNING);
            }
        });
        
        Button refreshButton = new Button("Обновить");
        refreshButton.setOnAction(e -> refreshUsers());
        
        buttonPanel.getChildren().addAll(addButton, editButton, deleteButton, refreshButton);
        
        // Добавление всех компонентов в панель
        panel.getChildren().addAll(searchPanel, userTable, buttonPanel);
        VBox.setVgrow(userTable, Priority.ALWAYS);
        
        return panel;
    }

    private VBox createPostsPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        
        // Панель поиска
        HBox searchPanel = new HBox(10);
        TextField postSearchField = new TextField();
        postSearchField.setPromptText("Поиск по содержимому...");
        postSearchField.setPrefWidth(300);
        
        Button searchButton = new Button("Поиск");
        searchButton.setOnAction(e -> searchPosts(postSearchField.getText()));
        
        searchPanel.getChildren().addAll(new Label("Поиск:"), postSearchField, searchButton);
        
        // Таблица постов
        postTable = new TableView<>();
        postTable.setPlaceholder(new Label("Нет данных"));
        
        TableColumn<Post, String> authorColumn = new TableColumn<>("Автор");
        authorColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getAuthor() != null ? data.getValue().getAuthor().getLogin() : ""));
        
        TableColumn<Post, String> contentColumn = new TableColumn<>("Содержимое");
        contentColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getContent()));
        contentColumn.setPrefWidth(300);
        
        TableColumn<Post, String> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCreationDate().toString()));
        
        TableColumn<Post, String> likesColumn = new TableColumn<>("Лайки");
        likesColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getLikesCount())));
        
        postTable.getColumns().addAll(authorColumn, contentColumn, dateColumn, likesColumn);
        postTable.setPrefHeight(400);
        
        // Панель кнопок
        HBox buttonPanel = new HBox(10);
        
        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> showPostDialog(null));
        
        Button editButton = new Button("Редактировать");
        editButton.setOnAction(e -> {
            Post selectedPost = postTable.getSelectionModel().getSelectedItem();
            if (selectedPost != null) {
                showPostDialog(selectedPost);
            } else {
                showAlert("Выберите пост для редактирования", Alert.AlertType.WARNING);
            }
        });
        
        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> {
            Post selectedPost = postTable.getSelectionModel().getSelectedItem();
            if (selectedPost != null) {
                deletePost(selectedPost);
            } else {
                showAlert("Выберите пост для удаления", Alert.AlertType.WARNING);
            }
        });
        
        Button likeButton = new Button("Лайк");
        likeButton.setOnAction(e -> {
            Post selectedPost = postTable.getSelectionModel().getSelectedItem();
            if (selectedPost != null) {
                likePost(selectedPost);
            } else {
                showAlert("Выберите пост для лайка", Alert.AlertType.WARNING);
            }
        });
        
        Button refreshButton = new Button("Обновить");
        refreshButton.setOnAction(e -> refreshPosts());
        
        buttonPanel.getChildren().addAll(addButton, editButton, deleteButton, likeButton, refreshButton);
        
        // Добавление всех компонентов в панель
        panel.getChildren().addAll(searchPanel, postTable, buttonPanel);
        VBox.setVgrow(postTable, Priority.ALWAYS);
        
        return panel;
    }

    private void showUserDialog(User user) {
        UserDialog dialog = new UserDialog(stage, user, userService);
        dialog.showAndWait().ifPresent(updatedUser -> refreshUsers());
    }

    private void showPostDialog(Post post) {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            showAlert("Сначала создайте пользователя", Alert.AlertType.WARNING);
            return;
        }
        
        PostDialog dialog = new PostDialog(stage, post, postService, users);
        dialog.showAndWait().ifPresent(updatedPost -> refreshPosts());
    }

    private void deleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Удаление пользователя");
        alert.setContentText("Вы уверены, что хотите удалить пользователя " + user.getLogin() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userService.deleteUser(user.getId());
                refreshUsers();
                showAlert("Пользователь успешно удален", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Ошибка при удалении пользователя: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void deletePost(Post post) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Удаление поста");
        alert.setContentText("Вы уверены, что хотите удалить этот пост?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                postService.deletePost(post.getId());
                refreshPosts();
                showAlert("Пост успешно удален", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Ошибка при удалении поста: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void likePost(Post post) {
        try {
            postService.likePost(post.getId());
            refreshPosts();
        } catch (Exception e) {
            showAlert("Ошибка при лайке поста: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void refreshUsers() {
        Platform.runLater(() -> {
            try {
                List<User> users = userService.getAllUsers();
                userTable.setItems(FXCollections.observableArrayList(users));
            } catch (Exception e) {
                showAlert("Ошибка при загрузке пользователей: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void refreshPosts() {
        Platform.runLater(() -> {
            try {
                List<Post> posts = postService.getAllPosts();
                postTable.setItems(FXCollections.observableArrayList(posts));
            } catch (Exception e) {
                showAlert("Ошибка при загрузке постов: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void searchUsers() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            refreshUsers();
        } else {
            try {
                List<User> users = userService.searchUsersByName(searchText);
                userTable.setItems(FXCollections.observableArrayList(users));
            } catch (Exception e) {
                showAlert("Ошибка при поиске: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void searchPosts(String searchText) {
        if (searchText.trim().isEmpty()) {
            refreshPosts();
        } else {
            try {
                List<Post> posts = postService.searchPostsByContent(searchText);
                postTable.setItems(FXCollections.observableArrayList(posts));
            } catch (Exception e) {
                showAlert("Ошибка при поиске: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show() {
        stage.show();
    }
} 