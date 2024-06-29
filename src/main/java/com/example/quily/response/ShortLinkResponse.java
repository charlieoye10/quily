package com.example.quily.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ShortLinkResponse {
   private String userID;
   private String originalLink;
   private String shortedLink;
   private ExpiryDateResponse expiryDate;
   private String creationDate;
}

