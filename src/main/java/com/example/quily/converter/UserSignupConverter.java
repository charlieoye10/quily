package com.example.quily.converter;

import com.example.quily.model.User;
import com.example.quily.request.SignUpRequest;
import com.example.quily.response.SignUpResponse;
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
   public SignUpResponse convertModelToResponse(User user) {
      return new SignUpResponse(
         user.getEmail(),
         LocalDateTime.now().toString(),
         ""
      );
   }
}
