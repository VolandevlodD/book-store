package org.example.bookstore.config;

import org.testcontainers.containers.MySQLContainer;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String IMAGE_NAME = "mysql";

    private static CustomMySqlContainer container;

    private CustomMySqlContainer() {
        super(IMAGE_NAME);
    }

    public static synchronized CustomMySqlContainer getInstance() {
        if (container == null) {
            container = new CustomMySqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", getUsername());
        System.setProperty("TEST_DB_PASSWORD", getPassword());
    }
}
