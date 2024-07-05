package com.example.quily.dao;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.converter.ShortLinkConverter;
import com.example.quily.exception.BadRequestException;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.services.KeyGeneratorService;
import com.example.quily.services.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.quily.constants.ShortLinkConstants.CUSTOM_ALIAS_EXISTS_MESSAGE;
import static com.example.quily.constants.ShortLinkConstants.CUSTOM_ALIAS_PATTERN_MESSAGE;

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
      if (!isCustomAliasPatter(request.getCustomAlias())) {
         return Mono.error(new BadRequestException(CUSTOM_ALIAS_PATTERN_MESSAGE));
      }
      return shortLinkService.isCustomAliasAvailable(request.getCustomAlias())
         .flatMap(available -> {
            if (!available) {
               return Mono.error(new EntityAlreadyExistException(CUSTOM_ALIAS_EXISTS_MESSAGE));
            }
            final ShortLink shortLink = getShortLink(request, request.getCustomAlias());
            return shortLinkService.createSortLinkAndUpdateIndices(shortLink, Optional.empty());
         });
   }

   private Mono<ShortLink> handleGeneratedAlias(CreateShortLinkRequest request) {
      return keyGeneratorService.getCurrentKey()
         .flatMap(responseDetail -> {
            final String hashKey = responseDetail.getKeyGeneratorResponse().getHashKey();
            final Optional<KeyIndices> nextIndices = Optional.of(responseDetail.getNextIndices());
            final ShortLink shortLink = getShortLink(request, hashKey);
            return shortLinkService.createSortLinkAndUpdateIndices(shortLink, nextIndices);
         });
   }

   private ShortLink getShortLink(CreateShortLinkRequest request, String hashKey) {
      final String shortUrl = String.join("", ShortLinkConstants.LOCALHOST_URL, hashKey);
      return converter.convertRequestToModel(request, shortUrl);
   }

   public Boolean isCustomAliasPatter(String customAliasPatter) {
      return Pattern.compile(ShortLinkConstants.CUSTOM_ALIAS_PATTERN_REGEX)
              .matcher(customAliasPatter)
              .matches();
   }
}