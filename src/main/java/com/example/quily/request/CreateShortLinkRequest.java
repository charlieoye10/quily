package com.example.quily.request;

public class CreateShortLinkRequest {
    private String userID;
    private String originalLink;
    private String expiryDate;

    public String getUserID() {
        return userID;
    }

    public String getOriginalLink() {
        if (originalLink.contains("?")){
            return originalLink.substring(0, originalLink.indexOf("?"));
        }
        return originalLink;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
