package com.example.quily.util;

import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.model.User;
import com.example.quily.response.ShortLinkResponse;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import static com.example.quily.constants.ColumnNameConstants.*;
import static com.example.quily.constants.CommonConstants.MAX_LOCAL_TIME;
import static com.example.quily.converter.ShortLinkConverter.handleExpiryDateResponse;

public class CommonUtil {
   public static String getCurrentDateTimeInFormat() {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma 'on' dd MMMM yyyy", Locale.ENGLISH);
      return LocalDateTime.now().format(formatter);
   }

   public static final String emailPatternRegex = "^[\\w.%+-]+@[A-Za-z0-9.-]+\\.com$";

   public static User parseUser(Map<String, Object> row) {
      return new User(
         (String) row.get(EMAIL),
         (String) row.get(USER_NAME),
         (String) row.get(PASSWORD),
         convertByteToBoolean(row.get(IS_ACTIVE))
      );
   }

   public static KeyIndices parseKeyIndices(Map<String, Object> row) {
      return new KeyIndices(
         (Long) (row.get(ID)),
         (Integer) row.get(INDEX1),
         (Integer) row.get(INDEX2),
         (Integer) row.get(INDEX3),
         (Integer) row.get(INDEX4),
         (Integer) row.get(INDEX5),
         (Integer) row.get(INDEX6));
   }

   public static ShortLink parseShortLink(Map<String, Object> row) {
      return new ShortLink(
         (Long) row.get(ID),
         (String) row.get(USER_EMAIL),
         (String) row.get(ORIGINAL_LINK),
         (String) row.get(SHORTED_LINK),
         convertUTCToKolkataTimeZone((ZonedDateTime) row.get(CREATION_DATE)),
         convertUTCToKolkataTimeZone((ZonedDateTime) row.get(EXPIRY_DATE)),
         convertByteToBoolean(row.get(IS_ACTIVE)),
         convertUTCToKolkataTimeZone((ZonedDateTime) row.get(UPDATED_TIME)),
         convertByteToBoolean(row.get(IS_QR_CREATED))
      );
   }

   public static EmailConfirmationToken parseEmailConfirmationToken(Map<String, Object> row) {
      return new EmailConfirmationToken(
         (String) row.get(CONFIRMATION_TOKEN),
         (String) row.get(USER_EMAIL),
         (ZonedDateTime) row.get("updated_time")
      );
   }

   private static boolean convertByteToBoolean(Object isActiveObj) {
      if (isActiveObj instanceof Boolean)
         return (Boolean) isActiveObj;
      else
         return ((Byte) isActiveObj) != 0;
   }

   public static ShortLinkResponse parseToShortLinkResponse(Map<String, Object> row) {
      return new ShortLinkResponse(
         (String) row.get(USER_EMAIL),
         (String) row.get(ORIGINAL_LINK),
         (String) row.get(SHORTED_LINK),
         handleExpiryDateResponse(convertUTCToKolkataTimeZone((ZonedDateTime) row.get(EXPIRY_DATE))),
         convertUTCToKolkataTimeZone((ZonedDateTime) row.get(CREATION_DATE)),
         convertUTCToKolkataTimeZone((ZonedDateTime) row.get(UPDATED_TIME)),
         convertByteToBoolean(row.get(IS_QR_CREATED))
      );
   }

   public static LocalDateTime convertStringToLocalTimeDate(String dateTime) {
      if (dateTime == null) {
         return LocalDateTime.parse(MAX_LOCAL_TIME, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      }
      return LocalDateTime.parse(dateTime + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
   }

   public static LocalDateTime convertUTCToKolkataTimeZone(ZonedDateTime utcDateTime) {
      if (utcDateTime == null) {
         return LocalDateTime.MIN;
      }
      return utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Kolkata"))
         .toLocalDateTime();
   }
}