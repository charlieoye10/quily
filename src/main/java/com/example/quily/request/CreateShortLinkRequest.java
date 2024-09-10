package com.example.quily.request;

import lombok.Data;

@Data
public class CreateShortLinkRequest {
    private String originalLink;
    private String expiryDate;   // format must be yyyy-mm-dd
    private String customBackHalf;
}