package com.example.quily.handler;

import com.example.quily.converter.KeyIndicesConverter;
import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.request.KeyGeneratorRequest;
import com.example.quily.response.KeyGeneratorResponse;
import com.example.quily.response.ResponseBody;
import com.example.quily.services.KeyGeneratorService;
import com.example.quily.util.KeyGeneratorUtil;
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
   private final KeyGeneratorDAO keyGeneratorDAO;

   @Autowired
   public KeyGeneratorHandler(KeyGeneratorService keyGeneratorService, KeyIndicesConverter keyIndicesConverter, KeyGeneratorDAO keyGeneratorDAO) {
      this.keyGeneratorService = keyGeneratorService;
      this.keyIndicesConverter = keyIndicesConverter;
      this.keyGeneratorDAO = keyGeneratorDAO;
   }

   public Mono<ServerResponse> updateKey(ServerRequest request) {
      return request.bodyToMono(KeyGeneratorRequest.class)
         .flatMap(req -> keyGeneratorService.updateKeyIndicesIfGreater(keyIndicesConverter.convertRequestToModel(req)))
         .flatMap(updatedKeyIndices -> {
            final ResponseBody<KeyGeneratorResponse> response =
               keyIndicesConverter.convertModelToResponse(updatedKeyIndices);
            return ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(response);
         })
         .switchIfEmpty(handleIfGreaterIndicesFound());
   }

   public Mono<ServerResponse> getAvailableKey() {
      return keyGeneratorService.getCurrentKey()
         .flatMap(keyDetail ->
            ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(
                  keyIndicesConverter.convertModelToResponse(
                     keyGeneratorDAO.getKeyIndicesFromResponse(keyDetail.getKeyGeneratorResponse())
                  )
               ));

   }

   private Mono<ServerResponse> handleIfGreaterIndicesFound() {
      final String errorMessage = KeyGeneratorUtil.GREATER_INDICES_FOUND_EXCEPTION_MESSAGE;
      return ServerResponse.badRequest()
         .contentType(MediaType.APPLICATION_JSON)
         .bodyValue(new ResponseBody<>(
            HttpStatus.BAD_REQUEST.value()
            , errorMessage,
            null
         ));
   }
}