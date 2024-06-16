package com.example.quily.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortLinkResponse {
    private String userID;
    private String originalLink;
    private String shortedLink;
    private String expiryDate;
    private String creationDate;

    public ResponseBody<ShortLinkResponse> toSuccessResponse() {
        return new ResponseBody<>(200, "Successfully short link created.", this);
    }
}
