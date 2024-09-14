package com.example.quily.dao;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.converter.ShortLinkConverter;
import com.example.quily.exception.BadRequestException;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.linkpreview.LinkPreviewService;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.security.UserDetailsServiceImpl;
import com.example.quily.services.KeyGeneratorService;
import com.example.quily.services.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.quily.constants.ShortLinkConstants.*;

@Component
public class ShortLinkDAO {
   private final KeyGeneratorService keyGeneratorService;
   private final ShortLinkService shortLinkService;
   private final ShortLinkConverter converter;
   private final UserDetailsServiceImpl userDetailsService;

   @Autowired
   public ShortLinkDAO(KeyGeneratorService keyGeneratorService,
                       ShortLinkService shortLinkService,
                       ShortLinkConverter converter,
                       UserDetailsServiceImpl userDetailsService, LinkPreviewService linkPreviewService) {
      this.keyGeneratorService = keyGeneratorService;
      this.shortLinkService = shortLinkService;
      this.converter = converter;
      this.userDetailsService = userDetailsService;
   }

   public Mono<ShortLink> createShortLink(CreateShortLinkRequest request) {
      return shortLinkService.getTitle(request.getOriginalLink(), request.getTitle())
         .flatMap(title ->
            userDetailsService.getLoggedInUser()
               .flatMap(userDetails -> {
                  if (request.getCustomBackHalf() != null) {
                     return handleCustomAlias(request, userDetails, title);
                  }
                  return handleGeneratedAlias(request, userDetails, title);
               }));
   }

   private Mono<ShortLink> handleCustomAlias(CreateShortLinkRequest request, UserDetails userDetails, String title) {
      if (!isCustomAliasPatter(request.getCustomBackHalf())) {
         return Mono.error(new BadRequestException(CUSTOM_ALIAS_PATTERN_MESSAGE));
      }
      return shortLinkService.isCustomAliasAvailable(request.getCustomBackHalf())
         .flatMap(available -> {
            if (!available) {
               return Mono.error(new EntityAlreadyExistException(CUSTOM_ALIAS_EXISTS_MESSAGE));
            }
            final ShortLink shortLink = getShortLink(request, request.getCustomBackHalf(), userDetails, title);
            return shortLinkService.createSortLinkAndUpdateIndices(shortLink, Optional.empty());
         });
   }

   private Mono<ShortLink> handleGeneratedAlias(CreateShortLinkRequest request, UserDetails userDetails, String title) {
      return keyGeneratorService.getCurrentKey()
         .flatMap(responseDetail -> {
            final String hashKey = responseDetail.getKeyGeneratorResponse().getHashKey();
            final Optional<KeyIndices> nextIndices = Optional.of(responseDetail.getNextIndices());
            final ShortLink shortLink = getShortLink(request, hashKey, userDetails, title);
            return shortLinkService.createSortLinkAndUpdateIndices(shortLink, nextIndices);
         });
   }

   private ShortLink getShortLink(CreateShortLinkRequest request, String hashKey, UserDetails userDetails, String title) {
      final String shortUrl = String.join("", ShortLinkConstants.BASE_URL, hashKey);
      return converter.convertRequestToModel(request, shortUrl, userDetails.getUsername(), title);
   }

   public Boolean isCustomAliasPatter(String customAliasPatter) {
      return Pattern.compile(CUSTOM_ALIAS_PATTERN_REGEX)
              .matcher(customAliasPatter)
              .matches();
   }
}