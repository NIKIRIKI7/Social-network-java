package org.example.config;

import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import org.example.model.Post;
import org.example.model.User;

import java.util.Properties;

public class DatabaseConfig {
    private static Database database;

    public static Database getDatabase() {
        if (database == null) {
            initializeDatabase();
        }
        return database;
    }

    private static void initializeDatabase() {
        DatabaseConfig config = new DatabaseConfig();
        config.setDefaultServer(true);
        
        // Регистрация классов-сущностей
        config.addClass(User.class);
        config.addClass(Post.class);

        // Настройка подключения к базе данных
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername("postgres");
        dataSourceConfig.setPassword("postgres");
        dataSourceConfig.setUrl("jdbc:postgresql://localhost:5432/socialnetwork");
        dataSourceConfig.setDriver("org.postgresql.Driver");
        
        config.setDataSourceConfig(dataSourceConfig);
        
        // Генерация и запуск DDL
        config.setDdlGenerate(true);
        config.setDdlRun(true);
        
        database = DatabaseFactory.create(config);
    }
} 