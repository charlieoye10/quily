package com.example.quily.dao;

import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.KGSService;
import com.example.quily.services.ShortLinkService;
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

    @Autowired
    ShortLinkService shortLinkService;

    public Mono<ShortLink> createSortLink(CreateShortLinkRequest createShortLinkRequest) {
        return kgsService.getCurrentKey()
                .flatMap(tuple ->
                      shortLinkService.createSortLinkAndUpdateIndices(new ShortLink(
                                createShortLinkRequest.getUserID(),
                                createShortLinkRequest.getOriginalLink(),
                                ShortLinkUtil.localBaseUrl + tuple.getT1().getHashKey(),
                                CommonUtil.getCurrentDateTimeInFormat(),
                                createShortLinkRequest.getExpiryDate()),
                                tuple.getT2())
                );
    }

    public ShortLinkResponse mapShortLinkToShortLinkResponse(ShortLink shortLink) {
        return new ShortLinkResponse(shortLink.getUserID(),
                shortLink.getOriginalLink(),
                shortLink.getShortedLink(),
                shortLink.getExpiryDate(),
                shortLink.getCreationDate());
    }
}
