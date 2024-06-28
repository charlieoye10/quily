package com.example.quily.repositories;

import com.example.quily.model.User;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserRepository {
   private final DatabaseClient client;
   private static final String SqlQueryToCallGetUserProcedure = "CALL get_user_by_email(:email)";
   private static final String SqlQueryToCallCreateUserProcedure = "CALL create_user(:email, :user_name, :password, :is_active)";
   private static final String SqlQueryToCallMakeUserActiveProcedure = "CALL make_user_active(:email)";

   @Autowired
   public UserRepository(DatabaseClient client) {
      this.client = client;
   }

   public Mono<User> findUserByEmail(String email) {
      return client.sql(SqlQueryToCallGetUserProcedure)
         .bind("email", email)
         .fetch()
         .first()
         .map(CommonUtil::parseUser);
   }

   public Mono<User> createUser(User user) {
      return client.sql(SqlQueryToCallCreateUserProcedure)
         .bind("email", user.getEmail())
         .bind("user_name", user.getUserName())
         .bind("password", user.getPassword())
         .bind("is_active", user.isActive())
         .fetch()
         .rowsUpdated()
         .thenReturn(user);
   }

   public Mono<Long> makeUserActive(String email) {
      return client.sql(SqlQueryToCallMakeUserActiveProcedure)
         .bind("email", email)
         .fetch()
         .rowsUpdated();
   }
}