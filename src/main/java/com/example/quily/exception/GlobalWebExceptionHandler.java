package com.example.quily.exception;

import com.example.quily.response.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@Order(-2)
public class GlobalWebExceptionHandler implements WebExceptionHandler {

   @Override
   public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
      HttpStatus status =
         switch (ex.getClass().getSimpleName()) {
         case "ResourceNotFoundException" -> HttpStatus.NOT_FOUND;
         case "AlreadyExistEntityException" -> HttpStatus.CONFLICT;
         case "GreaterIndicesFoundException", "BadRequestException", "EmailFormatException" ->
            HttpStatus.BAD_REQUEST;
         default -> HttpStatus.INTERNAL_SERVER_ERROR;
      };

      ResponseBody<String> response = new ResponseBody<>(
         status.value(),
         ex.getMessage(),
         null
      );

      exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
      exchange.getResponse().setStatusCode(status);

      try {
         return exchange.getResponse().writeWith(
            Mono.just(exchange.getResponse().bufferFactory().wrap(
               new ObjectMapper().writeValueAsBytes(response)
            ))
         );
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
      }
   }
}