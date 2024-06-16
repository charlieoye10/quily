package com.example.quily.handler;

import com.example.quily.dao.UserDao;
import com.example.quily.exception.BadRequestException;
import com.example.quily.exception.ErrorResponse;
import com.example.quily.request.SignUpRequest;
import com.example.quily.response.ResponseBody;
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
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    public Mono<ServerResponse> signUp(ServerRequest request) {
       return request.bodyToMono(SignUpRequest.class)
                .flatMap(req -> userDao.emailVerification(req))
                .flatMap(resString -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(
                                new ResponseBody<>(200, "", resString)
						));
    }

    public Mono<ServerResponse> verifyUser(ServerRequest serverRequest) {
        Optional<String> tokenOpt = serverRequest.queryParam("token");
        return tokenOpt.map(token -> userService.verifyTokenAndSaveUser(token)
                .flatMap(signUpResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(signUpResponse.toSuccessResponse())))
				.orElseGet(() -> Mono.error(new BadRequestException("Email verification failed, token did not match either it was invalid or empty")));
    }
}
