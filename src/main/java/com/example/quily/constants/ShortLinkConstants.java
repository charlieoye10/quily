package com.example.quily.constants;

public class ShortLinkConstants {
   public static final String BASE_URL = "https://quily/";
   public static final String LOCALHOST_URL = "http://localhost:8081/";
   public static final String FE_LOCALHOST_URL = "http://localhost:3000/";
   public static final String EMAIL_VERIFICATION_URL = String.join("", LOCALHOST_URL, "api/auth/confirm-account?token=");
   public static final String PASSWORD_VERIFICATION_URL = String.join("", FE_LOCALHOST_URL, "api/auth/confirm-forgot-password?token=");

   public static final String LINK_ALREADY_USED_MESSAGE = "Provided link already has been used.";
   public static final String SHORT_LINK_CREATED_MESSAGE = "Link shorted successfully";
   public static final String INVALID_URL_MESSAGE = "Looks like the short link is invalid";

   public static final String CUSTOM_ALIAS_EXISTS_MESSAGE = "Custom alias already exists";
   public static final String CUSTOM_ALIAS_PATTERN_MESSAGE = "The characters `~,<>;\\':\"/\\\\[]^{}()=+!*@&$?%#| are not allowed";
   public static final String CUSTOM_ALIAS_PATTERN_REGEX = "^[0-9A-Za-z_-]+$";
   public static final String SHORT_LINK_DELETED_MESSAGE = "Link Deleted Successfully";
   public static final String SHORT_LINK_DOES_NOT_EXIST_MESSAGE = "Short Link does not exist";

   public static final String SqlQueryToCallGetShortLinkProcedure = "CALL get_short_link_by_url(:shorted_link)";

   public static final String SqlQueryToCallCreateShortLinkProcedure =
      "CALL create_short_link(:user_email, :original_link, :shorted_link, :creation_date, :expiry_date, :is_active, :compare_link)";

   public static final String SqlQueryToCallDeletedShortLinkProcedure =
      "CALL delete_short_link(:user_email, :shorted_link)";
}
