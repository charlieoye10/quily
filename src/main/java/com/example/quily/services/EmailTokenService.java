package com.example.quily.services;

import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.repositories.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EmailTokenService {
   private final EmailVerificationRepository emailVerificationRepository;

   @Autowired
   public EmailTokenService(EmailVerificationRepository emailVerificationRepository) {
      this.emailVerificationRepository = emailVerificationRepository;
   }


   public Mono<EmailConfirmationToken> save(EmailConfirmationToken emailConfirmationToken) {
      return emailVerificationRepository.saveToken(emailConfirmationToken);
   }

   public Mono<EmailConfirmationToken> findByToken(String token) {
      return emailVerificationRepository.findByToken(token);
   }

   public Mono<Long> deleteByToken(String token) {
      return emailVerificationRepository.deleteByToken(token);
   }
}
