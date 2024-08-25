package com.example.quily.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortLinkResponse {
    private String userEmail;
    private String originalLink;
    private String shortedLink;
    private String expiryDate;
    private String creationDate;
}
