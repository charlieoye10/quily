package com.example.quily.repositories;

import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class EmailTokenRepository {
   private final DatabaseClient client;
   private final String SqlQueryToCallGetTokenProcedure = "CALL get_email_confirmation_token(:token)";
   private final String SqlQueryToSaveTokenProcedure =
      "CALL save_email_confirmation_token(:confirmation_token, :created_time, :user_email)";
   private final String SqlQueryToDeleteTokenProcedure = "CALL delete_email_verification_token(:token)";

   @Autowired
   public EmailTokenRepository(DatabaseClient client) {
      this.client = client;
   }

   public Mono<EmailConfirmationToken> findByToken(String token) {
      return client.sql(SqlQueryToCallGetTokenProcedure)
         .bind("token", token)
         .fetch()
         .first()
         .map(CommonUtil::parseEmailConfirmationToken);
   }

   public Mono<EmailConfirmationToken> saveToken(EmailConfirmationToken emailConfirmationToken) {
      return client.sql(SqlQueryToSaveTokenProcedure)
         .bind("confirmation_token", emailConfirmationToken.getConfirmationToken())
         .bind("created_time", emailConfirmationToken.getCreatedTime())
         .bind("user_email", emailConfirmationToken.getUserEmail())
         .fetch()
         .first().map(row -> emailConfirmationToken);
   }

   public Mono<Long> deleteByToken(String token) {
      return client.sql(SqlQueryToDeleteTokenProcedure)
         .bind("token", token)
         .fetch()
         .rowsUpdated();
   }
}
