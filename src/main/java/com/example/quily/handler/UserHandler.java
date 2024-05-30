package com.example.quily.handler;

import com.example.quily.dao.UserDao;
import com.example.quily.exception.AlreadyExistEntityException;
import com.example.quily.exception.ErrorResponse;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.User;
import com.example.quily.request.SignUpRequest;
import com.example.quily.services.DbService;
import com.example.quily.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {
    @Autowired
    DbService<User, String> dbService;

    @Autowired
    UserDao userDao;
    public Mono<ServerResponse> signUp(ServerRequest request) {
       return request.bodyToMono(SignUpRequest.class)
                .flatMap(req -> dbService.save(userDao.signUpReqToUser(req)))
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser.toSignUpResponse()))
               .onErrorResume(AlreadyExistEntityException.class, e ->
                       ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .contentType(MediaType.APPLICATION_JSON)
                               .bodyValue(new ErrorResponse("Internal Server Error with error message: ", e.getMessage()) {
                               }));
    }
}
