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

@Repository
public class ShortLinkRepository {
   private final DatabaseClient client;
   private static final String SqlQueryToCallGetShortLinkProcedure = "CALL get_short_link_by_url(:shortLink)";
   private static final String SqlQueryToCallCreateShortLinkProcedure =
      "CALL create_short_link(:user_id, :original_link, :shorted_link, :creation_date, :expiry_date, :is_active, :compare_link)";

   @Autowired
   public ShortLinkRepository(DatabaseClient client) {
      this.client = client;
   }

   public Mono<ShortLink> getShortLink(String shortLink) {
      return client.sql(SqlQueryToCallGetShortLinkProcedure)
         .bind("shortLink", shortLink)
         .fetch()
         .first()
         .map(CommonUtil::parseShortLink);
   }

   public Mono<ShortLink> createShortLink(ShortLink shortLink) {
      final String errorMessage = ShortLinkConstants.LINK_ALREADY_USED_MESSAGE;
      return client.sql(SqlQueryToCallCreateShortLinkProcedure)
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

   private boolean convertByteToBoolean(Object isActiveObj) {
      if (isActiveObj instanceof Boolean)
         return (Boolean) isActiveObj;
      else
         return ((Byte) isActiveObj) != 0;
   }
}
