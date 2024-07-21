package com.example.quily.filter;

import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimiterFilter implements WebFilter {

   private final Map<String, Bucket> buckets = new HashMap<>();

   @Autowired
   public RateLimiterFilter(Bucket createShortLinkBucket) {
      buckets.put("/api/shortLink/create", createShortLinkBucket);
   }

   @Override
   public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
      String path = exchange.getRequest().getPath().value();
      Bucket bucket = buckets.get(path);

      if (bucket == null) {
         return chain.filter(exchange);
      }

      if (bucket.tryConsume(1)) {
         return chain.filter(exchange);
      } else {
         exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
         exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
         String responseBody = "{\"message\": \"Something went wrong Please try again after some time\"}";
         byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
         exchange.getResponse().getHeaders().setContentLength(bytes.length);
         return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
      }
   }
}
