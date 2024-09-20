package com.example.quily.router;

import com.example.quily.handler.KeyGeneratorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class KeyGeneratorRouter {
   private static final String GET_KEY_URL = "/api/kgs/getKey";
   private static final String KGS_UPDATE_URL = "/api/kgs/update";
   private final KeyGeneratorHandler keyGeneratorHandler;

   @Autowired
   public KeyGeneratorRouter(KeyGeneratorHandler keyGeneratorHandler) {
      this.keyGeneratorHandler = keyGeneratorHandler;
   }

   @Bean
   public RouterFunction<ServerResponse> kGSRoutes() {
      return RouterFunctions
         .route(RequestPredicates.GET(GET_KEY_URL), req -> keyGeneratorHandler.getAvailableKey())
         .andRoute(RequestPredicates.PUT(KGS_UPDATE_URL), keyGeneratorHandler::updateKey);
      //test by sami
   }
}