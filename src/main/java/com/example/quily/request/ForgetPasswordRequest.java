package com.example.quily.request;

import lombok.Getter;

@Getter
public class ForgetPasswordRequest {
   private String password;
   private String token;
}
