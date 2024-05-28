package com.example.quily.services;

import com.example.quily.exception.AlreadyExistEntityException;
import com.example.quily.exception.ErrorResponse;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.User;
import com.example.quily.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Service
public class UserService implements DbService<User, String>{
    @Autowired
    private UserRepository userRepository;

    private final DatabaseClient databaseClient;

    public UserService(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<User> findAll() {
        return null;
    }

    @Override
    public Mono<User> findByUniqueId(String email) {
        return userRepository.findById(email);
    }

    @Override
    public Mono<User> save(User user) throws RuntimeException {
        return databaseClient.sql("INSERT INTO users (email, user_name, password) VALUES (:email, :name, :password)")
                .bind("email", user.getEmail())
                .bind("name", user.getUserName())
                .bind("password", user.getPassword())
                .fetch()
                .rowsUpdated()
                .thenReturn(user);
    }

    @Override
    public Mono<User> update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void Delete(String email) {
        userRepository.deleteById(email);
    }
}
