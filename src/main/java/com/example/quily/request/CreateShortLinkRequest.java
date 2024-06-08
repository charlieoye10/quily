package com.example.quily.request;

import com.example.quily.util.ShortLinkUtil;

public class CreateShortLinkRequest {
    private String userID;
    private String originalLink;
    private String expiryDate;
    private String customAlias;

    public String getUserID() {
        return userID;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCustomAlias(){return  customAlias;}
}
