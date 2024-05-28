package com.example.quily.services;

import com.example.quily.model.ShortLink;
import com.example.quily.repositories.ShortLinkRepository;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class ShortLinkServices {
    String ShortedLink = ShortLinkUtil.ShortLink;
    @Autowired
    ShortLinkRepository shortLinkRepository;
    @Autowired
    KGSService kgsService;
    @Autowired
    DbService <ShortLink> dbService;

    public Mono<ShortLink> mapCreateShortLinkRequestToShortLink(CreateShortLinkRequest createShortLinkRequest) {
        return kgsService.getCurrentKey()
                .map(kgsResponse ->
                        new ShortLink(
                                createShortLinkRequest.getOriginalLink(),
                                ShortedLink + kgsResponse.getHashKey(),
                                LocalDateTime.now().toString(),
                                createShortLinkRequest.getExpiryDate()));
    }

    public Mono<ShortLink> saveShortLink(Mono<ShortLink> shortLink) {
        return shortLink.flatMap(dbService::save);
    }

    public ShortLinkResponse mapShortLinkToShortLinkResponse(ShortLink shortLink) {
        return new ShortLinkResponse(shortLink.getUserID(),
                shortLink.getOriginalLink(),
                shortLink.getShortedLink(),
                shortLink.getExpiryDate(),
                shortLink.getCreationDate());
    }
}
