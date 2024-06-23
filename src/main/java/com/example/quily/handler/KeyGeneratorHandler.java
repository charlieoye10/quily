package com.example.quily.handler;

import com.example.quily.converter.KeyIndicesConverter;
import com.example.quily.exception.ErrorResponse;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.request.KeyGeneratorRequest;
import com.example.quily.services.KeyGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class KeyGeneratorHandler {
   private final KeyGeneratorService keyGeneratorService;
   private final KeyIndicesConverter keyIndicesConverter;

   @Autowired
   public KeyGeneratorHandler(KeyGeneratorService keyGeneratorService, KeyIndicesConverter keyIndicesConverter) {
      this.keyGeneratorService = keyGeneratorService;
      this.keyIndicesConverter = keyIndicesConverter;
   }

   public Mono<ServerResponse> updateKey(ServerRequest request) {
      return request.bodyToMono(KeyGeneratorRequest.class)
         .flatMap(req -> keyGeneratorService.updateIfGreater(keyIndicesConverter.convertRequestToModel(req)))
         .flatMap(kgsResponse -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(kgsResponse))
         .onErrorResume(ResourceNotFoundException.class, e ->
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(new ErrorResponse("Internal Server Error with error message: ", e.getMessage())));

   }

   public Mono<ServerResponse> getAvailableKey() {
      return keyGeneratorService.getCurrentKey()
         .flatMap(kgsResponse -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(kgsResponse.getKeyGeneratorResponse()))
         .onErrorResume(ResourceNotFoundException.class, e ->
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(new ErrorResponse("Internal Server Error with error message: ", e.getMessage())));

   }
}