package com.example.quily.handler;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.converter.ShortLinkConverter;
import com.example.quily.dao.ShortLinkDAO;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ResponseBody;
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
      return serverRequest.bodyToMono(CreateShortLinkRequest.class)
         .flatMap(req ->
            shortLinkDAO.createShortLink(req)
               .flatMap(createdShortLink -> {
                     ResponseBody<ShortLinkResponse> responseBody =
                        shortLinkConverter.getResponseFromModel(createdShortLink);
                     return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody);
                  }
               )
         );
   }

   public Mono<ServerResponse> getOriginalLink(ServerRequest serverRequest) {
      return shortLinkService
         .findOriginalLink(serverRequest.uri().toString())
         .flatMap(originalLink -> {
            final ResponseBody<String> responseBody = new ResponseBody<>(
               HttpStatus.OK.value(),
               "",
               originalLink
            );
            return ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(responseBody);
         })
         .switchIfEmpty(handleIfInvalidUrl());
   }

   private Mono<ServerResponse> handleIfInvalidUrl() {
      final String errorMessage = ShortLinkConstants.INVALID_URL_MESSAGE;
      return ServerResponse.status(HttpStatus.BAD_REQUEST)
         .contentType(MediaType.APPLICATION_JSON)
         .bodyValue(
            new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), errorMessage, null)
         );
   }

   public Mono<ServerResponse> deleteShortLink(ServerRequest serverRequest)
   {
      String shortLink = serverRequest.pathVariable("shortLink");
      return shortLinkService.deleteShortLink(shortLink)
         .flatMap(message ->ServerResponse.ok()
         .contentType(MediaType.APPLICATION_JSON)
         .bodyValue(new ResponseBody<>(HttpStatus.OK.value(), message, null)));
   }
}