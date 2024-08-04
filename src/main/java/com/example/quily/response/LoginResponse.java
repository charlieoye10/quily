package com.example.quily.response;

import com.example.quily.MetaData.LoginResponseData;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
   private String token;
   private LoginResponseData user;
}
