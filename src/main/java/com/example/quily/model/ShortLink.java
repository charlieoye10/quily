package com.example.quily.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Table(name = "short_link")
public class ShortLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NonNull private String userID;
    @NonNull private String originalLink;
    @NonNull private String shortedLink;
    @NonNull private String creationDate;
    @NonNull private String expiryDate;
    private boolean active;

    public String getOriginalLinkWithoutParams() {
        if (originalLink.contains("?")){
            return originalLink.substring(0, originalLink.indexOf("?")) + "%";
        }
        return originalLink + "%";
    }
}
