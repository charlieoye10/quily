package com.example.quily.request;

public class CreateShortLinkRequest {
    private String userID;
    private String originalLink;
    private String expiryDate;

    public String getUserID() {
        return userID;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
