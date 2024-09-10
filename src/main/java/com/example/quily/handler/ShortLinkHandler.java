package com.example.quily.handler;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.converter.ShortLinkConverter;
import com.example.quily.dao.ShortLinkDAO;
import com.example.quily.exception.BadRequestException;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.request.UpdateShortLinkRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

import static com.example.quily.constants.ColumnNameConstants.*;
import static com.example.quily.constants.CommonConstants.PARAMS_VALUE_NOT_PRESENT;
import static com.example.quily.constants.ShortLinkConstants.*;


@Component
public class ShortLinkHandler {
   private final ShortLinkDAO shortLinkDAO;
   private final ShortLinkService shortLinkService;
   private final ShortLinkConverter shortLinkConverter;

   @Autowired
   public ShortLinkHandler(ShortLinkDAO shortLinkDAO, ShortLinkService shortLinkService, ShortLinkConverter shortLinkConverter) {
      this.shortLinkDAO = shortLinkDAO;
      this.shortLinkService = shortLinkService;
      this.shortLinkConverter = shortLinkConverter;
   }

   public Mono<ServerResponse> createShortLink(ServerRequest serverRequest) {
      return serverRequest.bodyToMono(CreateShortLinkRequest.class)
         .flatMap(req ->
            shortLinkDAO.createShortLink(req)
               .flatMap(createdShortLink -> {
                     ResponseBody<ShortLinkResponse> responseBody =
                        shortLinkConverter.getResponseFromModel(createdShortLink, HttpStatus.OK.value(), SHORT_LINK_CREATED_MESSAGE);
                     return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(responseBody);
                  }
               )
         );
   }

   public Mono<ServerResponse> redirect(ServerRequest serverRequest) {
      return shortLinkService
         .getShortLinkDetail(serverRequest.uri().toString())
         .flatMap(linkDetail -> ServerResponse.status(HttpStatus.FOUND)
            .location(URI.create(linkDetail.getOriginalLink()))
            .build())
         .switchIfEmpty(Mono.error(new ResourceNotFoundException(INVALID_URL_MESSAGE)));
   }

   private Mono<ServerResponse> handleIfInvalidUrl() {
      return ServerResponse.status(HttpStatus.BAD_REQUEST)
         .contentType(MediaType.APPLICATION_JSON)
         .bodyValue(
            new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), INVALID_URL_MESSAGE, null)
         );
   }

   public Mono<ServerResponse> deleteShortLink(ServerRequest serverRequest) {
    final Optional<String> shortLinkUrlOpt = serverRequest.queryParam("shortLink");
      final String errorMessage = ShortLinkConstants.SHORT_LINK_DOES_NOT_PASSED;
      return shortLinkUrlOpt.map(shortLink ->
         shortLinkService.deleteShortLink(shortLink)
         .flatMap(message -> ServerResponse.ok()
         .contentType(MediaType.APPLICATION_JSON)
         .bodyValue(new ResponseBody<>(HttpStatus.OK.value(), message, null))))
         .orElseGet(() -> Mono.error(new BadRequestException(errorMessage)));

   }

   public Mono<ServerResponse> getShortLinks(ServerRequest serverRequest) {
      final Optional<Integer> pageSize = serverRequest.queryParam(PAGE_SIZE).map(Integer::parseInt);
      final Optional<Integer> pageNumber = serverRequest.queryParam(PAGE_NUMBER).map(Integer::parseInt);
      final Optional<String> userEmail = serverRequest.queryParam(USER_EMAIL);
      boolean allPresent = pageSize.isPresent() && pageNumber.isPresent() && userEmail.isPresent();
      if (allPresent) {
         return shortLinkService.getShortLinks(pageSize.get(), pageNumber.get(), userEmail.get())
            .collectList()
            .flatMap(shortLinkList -> ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(new ResponseBody<>(HttpStatus.OK.value(), null, shortLinkList)));
      } else {
         return Mono.error(new BadRequestException(PARAMS_VALUE_NOT_PRESENT));
      }
   }

   public Mono<ServerResponse> updateShortLink(ServerRequest serverRequest) {
      return serverRequest.bodyToMono(UpdateShortLinkRequest.class)
         .flatMap(req ->
            shortLinkService.updateShortLink(req)
               .flatMap(shortLinkResponse -> ServerResponse.ok()
                  .contentType(MediaType.APPLICATION_JSON)
                  .bodyValue(new ResponseBody<>(HttpStatus.OK.value(),SHORT_LINK_UPDATED_MESSAGE ,shortLinkResponse ))));
   }

   public Mono<ServerResponse> getLinkDetail(ServerRequest request) {
      final Optional<String> backHalf = request.queryParam("backHalf");
      final String userEmail = request.headers().firstHeader("UserEmail");
      if (backHalf.isEmpty()) {
         return Mono.error(new BadRequestException(PARAMS_VALUE_NOT_PRESENT));
      }
      final String shortURL = String.join("", BASE_URL, backHalf.get());
      return shortLinkService.getShortLinkDetail(shortURL)
         .filter(linkDetail -> linkDetail.getUserEmail().equals(userEmail))
         .flatMap(linkDetail -> {
            final ResponseBody<ShortLinkResponse> responseBody = shortLinkConverter.getResponseFromModel(linkDetail, HttpStatus.OK.value(), null);
            return ServerResponse.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .bodyValue(responseBody);
         })
         .switchIfEmpty(handleIfInvalidUrl());
   }
}