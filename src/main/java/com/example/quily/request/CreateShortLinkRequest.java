package com.example.quily.request;

import lombok.Data;
import lombok.Getter;

@Getter
public class CreateShortLinkRequest {
   private String userID;
   private String originalLink;
   private ExpiryDateRequest expiryDate;
   private String customAlias;
}