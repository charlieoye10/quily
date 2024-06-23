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
    private long id;
    @NonNull private String userID;
    @NonNull private String originalLink;
    @NonNull private String shortedLink;
    @NonNull private String creationDate;
    @NonNull private String expiryDate;
    @NonNull private boolean isActive;
}
