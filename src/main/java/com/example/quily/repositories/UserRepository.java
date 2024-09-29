package com.example.quily.repositories;

import com.example.quily.model.User;
import com.example.quily.response.AuditLogResponse;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.quily.constants.ColumnNameConstants.*;
import static com.example.quily.constants.ShortLinkConstants.SqlQueryToGetAuditLogsProcedure;
import static com.example.quily.constants.UserConstants.*;

@Repository
public class UserRepository {
   private final DatabaseClient client;

   @Autowired
   public UserRepository(DatabaseClient client) {
      this.client = client;
   }

   public Mono<User> findUserByEmail(String email) {
      return client.sql(SqlQueryToCallGetUserProcedure)
         .bind(EMAIL, email)
         .fetch()
         .first()
         .map(CommonUtil::parseUser);
   }

   public Mono<User> createUser(User user) {
      return client.sql(SqlQueryToCallCreateUserProcedure)
         .bind(EMAIL, user.getEmail())
         .bind(USER_NAME, user.getUserName())
         .bind(PASSWORD, user.getPassword())
         .bind(IS_ACTIVE, user.isActive())
         .fetch()
         .rowsUpdated()
         .thenReturn(user);
   }

   public Mono<Long> resetPasswordByEmail(String email, String password) {
      return client.sql(SqlQueryToResetPassword)
         .bind(EMAIL, email)
         .bind(PASSWORD,password)
         .fetch()
         .rowsUpdated();
   }

   public Mono<Long> makeUserActive(String email) {
      return client.sql(SqlQueryToCallMakeUserActiveProcedure)
         .bind(EMAIL, email)
         .fetch()
         .rowsUpdated();
   }

   public Mono<Long> updateUsername(String email, String username) {
      return client.sql(SqlQueryToUpdateUsername)
         .bind(EMAIL, email)
         .bind(USER_NAME, username)
         .fetch()
         .rowsUpdated();
   }

   public Flux<AuditLogResponse> getAuditLog(int pageSize, int pageNumber, String dataEntity, String userEmail) {
      return binddataEntity(
              client.sql(SqlQueryToGetAuditLogsProcedure)
                      .bind(PAGE_NUMBER, pageNumber)
                      .bind(PAGE_SIZE, pageSize)
                      .bind(USER_EMAIL, userEmail), dataEntity)
              .fetch()
              .all()
              .map(CommonUtil::parseToAuditLogResponse);
   }

   private DatabaseClient.GenericExecuteSpec binddataEntity(DatabaseClient.GenericExecuteSpec query, String dataEntity) {
      if (dataEntity == null || dataEntity.isEmpty()) {
         return query.bindNull(DATA_ENTITY, String.class);
      } else {
         return query.bind(DATA_ENTITY, dataEntity);
      }
   }

}