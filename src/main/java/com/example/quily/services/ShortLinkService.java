package com.example.quily.services;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.exception.InternalServerError;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.repositories.ShortLinkRepository;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.request.UpdateShortLinkRequest;

import com.example.quily.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import java.util.Optional;

import static com.example.quily.constants.CommonConstants.COMMON_INTERNAL_SERVER_MESSAGE;
import static com.example.quily.constants.ShortLinkConstants.SHORT_LINK_DOES_NOT_EXIST_MESSAGE;


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

   public Mono<ShortLink> findOriginalLink(String shortLink) {
      return shortLinkRepository
         .getShortLink(shortLink);
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
         );
   }

   public Flux<ShortLinkResponse> getShortLinks(int pageSize, int pageNumber, String userEmail) {
      return shortLinkRepository.getShortLinks(pageSize, pageNumber, userEmail);
   }

   public Mono<ShortLinkResponse> updateShortLink(UpdateShortLinkRequest req) {
      return shortLinkRepository.getShortLink(req.getShortedLink())
         .flatMap(shortLink -> {
            String shortedLink = req.getCustomAlias() != null ? req.getCustomAlias() : req.getShortedLink();
            String originalLink = req.getOriginalLink() != null ? req.getOriginalLink() : shortLink.getOriginalLink();

            return shortLinkRepository.updateShortLink(shortLink.getId(), shortedLink, originalLink)
               .flatMap(
                  updatedShortLink ->
                     Mono.just(new ShortLinkResponse(
                        shortLink.getUserEmail(),
                        originalLink,
                        shortedLink,
                        shortLink.getExpiryDate(),
                        shortLink.getCreationDate()
                     )));
         })
         .switchIfEmpty(Mono.error(new ResourceNotFoundException(SHORT_LINK_DOES_NOT_EXIST_MESSAGE)));
   }
}