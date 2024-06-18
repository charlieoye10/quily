package com.example.quily.Services;

import com.example.quily.Conf.TestConfig;
import com.example.quily.model.User;
import com.example.quily.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@Import({UserService.class, TestConfig.class})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        databaseClient.sql("DROP TABLE IF EXISTS users;").then().block();
        databaseClient.sql("CREATE TABLE users (email VARCHAR(255) PRIMARY KEY, user_name VARCHAR(255), password VARCHAR(255), is_active BOOLEAN);")
                .then()
                .block();
    }

    @Test
    void testMakeUserActive() {
        User user = new User("test@gmail.com", "test", "123565", false);
        databaseClient.sql("INSERT INTO users (email, user_name, password, is_active) VALUES (:email, :user_name, :password, :is_active)")
                .bind("email", user.getEmail())
                .bind("user_name", user.getUserName())
                .bind("password", user.getPassword())
                .bind("is_active", user.isActive())
                .fetch()
                .rowsUpdated()
                .block();

        Mono<Long> isActiveMono = userService.makeUserActive("test@gmail.com");

        StepVerifier.create(isActiveMono)
                .assertNext(isActive -> {
                    assertThat(isActive).isEqualTo(1);
                })
                .verifyComplete();
    }
}
