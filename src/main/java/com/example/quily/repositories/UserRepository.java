package com.example.quily.repositories;

import com.example.quily.model.KeyIndices;
import com.example.quily.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<User, String> {
    @Query("SELECT email, user_name, password FROM users WHERE email = :email")
    Mono<User> findByEmail(@Param("email") String email);
}