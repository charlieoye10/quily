package com.example.quily.services;

import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.repositories.EmailTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class EmailTokenService {
   private final EmailTokenRepository emailTokenRepository;

   @Autowired
   public EmailTokenService(EmailTokenRepository emailTokenRepository) {
      this.emailTokenRepository = emailTokenRepository;
   }


   public Mono<EmailConfirmationToken> save(EmailConfirmationToken emailConfirmationToken) {
      return emailTokenRepository.saveToken(emailConfirmationToken);
   }

   public Mono<EmailConfirmationToken> findByToken(String token) {
      return emailTokenRepository.findByToken(token);
   }

   public Mono<Long> deleteByToken(String token) {
      return emailTokenRepository.deleteByToken(token);
   }
}
