package com.example.quily.exception;

import com.example.quily.response.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.example.quily.constants.ShortLinkConstants.FE_LOCALHOST_URL;
import static com.example.quily.constants.ShortLinkConstants.NETLIFY_FE_DOMAIN;

@Configuration
@Order(-2)
public class GlobalWebExceptionHandler implements WebExceptionHandler {

   @Override
   public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
      HttpStatus status =
         switch (ex.getClass().getSimpleName()) {
            case "ResourceNotFoundException" -> HttpStatus.NOT_FOUND;
            case "EntityAlreadyExistException" -> HttpStatus.CONFLICT;
            case "GreaterIndicesFoundException", "BadRequestException", "EmailFormatException" ->
               HttpStatus.BAD_REQUEST;
            case "UnauthorizedException" -> HttpStatus.UNAUTHORIZED;
            case "ExpiredJwtException" -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
         };

      ResponseBody<String> response = new ResponseBody<>(
         status.value(),
         ex.getMessage(),
         null
      );
      exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
      exchange.getResponse().setStatusCode(status);
      return handleResponse(exchange, ex, response);
   }


   private Mono<Void> handleResponse(ServerWebExchange exchange, Throwable ex, ResponseBody<String> response) {
      try {
         HttpHeaders headers = exchange.getResponse().getHeaders();
         if (NETLIFY_FE_DOMAIN.equals(headers.getAccessControlAllowOrigin()) || FE_LOCALHOST_URL.equals(headers.getAccessControlAllowOrigin())) {
            headers.setAccessControlAllowOrigin(headers.getAccessControlAllowOrigin());
         }
         headers.setAccessControlAllowMethods(List.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH));
         headers.setAccessControlAllowHeaders(List.of("Content-Type", "Authorization", "X-Requested-With"));

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