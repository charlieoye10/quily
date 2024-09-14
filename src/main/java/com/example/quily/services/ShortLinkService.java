package com.example.quily.services;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.linkpreview.LinkPreviewService;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.repositories.ShortLinkRepository;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.request.UpdateShortLinkRequest;
import com.example.quily.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.quily.constants.ShortLinkConstants.*;


@Service
@Component
public class ShortLinkService {
   private final KeyGeneratorService KeyGeneratorService;
   private final ShortLinkRepository shortLinkRepository;
   private final UserDetailsServiceImpl userDetailsService;
   private final LinkPreviewService linkPreviewService;

   @Autowired
   public ShortLinkService(KeyGeneratorService keyGeneratorService, ShortLinkRepository shortLinkRepository,
                           UserDetailsServiceImpl userDetailsService, LinkPreviewService linkPreviewService) {
      this.KeyGeneratorService = keyGeneratorService;
      this.shortLinkRepository = shortLinkRepository;
      this.userDetailsService = userDetailsService;

      this.linkPreviewService = linkPreviewService;
   }

   public Mono<ShortLink> createShortLink(ShortLink shortLink) {
      return shortLinkRepository.createShortLink(shortLink);
   }

   public Mono<ShortLink> getShortLinkDetail(String shortLink) {
      return shortLinkRepository
         .getShortLink(shortLink);
   }

   public Mono<Boolean> isCustomAliasAvailable(String customAlias) {
      final String url = String.join("", ShortLinkConstants.BASE_URL, customAlias);
      return shortLinkRepository
         .getShortLink(url)
         .map(link -> false)
         .defaultIfEmpty(true);
   }

   public Mono<Boolean> isOriginalLinkAvailableForTeam(String originalLink, String email) {
      return shortLinkRepository
         .getShortLinkByOriginalLinkAndEmail(originalLink, email)
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
      String shortedLink = ShortLinkConstants.BASE_URL + shortLink;

      return userDetailsService.getLoggedInUser()
         .flatMap(userDetails ->
            shortLinkRepository.deleteShortLink(shortedLink, userDetails.getUsername())
         );
   }

   public Flux<ShortLinkResponse> getShortLinks(int pageSize, int pageNumber, String userEmail) {
      return shortLinkRepository.getShortLinks(pageSize, pageNumber, userEmail);
   }

   public Mono<List<String>> getTitleAndLogo(String url, String titleValue) {
      if (titleValue != null && !titleValue.isEmpty()) {
         return Mono.just(Arrays.asList(titleValue, ""));
      }
      return linkPreviewService.getUrlDetails(url)
         .map(linkDetail -> {
            if (linkDetail.getTitle().isEmpty() || linkDetail.getTitle().equals("Untitled")) {
               if (url.length() < 50) return Arrays.asList(url + " - untitled", "");
               else return Arrays.asList(url.substring(0, 50) + " - untitled", "");
            } else return Arrays.asList(linkDetail.getTitle(), linkDetail.getImage());
         });
   }

   public Mono<ShortLinkResponse> updateShortLink(UpdateShortLinkRequest req) {
      return shortLinkRepository.getShortLink(req.getShortedLink())
         .flatMap(shortLink -> getTitleAndLogo(req.getOriginalLink(), req.getTitle())
            .flatMap(titleAndLogo -> {
               if (req.getCustomBackHalf() != null && req.getOriginalLink() != null) {
                  return handleShortLinkAndOriginalLinkUpdate(shortLink, req.getOriginalLink(), req.getCustomBackHalf(), titleAndLogo.get(0), titleAndLogo.get(1));
               }
               if (req.getCustomBackHalf() != null) {
                  return handleShortLinkUpdate(shortLink, req.getCustomBackHalf(), req.getTitle(), "");
               }
               if (req.getTitle() != null) {
                  return handTitleRequest(shortLink, titleAndLogo.get(0), titleAndLogo.get(1));
               }
               return handleOriginalLinkUpdate(shortLink, req.getOriginalLink(), titleAndLogo.get(0), titleAndLogo.get(1));
            })).switchIfEmpty(Mono.error(new ResourceNotFoundException(SHORT_LINK_DOES_NOT_EXIST_MESSAGE)));
   }

   private Mono<ShortLinkResponse> handleShortLinkAndOriginalLinkUpdate(ShortLink shortLink, String originalLink, String customAlias, String title, String logo) {
      final String url = String.join("", ShortLinkConstants.BASE_URL, customAlias);
      return shortLinkRepository.getShortLinkByOriginalLinkCustomAliasAndEmail(originalLink, url, shortLink.getUserEmail())
         .flatMap(existingShortLink -> {
            if (existingShortLink.getShortedLink().equals(url) && existingShortLink.getOriginalLink().equals(originalLink)) {
               return Mono.error(new EntityAlreadyExistException(CUSTOM_ALIAS_AND_SHORT_LINK_EXISTS_MESSAGE));
            } else if (existingShortLink.getShortedLink().equals(url)) {
               return Mono.error(new EntityAlreadyExistException(CUSTOM_ALIAS_EXISTS_MESSAGE));
            } else {
               return Mono.error(new EntityAlreadyExistException(LINK_ALREADY_USED_MESSAGE));
            }
         }).cast(ShortLinkResponse.class)
         .switchIfEmpty(
            shortLinkRepository.updateShortLink(shortLink.getId(), url, originalLink, title, logo)
               .map(updatedShortLink -> new ShortLinkResponse(
                  shortLink.getUserEmail(),
                  originalLink,
                  url,
                  shortLink.getExpiryDate(),
                  shortLink.getCreationDate(),
                  shortLink.getUpdatedTime(),
                  shortLink.isQrCreated(),
                  title,
                  logo
               ))
         );
   }

   private Mono<ShortLinkResponse> handleShortLinkUpdate(ShortLink shortLink, String customAlias, String title, String logo) {
      final String url = String.join("", ShortLinkConstants.BASE_URL, customAlias);
      return isCustomAliasAvailable(customAlias)
         .flatMap(isAvailable -> {
            if (!isAvailable) {
               return Mono.error(new EntityAlreadyExistException(CUSTOM_ALIAS_EXISTS_MESSAGE));
            }
            return shortLinkRepository.updateShortLink(shortLink.getId(), url, shortLink.getOriginalLink(), title, logo)
               .map(updatedShortLink -> new ShortLinkResponse(
                  shortLink.getUserEmail(),
                  shortLink.getOriginalLink(),
                  url,
                  shortLink.getExpiryDate(),
                  shortLink.getCreationDate(),
                  shortLink.getUpdatedTime(),
                  shortLink.isQrCreated(),
                  title,
                  logo
               ));
         });
   }

   private Mono<ShortLinkResponse> handleOriginalLinkUpdate(ShortLink shortLink, String originalLink, String title, String logo) {
      return isOriginalLinkAvailableForTeam(originalLink, shortLink.getUserEmail())
         .flatMap(isAvailable -> {
            if (!isAvailable) {
               return Mono.error(new EntityAlreadyExistException(LINK_ALREADY_USED_MESSAGE));
            }
            return shortLinkRepository.updateShortLink(shortLink.getId(), shortLink.getShortedLink(), originalLink, title, logo)
               .map(updatedShortLink -> new ShortLinkResponse(
                  shortLink.getUserEmail(),
                  originalLink,
                  shortLink.getShortedLink(),
                  shortLink.getExpiryDate(),
                  shortLink.getCreationDate(),
                  shortLink.getUpdatedTime(),
                  shortLink.isQrCreated(),
                  title,
                  logo
               ));
         });
   }

   private Mono<ShortLinkResponse> handTitleRequest(ShortLink shortLink, String title, String logo) {
      return shortLinkRepository.updateShortLink(shortLink.getId(), shortLink.getShortedLink(), shortLink.getOriginalLink(), title, logo)
         .map(updatedShortLink -> new ShortLinkResponse(
            shortLink.getUserEmail(),
            shortLink.getOriginalLink(),
            shortLink.getShortedLink(),
            shortLink.getExpiryDate(),
            shortLink.getCreationDate(),
            shortLink.getUpdatedTime(),
            shortLink.isQrCreated(),
            title,
            logo
         ));
   }
}