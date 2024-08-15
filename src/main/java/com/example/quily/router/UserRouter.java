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
   public static final String SIGNUP_URL = "/api/auth/signup";
   public static final String CONFIRM_ACCOUNT_URL = "/api/auth/confirm-account";
   public static final String LOGIN_URL = "/api/auth/login";
   private static final String RESET_PASSWORD_URL = "/api/auth/resetPassword";
   public static final String SEND_FORGET_PASSWORD_URL = "/api/auth/sendForgotPasswordVerificationEmail";
   public static final String FORGOT_PASSWORD_URL = "api/auth/forgotPassword";

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
         .andRoute(RequestPredicates.POST(RESET_PASSWORD_URL), userHandler::resetPassword)
         .andRoute(RequestPredicates.POST(SEND_FORGET_PASSWORD_URL), userHandler::verifyEmailForForgotPassword)
         .andRoute(RequestPredicates.POST(FORGOT_PASSWORD_URL), userHandler::forgotPassword);

   }
}
