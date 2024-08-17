package com.example.quily.services;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.exception.InternalServerError;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.repositories.ShortLinkRepository;
import com.example.quily.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Optional;

import static com.example.quily.constants.CommonConstants.COMMON_INTERNAL_SERVER_MESSAGE;

@Service
@Component
public class ShortLinkService {
   private final KeyGeneratorService KeyGeneratorService;
   private final ShortLinkRepository shortLinkRepository;
   private final UserDetailsServiceImpl userDetailsService;

   @Autowired
   public ShortLinkService(KeyGeneratorService keyGeneratorService, ShortLinkRepository shortLinkRepository,
                           UserDetailsServiceImpl userDetailsService) {
      this.KeyGeneratorService = keyGeneratorService;
      this.shortLinkRepository = shortLinkRepository;
      this.userDetailsService = userDetailsService;

   }

   public Mono<ShortLink> createShortLink(ShortLink shortLink) {
      return shortLinkRepository.createShortLink(shortLink);
   }

   public Mono<String> findOriginalLink(String shortLink) {
      return shortLinkRepository
         .getShortLink(shortLink)
         .map(ShortLink::getOriginalLink);
   }

   public Mono<Boolean> isCustomAliasAvailable(String customAlias) {
      final String url = String.join("", ShortLinkConstants.LOCALHOST_URL, customAlias);
      return shortLinkRepository
         .getShortLink(url)
         .map(link -> false)
         .defaultIfEmpty(true);
   }

   public Mono<ShortLink> createSortLinkAndUpdateIndices(ShortLink shortLink, Optional<KeyIndices> currentIndicesOpt) {
      final Mono<ShortLink> savedLink = createShortLink(shortLink);
      final Mono<KeyIndices> updatedIndices = currentIndicesOpt
         .map(KeyGeneratorService::updateKeyIndices)
         .orElse(Mono.empty());

      return updatedIndices
         .flatMap(ind -> Mono.zip(savedLink, Mono.just(ind)).map(Tuple2::getT1))
         .switchIfEmpty(savedLink);
   }

   public Mono<String> deleteShortLink(String shortLink) {
      String shortedLink = ShortLinkConstants.LOCALHOST_URL + shortLink;

      return userDetailsService.getLoggedInUser()
         .flatMap(userDetails ->
            shortLinkRepository.deleteShortLink(shortedLink, userDetails.getUsername())
               .switchIfEmpty(Mono.error(new InternalServerError(COMMON_INTERNAL_SERVER_MESSAGE)))
         );
   }

}