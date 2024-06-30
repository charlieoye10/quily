package com.example.quily.repositories;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.exception.EntityAlreadyExistException;
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
         .bind(USER_ID, shortLink.getUserID())
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
}
