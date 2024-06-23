package com.example.quily.util;

import com.example.quily.model.ShortLink;

public class ShortLinkUtil {
    public static final String BASE_URL = "https://quily/";
    public static final String LOCALHOST_URL = "http://localhost:8081/";
    public static final String EMAIL_VERIFICATION_URL = LOCALHOST_URL + "api/auth/confirm-account?token=";

    public static final String LINK_ALREADY_USED_MESSAGE = "Provided link already has been used.";
    public static final String SHORT_LINK_CREATED_MESSAGE = "Link shorted successfully";
    public static final String INVALID_URL_MESSAGE = "Looks like the short link is invalid";
    public static final String CUSTOM_ALIAS_EXISTS_MESSAGE = "Custom alias already exists";


    public static String getOriginalLinkWithoutParams(ShortLink shortLink) {
        final String originalLink = shortLink.getOriginalLink();
        if (originalLink.contains("?")) {
            return originalLink.substring(0, originalLink.indexOf("?")) + "%";
        }
        return originalLink + "%";
    }
}
