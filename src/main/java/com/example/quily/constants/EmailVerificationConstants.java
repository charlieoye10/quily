package com.example.quily.constants;

public class EmailVerificationConstants {
   public static final String SqlQueryToCallGetTokenProcedure = "CALL get_email_confirmation_token(:confirmation_token)";

   public static final String SqlQueryToSaveTokenProcedure =
      "CALL save_email_confirmation_token(:confirmation_token, :creation_date, :email)";

   public static final String SqlQueryToDeleteTokenProcedure = "CALL delete_email_verification_token(:confirmation_token)";
}
