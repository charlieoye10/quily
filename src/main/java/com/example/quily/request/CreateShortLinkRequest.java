package com.example.quily.request;

import lombok.Data;

@Data
public class CreateShortLinkRequest {
    private String originalLink;
    private String expiryDate;
    private String customAlias;
}