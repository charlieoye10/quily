package com.example.quily.util;

import com.example.quily.model.ShortLink;

public class ShortLinkUtil {
    public static String getOriginalLinkWithoutParams(ShortLink shortLink) {
        final String originalLink = shortLink.getOriginalLink();
        if (originalLink.contains("?")) {
            return originalLink.substring(0, originalLink.indexOf("?")) + "%";
        }
        return originalLink + "%";
    }
}
