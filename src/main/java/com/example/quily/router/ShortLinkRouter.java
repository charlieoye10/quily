package com.example.quily.router;

import com.example.quily.handler.ShortLinkHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ShortLinkRouter {
   private final ShortLinkHandler shortLinkHandler;
   private static final String CREATE_SHORT_LINK_URL = "/api/shortLink/create";
  private static final String GET_ORIGINAL_LINK_URL = "/{id:(?!api).+}";
   @Autowired
   public ShortLinkRouter(ShortLinkHandler shortLinkHandler) {
      this.shortLinkHandler = shortLinkHandler;
   }

   @Bean
   public RouterFunction<ServerResponse> ShortLinkRoutes() {
      return RouterFunctions
         .route(RequestPredicates.POST(CREATE_SHORT_LINK_URL), shortLinkHandler::createShortLink)
         .andRoute(RequestPredicates.GET(GET_ORIGINAL_LINK_URL), shortLinkHandler::getOriginalLink);
   }
}