package com.example.quily.handler;

import com.example.quily.converter.UserSignupConverter;
import com.example.quily.dao.UserDao;
import com.example.quily.exception.ErrorResponse;
import com.example.quily.request.SignUpRequest;
import com.example.quily.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class UserHandler {
   private final UserService userService;
   private final UserDao userDao;
   private final UserSignupConverter userSignupConverter;

	@Autowired
   public UserHandler(UserService userService, UserDao userDao, UserSignupConverter userSignupConverter) {
      this.userService = userService;
      this.userDao = userDao;
      this.userSignupConverter = userSignupConverter;
   }

   public Mono<ServerResponse> signUp(ServerRequest request) {
      return request.bodyToMono(SignUpRequest.class)
         .flatMap(req -> {
            if (userDao.isEmailPatternCorrect(req.email))
               return userService.sendEmailVerificationLink(userSignupConverter.convertRequestToModel(req));
            else
               return Mono.error(new Exception("Email format is not considerable, Please provide email with correct format eg. `user_name@(gmail or any company_name).com`"));
         })
         .flatMap(resString -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(resString))
         .onErrorResume(e ->
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(new ErrorResponse("Internal Server Error with error message: ", e.getMessage())));
   }

   public Mono<ServerResponse> verifyUser(ServerRequest serverRequest) {
      final Optional<String> tokenMono = serverRequest.queryParam("token");
      return tokenMono.map(token -> userService.verifyTokenAndSaveUser(token)
         .flatMap(resString -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(resString))
         .onErrorResume(e ->
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(new ErrorResponse("Internal Server Error with error message: ", e.getMessage()) {
               }))).orElseGet(() -> ServerResponse.status(HttpStatus.BAD_REQUEST)
         .contentType(MediaType.APPLICATION_JSON)
         .bodyValue(new ErrorResponse("confirmation token was empty in verification link",
            "please check your verification link or request for new verification link")));

   }
}
