package com.example.quily.dao;

import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.KGSService;
import com.example.quily.util.CommonUtil;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Component
public class ShortLinkDAO {
    @Autowired
    KGSService kgsService;

    public Mono<ShortLink> mapCreateShortLinkRequestToShortLink(CreateShortLinkRequest createShortLinkRequest) {
        return kgsService.getCurrentKey()
                .map(kgsResponse ->
                        new ShortLink(
                                createShortLinkRequest.getUserID(),
                                createShortLinkRequest.getOriginalLink(),
                                ShortLinkUtil.localBaseUrl + kgsResponse.getHashKey(),
                                CommonUtil.getCurrentDateTimeInFormat(),
                                createShortLinkRequest.getExpiryDate()));
    }

    public ShortLinkResponse mapShortLinkToShortLinkResponse(ShortLink shortLink) {
        return new ShortLinkResponse(shortLink.getUserID(),
                shortLink.getOriginalLink(),
                shortLink.getShortedLink(),
                shortLink.getExpiryDate(),
                shortLink.getCreationDate());
    }
}
