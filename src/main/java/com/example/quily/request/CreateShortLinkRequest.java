package com.example.quily.request;

import java.time.LocalDateTime;

public class CreateShortLinkRequest {
    private String userID;
    private String originalLink;
    private String expiryDate;
    private String creationDate;

    public CreateShortLinkRequest(){}

    public CreateShortLinkRequest(String userID, String expiryDate, String originalLink, String creationDate) {
        this.userID = userID;
        this.expiryDate = expiryDate;
        this.originalLink = originalLink;
        this.creationDate = creationDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public void setOriginalLink(String originalLink) {
        this.originalLink = originalLink;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

}

