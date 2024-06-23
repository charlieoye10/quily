package com.example.quily.dao;

import com.example.quily.converter.UserSignupConverter;
import com.example.quily.exception.EmailFormatException;
import com.example.quily.request.SignUpRequest;
import com.example.quily.services.UserService;
import com.example.quily.util.CommonUtil;
import com.example.quily.constants.UserConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Component
public class UserDao {
   private final UserService userService;
   private final UserSignupConverter userSignupConverter;

   @Autowired
   public UserDao(UserService userService, UserSignupConverter userSignupConverter) {
      this.userService = userService;
      this.userSignupConverter = userSignupConverter;
   }

   public Boolean isValidEmailFormat(String email) {
      return Pattern.compile(CommonUtil.emailPatternRegex)
         .matcher(email)
         .matches();
   }

   public Mono<String> processEmailVerification(SignUpRequest req) {
      final String errorMessage = UserConstants.INVALID_EMAIL_FORMAT_MESSAGE;
      if (!isValidEmailFormat(req.email)) {
         return Mono.error(new EmailFormatException(errorMessage));
      }
      return userService.sendEmailVerificationLink(userSignupConverter.getModelFromRequest(req));
   }
}
