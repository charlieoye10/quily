package com.example.quily.services;

import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.repositories.EmailTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmailTokenService implements DbService<EmailConfirmationToken, Long> {
   private final DatabaseClient dbClient;
   private final EmailTokenRepository emailTokenRepository;

   @Autowired
   public EmailTokenService(DatabaseClient dbClient, EmailTokenRepository emailTokenRepository) {
      this.dbClient = dbClient;
      this.emailTokenRepository = emailTokenRepository;
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
      return dbClient.sql("INSERT IGNORE INTO email_confirmation_token (confirmation_token, created_time, user_email)" +
            " VALUES (:confirmation_token, :created_time, :user_email)")
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
            row.get("confirmation_token", String.class),
            row.get("created_time", String.class),
            row.get("user_email", String.class)
         ))
         .one();
   }

   public Mono<Void> deleteByToken(String token) {
      return dbClient.sql("DELETE FROM email_confirmation_token WHERE confirmation_token = :token")
         .bind("token", token)
         .fetch()
         .first().map(Void.class::cast);
   }
}
