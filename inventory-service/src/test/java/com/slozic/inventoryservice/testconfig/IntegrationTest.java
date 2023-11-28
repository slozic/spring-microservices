package com.slozic.inventoryservice.testconfig;

import com.slozic.inventoryservice.TestPostgreSQLContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TestPostgreSQLContainer.class})
public abstract class IntegrationTest {
}
