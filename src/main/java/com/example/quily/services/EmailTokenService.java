package com.example.quily.services;

import com.example.quily.model.EmailConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class EmailTokenService implements DbService<EmailConfirmationToken, Long> {
   private final DatabaseClient dbClient;
   private static final String CONFIRMATION_TOKEN = "confirmation_token";
   private static final String USER_EMAIL = "user_email";
   private static final String CREATED_TIME = "created_time";

   @Autowired
   public EmailTokenService(DatabaseClient dbClient) {
      this.dbClient = dbClient;
   }

   @Override
   public Flux<EmailConfirmationToken> findAll() {
      return null;
   }

   @Override
   public Mono<EmailConfirmationToken> findByUniqueId(Long id) {
      return null;
   }

   @Override
   public Mono<EmailConfirmationToken> save(EmailConfirmationToken emailConfirmationToken) {
      return dbClient.sql("INSERT INTO email_confirmation_token (confirmation_token, created_time, user_email)" +
            " VALUES (:confirmation_token, :created_time, :user_email)" +
            " ON DUPLICATE KEY UPDATE confirmation_token = :confirmation_token, created_time = :created_time")
         .bind("confirmation_token", emailConfirmationToken.getConfirmationToken())
         .bind("created_time", emailConfirmationToken.getCreatedTime())
         .bind("user_email", emailConfirmationToken.getUserEmail())
         .fetch()
         .first().map(
            row -> emailConfirmationToken
         );
   }

   @Override
   public Mono<EmailConfirmationToken> update(EmailConfirmationToken emailConfirmationToken) {
      return null;
   }

   @Override
   public void delete(Long id) {
   }

   public Mono<EmailConfirmationToken> findByToken(String token) {
      return dbClient.sql("SELECT * FROM email_confirmation_token WHERE confirmation_token = :token")
         .bind("token", token)
         .map((row, metadata) -> new EmailConfirmationToken(
            row.get(CONFIRMATION_TOKEN, String.class),
            row.get(USER_EMAIL, String.class),
            row.get(CREATED_TIME, String.class)
         ))
         .one();
   }

   public Mono<Long> deleteByToken(String token) {
      return dbClient.sql("DELETE FROM email_confirmation_token WHERE confirmation_token = :token")
         .bind("token", token)
         .fetch()
         .rowsUpdated();
   }
}
