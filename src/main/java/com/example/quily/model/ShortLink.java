package com.example.quily.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

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
    private LocalDateTime creationDate;
    private LocalDateTime expiryDate;
    private boolean isActive;
    private LocalDateTime  updatedTime;

    public ShortLink(
       @NonNull String userEmail,
       @NonNull String originalLink,
       @NonNull String shortedLink,
       LocalDateTime creationDate,
       LocalDateTime expiryDate,
       boolean isActive,
       LocalDateTime updatedTime
    ) {
        this.userEmail = userEmail;
        this.originalLink = originalLink;
        this.shortedLink = shortedLink;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
        this.updatedTime = updatedTime;
    }
}
