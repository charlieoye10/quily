package com.example.quily.handler;

import com.example.quily.converter.ShortLinkConverter;
import com.example.quily.dao.ShortLinkDAO;
import com.example.quily.exception.ErrorResponse;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ShortLinkHandler {
   private final ShortLinkDAO shortLinkDAO;
   private final ShortLinkService shortLinkService;
   private final ShortLinkConverter shortLinkConverter;

   @Autowired
   public ShortLinkHandler(ShortLinkDAO shortLinkDAO, ShortLinkService shortLinkService, ShortLinkConverter shortLinkConverter) {
      this.shortLinkDAO = shortLinkDAO;
      this.shortLinkService = shortLinkService;
      this.shortLinkConverter = shortLinkConverter;
   }

   public Mono<ServerResponse> createShortLink(ServerRequest serverRequest) {
      final Mono<CreateShortLinkRequest> monoShortLinkRequest =
         serverRequest.bodyToMono(CreateShortLinkRequest.class);
      return ServerResponse.ok()
         .contentType(MediaType.APPLICATION_JSON)
         .body(
            monoShortLinkRequest
               .flatMap(shortLinkDAO::createShortLink)
               .map(shortLinkConverter::convertModelToResponse),
            ShortLinkResponse.class
         );
   }

   public Mono<ServerResponse> getOriginalLink(ServerRequest serverRequest) {
      return shortLinkService.findOriginalLink(serverRequest.uri().toString())
         .flatMap(originalLink -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(originalLink))
         .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new ErrorResponse("Looks like this URL is not in the database",
               "Please provide a valid URL")))
         .onErrorResume(e ->
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(new ErrorResponse("error:", e.getMessage()))
         );
   }
}