package com.example.quily.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignUpResponse {
   public String email;
   public String creationTime;
   public Boolean isResendMail;
}
