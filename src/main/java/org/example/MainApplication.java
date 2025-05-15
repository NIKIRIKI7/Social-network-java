package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.config.DatabaseConfig;
import org.example.ui.MainWindow;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Инициализация базы данных
        DatabaseConfig.getDatabase();
        
        // Запуск главного окна
        MainWindow mainWindow = new MainWindow(primaryStage);
        mainWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 