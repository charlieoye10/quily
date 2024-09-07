package com.example.quily.repositories;

import com.example.quily.exception.BadRequestException;
import com.example.quily.exception.EntityAlreadyExistException;
import com.example.quily.exception.InternalServerError;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.ShortLink;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.request.UpdateShortLinkRequest;
import com.example.quily.util.CommonUtil;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.quily.constants.ColumnNameConstants.*;
import static com.example.quily.constants.CommonConstants.COMMON_INTERNAL_SERVER_MESSAGE;
import static com.example.quily.constants.ShortLinkConstants.*;
import static com.example.quily.util.CommonUtil.parseToShortLinkResponse;

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

   public Flux<ShortLinkResponse> getShortLinks(int pageSize, int pageNumber, String userEmail) {
      return client.sql(SqlQueryToGetShortLinksProcedure)
         .bind(PAGE_NUMBER, pageNumber)
         .bind(PAGE_SIZE, pageSize)
         .bind(USER_EMAIL, userEmail)
         .fetch()
         .all()
         .map(CommonUtil::parseToShortLinkResponse);
   }

   public Mono<String> updateShortLink(Long id, String shortedLink, String originalLink) {
      return client.sql(SqlQueryToUpdateShortLinkProcedure)
         .bind(ID, id)
         .bind(SHORTED_LINK, shortedLink)
         .bind(ORIGINAL_LINK, originalLink)
         .fetch()
         .rowsUpdated()
         .flatMap(result -> result > 0
            ? Mono.just(shortedLink)
            : Mono.error(new InternalServerError(COMMON_INTERNAL_SERVER_MESSAGE)));
   }

   public Mono<ShortLink> getShortLinkByOriginalLinkAndEmail(String originalLink, String email) {
      return client.sql(SqlQueryToCallGetShortLinkByOriginalLinkAndEmailProcedure)
         .bind(ORIGINAL_LINK, originalLink)
         .bind(USER_EMAIL, email)
         .fetch()
         .first()
         .map(CommonUtil::parseShortLink);
   }

   public Mono<ShortLink> getShortLinkByOriginalLinkCustomAliasAndEmail(String originalLink, String customAlias, String email) {
      return client.sql(SqlQueryToCallGetShortLinkByOriginalLinkCustomerAliasAndEmailProcedure)
         .bind(ORIGINAL_LINK, originalLink)
         .bind(SHORTED_LINK, customAlias)
         .bind(USER_EMAIL, email)
         .fetch()
         .first()
         .map(CommonUtil::parseShortLink);
   }
}
