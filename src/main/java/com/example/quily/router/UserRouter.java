package com.example.quily.router;

import com.example.quily.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {
   private final UserHandler userHandler;
   private static final String SIGNUP_URL = "/api/auth/signup";
   private static final String CONFIRM_ACCOUNT_URL = "/api/auth/confirm-account";
   private static final String LOGIN_URL = "/api/auth/login";
   private static final String RESET_PASSWORD = "/api/auth/resetPassword";
   private static final String SEND_FORGET_PASSWORD = "/api/auth/sendForgotPasswordVerifyEmail";
   private static final String FORGOT_PASSWORD = "api/auth/forgotPassword";

   @Autowired
   public UserRouter(UserHandler userHandler) {
      this.userHandler = userHandler;
   }

   @Bean
   public RouterFunction<ServerResponse> userRoutes() {
      return RouterFunctions
         .route(RequestPredicates.POST(SIGNUP_URL), userHandler::signUp)
         .andRoute(RequestPredicates.GET(CONFIRM_ACCOUNT_URL), userHandler::verifyUser)
         .andRoute(RequestPredicates.POST(LOGIN_URL), userHandler::login)
         .andRoute(RequestPredicates.POST(RESET_PASSWORD), userHandler::resetPassword)
         .andRoute(RequestPredicates.POST(SEND_FORGET_PASSWORD), userHandler::verifyEmailForForgotPassword)
         .andRoute(RequestPredicates.POST(FORGOT_PASSWORD), userHandler::forgotPassword);

   }
}
