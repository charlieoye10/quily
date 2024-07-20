package com.example.quily.handler;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.converter.ShortLinkConverter;
import com.example.quily.dao.ShortLinkDAO;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.ShortLinkService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@Component
public class ShortLinkHandler {
   private final ShortLinkDAO shortLinkDAO;
   private final ShortLinkService shortLinkService;
   private final ShortLinkConverter shortLinkConverter;
   private final Bucket bucket; // Bucket for rate limiting

   @Autowired
   public ShortLinkHandler(ShortLinkDAO shortLinkDAO, ShortLinkService shortLinkService, ShortLinkConverter shortLinkConverter) {
      this.shortLinkDAO = shortLinkDAO;
      this.shortLinkService = shortLinkService;
      this.shortLinkConverter = shortLinkConverter;

      Bandwidth limit = Bandwidth.classic(200, Refill.intervally(200, Duration.ofMinutes(1)));
      this.bucket = Bucket4j.builder()
         .addLimit(limit)
         .build();
   }

   public Mono<ServerResponse> createShortLink(ServerRequest serverRequest) {
      return serverRequest.bodyToMono(CreateShortLinkRequest.class)
         .flatMap(req -> {
            // Check if rate limit is exceeded
            if (bucket.tryConsume(1)) {
               return shortLinkDAO.createShortLink(req)
                  .flatMap(createdShortLink -> {
                     ResponseBody<ShortLinkResponse> responseBody =
                        shortLinkConverter.getResponseFromModel(createdShortLink);
                     return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody);
                  });
            } else {
               return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                  .contentType(MediaType.APPLICATION_JSON)
                  .bodyValue(new ResponseBody<>(
                     HttpStatus.TOO_MANY_REQUESTS.value(),
                     "Something went wrong, please try again after some time.",
                     null
                  ));
            }
         });
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
}
