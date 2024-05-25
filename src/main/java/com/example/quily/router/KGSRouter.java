package com.example.quily.router;

import com.example.quily.handler.KGSHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class KGSRouter {
    @Autowired
    KGSHandler kGSHandler;

    @Bean
    public RouterFunction<ServerResponse> kGSRoutes() {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/kgs/hashKey"),
                        serverRequest -> kGSHandler.getAvailableKey())
                .andRoute(RequestPredicates.PUT("/api/kgs/saveData").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        kGSHandler::saveData
                );
    }
}