package com.example.quily.dao;

import com.example.quily.converter.ShortLinkConverter;
import com.example.quily.exception.AlreadyExistEntityException;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.services.KeyGeneratorService;
import com.example.quily.services.ShortLinkService;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ShortLinkDAO {
   private final KeyGeneratorService keyGeneratorService;
   private final ShortLinkService shortLinkService;
   private final ShortLinkConverter converter;

   @Autowired
   public ShortLinkDAO(KeyGeneratorService keyGeneratorService, ShortLinkService shortLinkService, ShortLinkConverter converter) {
      this.keyGeneratorService = keyGeneratorService;
      this.shortLinkService = shortLinkService;
      this.converter = converter;
   }

   public Mono<ShortLink> createShortLink(CreateShortLinkRequest request) {
      if (request.getCustomAlias() != null) {
         return handleCustomAlias(request);
      }
      return handleGeneratedAlias(request);
   }

   private Mono<ShortLink> handleCustomAlias(CreateShortLinkRequest request) {
      return shortLinkService.isCustomAliasAvailable(request.getCustomAlias())
         .flatMap(available -> {
            if (!available) {
               return Mono.error(new AlreadyExistEntityException("The custom alias is already present."));
            }
            final ShortLink shortLink = getShortLink(request, request.getCustomAlias());
            return shortLinkService.createSortLinkAndUpdateIndices(shortLink, Optional.empty());
         });
   }

   private Mono<ShortLink> handleGeneratedAlias(CreateShortLinkRequest request) {
      return keyGeneratorService.getCurrentKey()
         .flatMap(responseDetail -> {
            String hashKey = responseDetail.getKeyGeneratorResponse().getHashKey();
            Optional<KeyIndices> nextIndices = Optional.of(responseDetail.getNextIndices());
            final ShortLink shortLink = getShortLink(request, hashKey);
            return shortLinkService.createSortLinkAndUpdateIndices(shortLink, nextIndices);
         });
   }

   private ShortLink getShortLink(CreateShortLinkRequest request, String hashKey) {
      ShortLink shortLink = converter.convertRequestToModel(request);
      shortLink.setShortedLink(ShortLinkUtil.LOCALHOST_URL + hashKey);
      return shortLink;
   }
}