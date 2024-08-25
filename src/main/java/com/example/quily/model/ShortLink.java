package com.example.quily.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table(name = "short_link")
public class ShortLink {
    @Id
    @NonNull
    private long id;
    @NonNull private String userEmail;
    @NonNull private String originalLink;
    @NonNull private String shortedLink;
    private String creationDate;
    private String expiryDate;
    private boolean isActive;

    public ShortLink(
       @NonNull String userEmail,
       @NonNull String originalLink,
       @NonNull String shortedLink,
       String creationDate,
       String expiryDate,
       boolean isActive
    ) {
        this.userEmail = userEmail;
        this.originalLink = originalLink;
        this.shortedLink = shortedLink;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }
}
