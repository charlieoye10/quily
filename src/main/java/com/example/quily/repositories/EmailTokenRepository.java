package com.example.quily.repositories;

import com.example.quily.model.EmailConfirmationToken;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTokenRepository extends R2dbcRepository<EmailConfirmationToken, Long> {}
