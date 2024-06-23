package com.example.quily.handler;

import com.example.quily.converter.UserSignupConverter;
import com.example.quily.dao.UserDao;
import com.example.quily.exception.BadRequestException;
import com.example.quily.request.SignUpRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.services.UserService;
import com.example.quily.util.UserUtil;
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
         .flatMap(userDao::emailVerification)
         .flatMap(resString -> {
            ResponseBody<String> responseBody =
               new ResponseBody<>(HttpStatus.OK.value(), "", resString);
            return ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(responseBody);
         });
   }

   public Mono<ServerResponse> verifyUser(ServerRequest serverRequest) {
      final String errorMessage = UserUtil.VERIFICATION_FAILED_MESSAGE;
      final Optional<String> tokenOpt = serverRequest.queryParam("token");
      return tokenOpt.map(token -> userService.verifyTokenAndSaveUser(token)
            .flatMap(signUpResponse -> ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(
                  userSignupConverter.convertModelToResponse(signUpResponse)
               )))
         .orElseGet(() -> Mono.error(new BadRequestException(errorMessage)));

   }
}
