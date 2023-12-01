package com.slozic.orderservice.testconfig;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.RabbitMQContainer;

public class TestRabbitMQContainer extends RabbitMQContainer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String IMAGE_VERSION = "rabbitmq:3.11.25-management-alpine";

    public TestRabbitMQContainer() {
        super(IMAGE_VERSION);
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("AMPQ_URL", this.getAmqpsUrl());
        System.setProperty("AMPQ_USERNAME", this.getAdminUsername());
        System.setProperty("AMPQ_PASSWORD", this.getAdminPassword());
    }

    @Override
    public void stop() {
        // do nothing, JVM handles shut down
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        this.start();
        TestPropertyValues.of(
                        "spring.rabbitmq.host=" + this.getAmqpsUrl(),
                        "spring.rabbitmq.username=" + this.getAdminUsername(),
                        "spring.rabbitmq.password=" + this.getAdminPassword())
                .applyTo(applicationContext.getEnvironment());
    }
}
