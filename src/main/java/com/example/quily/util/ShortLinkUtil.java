package com.example.quily.util;

import com.example.quily.model.ShortLink;

public class ShortLinkUtil {
   public static final String BASE_URL = "https://quily/";
   public static final String LOCALHOST_URL = "http://localhost:8081/";
   public static final String EMAIL_VERIFICATION_URL = LOCALHOST_URL + "api/auth/confirm-account?token=";
   public static final int Delay = 300000;

   public static String getOriginalLinkWithoutParams(ShortLink shortLink) {
      final String originalLink = shortLink.getOriginalLink();
      if (originalLink.contains("?")) {
         return originalLink.substring(0, originalLink.indexOf("?")) + "%";
      }
      return originalLink + "%";
   }
}
