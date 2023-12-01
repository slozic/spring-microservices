package com.slozic.orderservice.testconfig;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TestPostgreSQLContainer.class, TestRabbitMQContainer.class})
public abstract class IntegrationTest {
}
