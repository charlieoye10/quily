package com.example.quily.converter;

import com.example.quily.model.User;
import com.example.quily.request.SignUpRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.SignUpResponse;
import com.example.quily.util.ShortLinkUtil;
import com.example.quily.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserSignupConverter implements Converter<SignUpRequest, SignUpResponse, User> {
   private final PasswordEncoder passwordEncoder;

   @Autowired
   public UserSignupConverter(PasswordEncoder passwordEncoder) {
      this.passwordEncoder = passwordEncoder;
   }
   @Override
   public User convertRequestToModel(SignUpRequest signUpRequest) {
      return new User(
         signUpRequest.email,
         signUpRequest.userName,
         passwordEncoder.encode(signUpRequest.password),
         false);
   }

   @Override
   public ResponseBody<SignUpResponse> convertModelToResponse(User user) {
      return null;
   }

   public ResponseBody<SignUpResponse> convertModelToResponse(SignUpResponse signUpResponse) {
      final String message = UserUtil.USER_CREATED_MESSAGE;
      return new ResponseBody<>(200, message, signUpResponse);
   }
}
