package com.example.quily.constants;

public class ShortLinkConstants {
   public static final String LOCALHOST_URL = "http://localhost:8081/";
   public static final String BE_DOMAIN_URL = "https://quily.onrender.com/";
   public static final String FE_LOCALHOST_URL = "http://localhost:3000/";
   public static final String FE_LOGIN_URL = FE_LOCALHOST_URL + "login";
   public static final String EMAIL_VERIFICATION_URL = String.join("", BE_DOMAIN_URL, "api/auth/confirm-account?token=");
   public static final String PASSWORD_VERIFICATION_URL = String.join("", FE_LOCALHOST_URL, "ForgotPassword?token=");

   public static final String LINK_ALREADY_USED_MESSAGE = "Provided link already has been used.";
   public static final String SHORT_LINK_CREATED_MESSAGE = "Link shorted successfully";
   public static final String INVALID_URL_MESSAGE = "Looks like the short link is invalid";

   public static final String CUSTOM_ALIAS_EXISTS_MESSAGE = "Custom alias already exists";
   public static final String CUSTOM_ALIAS_AND_SHORT_LINK_EXISTS_MESSAGE = "Custom alias and Short Link already exists";
   public static final String CUSTOM_ALIAS_PATTERN_MESSAGE = "The characters `~,<>;\\':\"/\\\\[]^{}()=+!*@&$?%#| are not allowed";
   public static final String CUSTOM_ALIAS_PATTERN_REGEX = "^[0-9A-Za-z_-]+$";
   public static final String SHORT_LINK_DELETED_MESSAGE = "Link Deleted Successfully";
   public static final String SHORT_LINK_DOES_NOT_EXIST_MESSAGE = "Short Link does not exist";
   public static final String SHORT_LINK_DOES_NOT_PASSED = "Short link does not passed in parameter";
   public static final String SHORT_LINK_UPDATED_MESSAGE = "Short link updated successfully";

   public static final String SqlQueryToCallGetShortLinkProcedure = "CALL get_short_link_by_url(:shorted_link)";

   public static final String SqlQueryToCallCreateShortLinkProcedure =
      "CALL create_short_link(:user_email, :original_link, :shorted_link, :expiry_date, :is_active, :compare_link)";

   public static final String SqlQueryToCallDeletedShortLinkProcedure =
      "CALL delete_short_link(:user_email, :shorted_link)";
   public static final String SqlQueryToGetShortLinksProcedure = "CALL get_short_links(:page_number, :page_size, :user_email)";

   public static final String SqlQueryToUpdateShortLinkProcedure =
      "CALL update_short_link(:id, :shorted_link, :original_link)";

   public static final String SqlQueryToCallGetShortLinkByOriginalLinkAndEmailProcedure = "CALL get_short_link_by_original_link_and_email(:original_link, :user_email)";

   public static final String SqlQueryToCallGetShortLinkByOriginalLinkCustomerAliasAndEmailProcedure = "CALL get_short_link_by_original_link_custom_alias_and_email(:original_link, :shorted_link, :user_email)";
}
