package com.example.quily.model;

import jakarta.persistence.*;

@Entity
@Table(name = "short_link")
public class ShortLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String userID;
    private String originalLink;
    private String shortedLink;
    private String creationDate;
    private String expiryDate;
    private boolean active;

    public ShortLink(){}

    public ShortLink(String userID,String originalLink, String shortedLink, String creationDate, String expiryDate) {
        this.userID =  userID;
        this.originalLink = originalLink;
        this.shortedLink = shortedLink;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
        this.active = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "ShortLink{" +
                "id=" + id +
                ", userID='" + userID + '\'' +
                ", originalLink='" + originalLink + '\'' +
                ", shortedLink='" + shortedLink + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", active=" + active +
                '}';
    }
}
