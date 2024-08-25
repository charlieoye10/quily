package com.example.quily.repositories;

import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static com.example.quily.constants.ColumnNameConstants.*;
import static com.example.quily.constants.EmailVerificationConstants.*;

@Repository
public class EmailVerificationRepository {
   private final DatabaseClient client;

   @Autowired
   public EmailVerificationRepository(DatabaseClient client) {
      this.client = client;
   }

   public Mono<EmailConfirmationToken> findByToken(String token) {
      return client.sql(SqlQueryToCallGetTokenProcedure)
         .bind(CONFIRMATION_TOKEN, token)
         .fetch()
         .first()
         .map(CommonUtil::parseEmailConfirmationToken);
   }

   public Mono<EmailConfirmationToken> saveToken(EmailConfirmationToken emailConfirmationToken) {
      return client.sql(SqlQueryToSaveTokenProcedure)
         .bind(CONFIRMATION_TOKEN, emailConfirmationToken.getConfirmationToken())
         .bind(EMAIL, emailConfirmationToken.getUserEmail())
         .fetch()
         .first().map(row -> emailConfirmationToken);
   }

   public Mono<Long> deleteByToken(String token) {
      return client.sql(SqlQueryToDeleteTokenProcedure)
         .bind(CONFIRMATION_TOKEN, token)
         .fetch()
         .rowsUpdated();
   }
}
