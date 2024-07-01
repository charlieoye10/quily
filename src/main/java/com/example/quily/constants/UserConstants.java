package com.example.quily.constants;

public class UserConstants {
   public static final String USER_CREATED_MESSAGE = "User registered successfully";

   public static final String INVALID_EMAIL_FORMAT_MESSAGE =
      "Email format is not acceptable. Please provide an email with the correct format, e.g., user_name@(gmail or domain_name).com";

   public static final String VERIFICATION_FAILED_MESSAGE = "Email verification failed, token did not match either it was invalid or empty";

   public static final String EMAIL_VERIFICATION_MESSAGE = "Email verification link sent on email %s please verify it to click it." +
      " If you didn't get verification link please check your email";

   public static final String SqlQueryToCallGetUserProcedure = "CALL get_user_by_email(:email)";

   public static final String SqlQueryToCallCreateUserProcedure = "CALL create_user(:email, :user_name, :password, :is_active)";

   public static final String SqlQueryToCallMakeUserActiveProcedure = "CALL make_user_active(:email)";

}
