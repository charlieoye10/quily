package com.example.quily.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {
   private static final String BEARER = "Bearer ";
   private final JwtUtil jwtUtil;
   private final ReactiveUserDetailsService userDetailsService;

   @Autowired
   public JwtServerAuthenticationConverter(JwtUtil jwtUtil, ReactiveUserDetailsService userDetailsService) {
      this.jwtUtil = jwtUtil;
      this.userDetailsService = userDetailsService;
   }

   @Override
   public Mono<Authentication> convert(ServerWebExchange exchange) {
      return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
         .filter(header -> header.startsWith(BEARER))
         .map(header -> header.substring(BEARER.length()))
         .flatMap(token -> {
            String userEmail = jwtUtil.getEmailFromToken(token);
            return userDetailsService.findByUsername(userEmail)
               .filter(userDetails -> jwtUtil.validateToken(token, userDetails.getUsername()))
               .map(userDetails -> new JwtToken(Collections.emptyList()));
         });
   }
}
