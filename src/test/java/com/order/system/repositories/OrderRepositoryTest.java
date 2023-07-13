package com.order.system.repositories;

import com.order.system.enums.OrderStatus;
import com.order.system.repositories.entities.OrderEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class OrderRepositoryTest {
    @Container
    public static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:5.6")
            .withPrivilegedMode(true)
            .withDatabaseName("orderSystem")
            .withEnv("MYSQL_USER", "root")
            .withEnv("MYSQL_PASSWORD", "password")
            .withEnv("MYSQL_RANDOM_ROOT_PASSWORD", "yes")
            .withInitScript("./initialize.sql");

    @DynamicPropertySource
    public static void dbProperties(DynamicPropertyRegistry registry) {
        mySQLContainer.start();
        registry.add("spring.datasource.url", () -> "jdbc:mysql://" + mySQLContainer.getHost() + ":" + mySQLContainer.getFirstMappedPort() + "/" + mySQLContainer.getDatabaseName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQLDialect");
    }

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void initali_table() {
        for (int i = 0; i < 100; i++) {
            OrderEntity orderEntity = OrderEntity.builder()
                    .originLatitude(String.valueOf(i))
                    .originLongitude(String.valueOf(i))
                    .destinationLatitude(String.valueOf(i))
                    .destinationLongitude(String.valueOf(i))
                    .orderStatus(OrderStatus.UNASSIGNED.name())
                    .totalDistance(i)
                    .build();
            orderRepository.save(orderEntity);
        }
    }

    @AfterEach
    public void clean_data() {
        orderRepository.deleteAll();
    }

    @ParameterizedTest
    @CsvSource({
        "40, 0, 40",
        "40, 40, 40",
        "40, 80, 20"
    })
    public void getOrdersOrderedByCreateDateTest(Integer limit, Integer offset, Integer expectedSize) {
        assertEquals(expectedSize, orderRepository.getOrdersOrderedByCreateDate(limit, offset).size());
    }
}

