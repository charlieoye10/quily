package com.example.quily.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
   private String currentPassword;
   private String newPassword;
}
