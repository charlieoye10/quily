package com.example.quily.util;

import com.example.quily.model.EmailConfirmationToken;
import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.model.User;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class CommonUtil {
   public static String getCurrentDateTimeInFormat() {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma 'on' dd MMMM yyyy", Locale.ENGLISH);
      return LocalDateTime.now().format(formatter);
   }

   public static final String emailPatternRegex = "^[\\w.%+-]+@[A-Za-z0-9.-]+\\.com$";

   public static User parseUser(Map<String, Object> row) {
      return new User(
         (String) row.get("email"),
         (String) row.get("user_name"),
         (String) row.get("password"),
         convertByteToBoolean(row.get("is_active"))
      );
   }

   public static KeyIndices parseKeyIndices(Map<String, Object> row) {
      return new KeyIndices(
         (Long) (row.get("id")),
         (Integer) row.get("index1"),
         (Integer) row.get("index2"),
         (Integer) row.get("index3"),
         (Integer) row.get("index4"),
         (Integer) row.get("index5"),
         (Integer) row.get("index6"));
   }

   public static ShortLink parseShortLink(Map<String, Object> row) {
      return new ShortLink(
         (String) row.get("user_email"),
         (String) row.get("original_link"),
         (String) row.get("shorted_link"),
         (String) row.get("created_time"),
         (String) row.get("expiry_date"),
         convertByteToBoolean(row.get("is_active"))
      );
   }

   public static EmailConfirmationToken parseEmailConfirmationToken(Map<String, Object> row) {
      Map<String, Object> x = row;
      return new EmailConfirmationToken(
         (String) row.get("confirmation_token"),
         (String) row.get("user_email"),
         (ZonedDateTime) row.get("updated_time")
      );
   }

   private static boolean convertByteToBoolean(Object isActiveObj) {
      if (isActiveObj instanceof Boolean)
         return (Boolean) isActiveObj;
      else
         return ((Byte) isActiveObj) != 0;
   }
}