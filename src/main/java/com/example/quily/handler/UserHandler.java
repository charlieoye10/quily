package com.example.quily.handler;

import com.example.quily.metadata.LoginResponseData;
import com.example.quily.constants.UserConstants;
import com.example.quily.converter.UserSignupConverter;
import com.example.quily.dao.UserDao;
import com.example.quily.exception.BadRequestException;
import com.example.quily.model.User;
import com.example.quily.request.*;
import com.example.quily.response.LoginResponse;
import com.example.quily.response.ResponseBody;
import com.example.quily.security.JwtUtil;
import com.example.quily.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.quily.constants.UserConstants.LOGIN_FAILED_MESSAGE;

@Component
public class UserHandler {
   private final UserService userService;
   private final UserDao userDao;
   private final UserSignupConverter userSignupConverter;
   private final JwtUtil jwtUtil;

	@Autowired
   public UserHandler(UserService userService, UserDao userDao, UserSignupConverter userSignupConverter, JwtUtil jwtUtil) {
      this.userService = userService;
      this.userDao = userDao;
      this.userSignupConverter = userSignupConverter;
      this.jwtUtil = jwtUtil;
   }

   public Mono<ServerResponse> signUp(ServerRequest request) {
      return request.bodyToMono(SignUpRequest.class)
         .flatMap(userDao::processEmailVerification)
         .flatMap(resString -> {
            ResponseBody<String> responseBody =
               new ResponseBody<>(HttpStatus.OK.value(), resString);
            return ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(responseBody);
         });
   }

   public Mono<ServerResponse> verifyUser(ServerRequest serverRequest) {
      final String errorMessage = UserConstants.VERIFICATION_FAILED_MESSAGE;
      final Optional<String> tokenOpt = serverRequest.queryParam("token");
      return tokenOpt.map(token -> userService.verifyTokenAndSaveUser(token)
            .flatMap(signUpResponse -> ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(
                  userSignupConverter.getResponseFromModel(signUpResponse)
               )))
         .orElseGet(() -> Mono.error(new BadRequestException(errorMessage)));

   }

   public Mono<ServerResponse> login(ServerRequest serverRequest) {
      return serverRequest.bodyToMono(LoginRequest.class)
         .flatMap(loginReq -> userService
            .getUserAndCheckCredentials(loginReq.getEmail(), loginReq.getPassword()))
         .flatMap(this::handleSuccessLogin)
         .switchIfEmpty(handleFailedLogin());
   }

   private Mono<ServerResponse> handleFailedLogin() {
      return ServerResponse.status(HttpStatus.UNAUTHORIZED)
         .contentType(MediaType.APPLICATION_JSON)
         .bodyValue(new ResponseBody<>(HttpStatus.UNAUTHORIZED.value(), LOGIN_FAILED_MESSAGE, null));
   }

   private Mono<ServerResponse> handleSuccessLogin(User user) {
      LoginResponseData loginResponseData = new LoginResponseData(user.getUserName(),user.getEmail());
      return ServerResponse.ok()
         .contentType(MediaType.APPLICATION_JSON)
         .bodyValue(
            new ResponseBody<>(
               HttpStatus.OK.value(),
                    new LoginResponse(jwtUtil.generateToken(user.getEmail()), loginResponseData)
            )
         );
   }

   public Mono<ServerResponse> resetPassword(ServerRequest req) {
      return req.bodyToMono(ResetPasswordRequest.class)
         .flatMap(resetPasswordRequest ->
            userService.resetPassword(resetPasswordRequest)
               .flatMap(message ->
                  ServerResponse.ok()
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(new ResponseBody<>(HttpStatus.OK.value(), message, null))
               )
         );
   }

   public Mono<ServerResponse> forgotPassword(ServerRequest serverRequest) {
      return serverRequest.bodyToMono(ForgetPasswordRequest.class)
         .flatMap(req -> userService.forgotPassword(req)
            .flatMap(message->
               ServerResponse.ok()
                   .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(new ResponseBody<>(HttpStatus.OK.value(), message, null))));
   }

   public Mono<ServerResponse> verifyEmailForForgotPassword(ServerRequest serverRequest) {
      return serverRequest.bodyToMono(VerifyEmailForgetPasswordRequest.class)
          .flatMap(req -> userService.sendVerificationLinkForForgotPassword(req)
             .flatMap(message ->ServerResponse.ok()
                   .contentType(MediaType.APPLICATION_JSON)
                   .bodyValue(new ResponseBody<>(HttpStatus.OK.value(), message, null))));

   }

}
