package com.example.quily.repositories;

import com.example.quily.exception.BadRequestException;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.ShortLink;
import com.example.quily.util.CommonUtil;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static com.example.quily.constants.ColumnNameConstants.*;
import static com.example.quily.constants.ShortLinkConstants.*;

@Repository
public class ShortLinkRepository {
   private final DatabaseClient client;

   @Autowired
   public ShortLinkRepository(DatabaseClient client) {
      this.client = client;
   }

   public Mono<ShortLink> getShortLink(String shortLink) {
      return client.sql(SqlQueryToCallGetShortLinkProcedure)
         .bind(SHORTED_LINK, shortLink)
         .fetch()
         .first()
         .map(CommonUtil::parseShortLink);
   }

   public Mono<ShortLink> createShortLink(ShortLink shortLink) {
      return client.sql(SqlQueryToCallCreateShortLinkProcedure)
         .bind(USER_EMAIL, shortLink.getUserEmail())
         .bind(COMPARE_LINK, ShortLinkUtil.getOriginalLinkWithoutParams(shortLink))
         .bind(ORIGINAL_LINK, shortLink.getOriginalLink())
         .bind(SHORTED_LINK, shortLink.getShortedLink())
         .bind(CREATION_DATE, shortLink.getCreationDate())
         .bind(EXPIRY_DATE, shortLink.getExpiryDate())
         .bind(IS_ACTIVE, shortLink.isActive())
         .fetch()
         .rowsUpdated()
         .flatMap(row -> {
            if (row > 0)
               return Mono.just(shortLink);
            else
               return Mono.error(new EntityAlreadyExistException(LINK_ALREADY_USED_MESSAGE));
         });
   }

   public Mono<String> deleteShortLink(String shortedLink, String userEmail) {
      return client.sql(SqlQueryToCallDeletedShortLinkProcedure)
         .bind(SHORTED_LINK, shortedLink)
         .bind(USER_EMAIL, userEmail)
         .fetch()
         .rowsUpdated()
         .flatMap(rowsUpdated ->
            rowsUpdated > 0
               ? Mono.just(SHORT_LINK_DELETED_MESSAGE)
               : Mono.error(new ResourceNotFoundException(SHORT_LINK_DOES_NOT_EXIST_MESSAGE))
         );
   }

}
