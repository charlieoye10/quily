package com.example.quily.request;

import lombok.Data;

@Data
public class UpdateShortLinkRequest {
    private String shortedLink;
    private String originalLink;
    private String customAlias;
    private String userEmail;
}