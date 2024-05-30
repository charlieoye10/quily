package com.example.quily.router;

import com.example.quily.handler.KGSHandler;
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
    @Autowired
    UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return RouterFunctions
                .route(RequestPredicates.POST("/api/auth/signup"),
                        serverRequest -> userHandler.signUp(serverRequest));
    }
}
