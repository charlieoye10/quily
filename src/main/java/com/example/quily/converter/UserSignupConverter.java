package com.example.quily.converter;

import com.example.quily.model.User;
import com.example.quily.request.SignUpRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.SignUpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.quily.constants.UserConstants.*;

@Component
public class UserSignupConverter implements Converter<SignUpRequest, SignUpResponse, User> {
   private final PasswordEncoder passwordEncoder;

   @Autowired
   public UserSignupConverter(PasswordEncoder passwordEncoder) {
      this.passwordEncoder = passwordEncoder;
   }
   @Override
   public User getModelFromRequest(SignUpRequest signUpRequest) {
      return new User(
         signUpRequest.email,
         signUpRequest.userName,
         passwordEncoder.encode(signUpRequest.password),
         false);
   }

   @Override
   public ResponseBody<SignUpResponse> getResponseFromModel(User user) {
      return null;
   }

   public ResponseBody<SignUpResponse> getResponseFromModel(SignUpResponse signUpResponse) {
      if(signUpResponse.isResendMail)
         return new ResponseBody<>(200, RESEND_VERIFICATION_LINK_MESSAGE, signUpResponse);
      else
       return new ResponseBody<>(200, USER_CREATED_MESSAGE, signUpResponse);
   }

}
