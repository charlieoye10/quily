package com.example.quily.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShortLinkResponse {
    private String userEmail;
    private String originalLink;
    private String shortedLink;
    private LocalDateTime expiryDate;
    private LocalDateTime creationDate;
    private LocalDateTime updatedTime;
}
