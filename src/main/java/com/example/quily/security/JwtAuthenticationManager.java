package com.example.quily.security;

import com.example.quily.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.example.quily.constants.UserConstants.TOKEN_VALIDATION_FAILED_MESSAGE;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
   private final JwtUtil jwtUtil;

   @Autowired
   public JwtAuthenticationManager(JwtUtil jwtUtil) {
      this.jwtUtil = jwtUtil;
   }

   @Override
   public Mono<Authentication> authenticate(Authentication authentication) {
      return Mono.just(authentication)
         .cast(JwtToken.class)
         .map(userDetails -> {
            userDetails.setUserAuthenticated();
            return (Authentication) userDetails;
         })
         .switchIfEmpty(Mono.error(new BadRequestException(TOKEN_VALIDATION_FAILED_MESSAGE)));
   }
}
