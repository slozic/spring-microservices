package com.slozic.inventoryservice;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgreSQLContainer extends PostgreSQLContainer<TestPostgreSQLContainer>
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String IMAGE_VERSION = PostgreSQLContainer.IMAGE + ":11.2";

    public TestPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", this.getJdbcUrl());
        System.setProperty("DB_USERNAME", this.getUsername());
        System.setProperty("DB_PASSWORD", this.getPassword());
    }

    @Override
    public void stop() {
        // do nothing, JVM handles shut down
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        this.start();
        TestPropertyValues.of(
                "spring.datasource.url=" + this.getJdbcUrl(),
                "spring.datasource.username=" + this.getUsername(),
                "spring.datasource.password=" + this.getPassword())
                .applyTo(applicationContext.getEnvironment());
    }
}
