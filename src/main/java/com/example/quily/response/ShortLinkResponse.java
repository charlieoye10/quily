package com.example.quily.response;

public class ShortLinkResponse {
    private String userID;
    private String originalLink;
    private String shortedLink;
    private String expiryDate;
    private String creationDate;

    public ShortLinkResponse(String userID, String originalLink, String shortedLink, String expiryDate, String creationDate) {
        this.userID = userID;
        this.originalLink = originalLink;
        this.shortedLink = shortedLink;
        this.expiryDate = expiryDate;
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

    public String getShortedLink() {
        return shortedLink;
    }

    public void setShortedLink(String shortedLink) {
        this.shortedLink = shortedLink;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
