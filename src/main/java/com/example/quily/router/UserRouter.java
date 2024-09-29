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
   public static final String RESET_PASSWORD_URL = "/api/auth/resetPassword";
   public static final String SEND_FORGET_PASSWORD_URL = "/api/auth/sendForgotPasswordVerificationEmail";
   public static final String FORGOT_PASSWORD_URL = "api/auth/forgotPassword";
   public static final String CHANGE_USERNAME_URL = "/api/auth/changeUsername";
   public static final String link = "/api/auth/linkPreviewDetail";
   private static final String GET_AUDIT_LOG_LIST = "/api/auditLog/getAuditLog";


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
         .andRoute(RequestPredicates.PUT(RESET_PASSWORD_URL), userHandler::resetPassword)
         .andRoute(RequestPredicates.POST(SEND_FORGET_PASSWORD_URL), userHandler::verifyEmailForForgotPassword)
         .andRoute(RequestPredicates.PUT(FORGOT_PASSWORD_URL), userHandler::forgotPassword)
         .andRoute(RequestPredicates.PUT(CHANGE_USERNAME_URL), userHandler::updateUsername)
         .andRoute(RequestPredicates.GET(link), userHandler::getLinkDetailFromLinkPreview)
         .andRoute(RequestPredicates.GET(GET_AUDIT_LOG_LIST), userHandler::getAuditLog);

   }
}
