package com.example.quily.services;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Component
public class ShortLinkService implements DbService<ShortLink, Long> {
   private final DatabaseClient databaseClient;
   private final KeyGeneratorService KeyGeneratorService;

   @Autowired
   public ShortLinkService(DatabaseClient databaseClient, com.example.quily.services.KeyGeneratorService keyGeneratorService) {
      this.databaseClient = databaseClient;
      this.KeyGeneratorService = keyGeneratorService;
   }

   @Override
   public Flux<ShortLink> findAll() {
      return null;
   }

   @Override
   public Mono<ShortLink> findByUniqueId(Long id) {
      return null;
   }

   @Override
   public Mono<ShortLink> update(ShortLink shortLink) {
      return null;
   }

   @Override
   public void delete(Long id) {}

   @Override
   public Mono<ShortLink> save(ShortLink shortLink) {
      return null;
   }

   public Mono<ShortLink> saveShortLink(ShortLink shortLink) {
      final String errorMessage = ShortLinkConstants.LINK_ALREADY_USED_MESSAGE;
      final String sql = "INSERT INTO short_link (user_id, original_link, shorted_link, creation_date, expiry_date, is_active) " +
         "SELECT :user_id, :original_link, :shorted_link, :creation_date, :expiry_date, :is_active " +
         "FROM dual WHERE NOT EXISTS (" +
         "   SELECT 1 FROM short_link WHERE user_id = :user_id AND original_link Like  :compare_link" +
         ")";
      return databaseClient.sql(sql)
         .bind("user_id", shortLink.getUserID())
         .bind("compare_link", ShortLinkUtil.getOriginalLinkWithoutParams(shortLink))
         .bind("original_link", shortLink.getOriginalLink())
         .bind("shorted_link", shortLink.getShortedLink())
         .bind("creation_date", shortLink.getCreationDate())
         .bind("expiry_date", shortLink.getExpiryDate())
         .bind("is_active", shortLink.isActive())
         .fetch()
         .rowsUpdated()
         .flatMap(row -> {
            if (row > 0)
               return Mono.just(shortLink);
            else
               return Mono.error(new EntityAlreadyExistException(errorMessage));
         });
   }

   public Mono<String> findOriginalLink(String shortLink) {
      return databaseClient.sql("SELECT original_link FROM short_link WHERE shorted_link = :shortLink")
         .bind("shortLink", shortLink)
         .fetch()
         .first()
         .map(row -> (String) row.get("original_link"));
   }

   public Mono<Boolean> isCustomAliasAvailable(String customAlias) {
      return databaseClient.sql("SELECT shorted_link FROM short_link WHERE shorted_link = :customAlias")
         .bind("customAlias", ShortLinkConstants.LOCALHOST_URL + customAlias)
         .fetch()
         .first().map(usedAlias -> false)
         .switchIfEmpty(Mono.just(true));
   }

   public Mono<ShortLink> createSortLinkAndUpdateIndices(ShortLink shortLink, Optional<KeyIndices> currentIndicesOpt) {
      final Mono<ShortLink> savedLink = saveShortLink(shortLink);
      final Mono<KeyIndices> updatedIndices = currentIndicesOpt
         .map(KeyGeneratorService::updateKeyIndicesIfGreater)
         .orElse(Mono.empty());

      return updatedIndices
         .flatMap(ind -> Mono.zip(savedLink, Mono.just(ind)).map(Tuple2::getT1))
         .switchIfEmpty(savedLink);
   }

   public Mono<Long> deleteShortLink(LocalDateTime now) {
      return databaseClient.sql("DELETE FROM short_link WHERE expiry_date < :now")
              .bind("now", now)
              .fetch()
              .rowsUpdated();
   }
}