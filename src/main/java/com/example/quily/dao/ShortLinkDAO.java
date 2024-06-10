package com.example.quily.dao;

import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.KGSService;
import com.example.quily.services.ShortLinkServiceImpl;
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
    ShortLinkServiceImpl shortLinkServiceImpl;

    public Mono<ShortLink> mapCreateShortLinkRequestToShortLink(CreateShortLinkRequest createShortLinkRequest) {
        if (createShortLinkRequest.getCustomAlias() != null) {
            return shortLinkServiceImpl.hasCustomAliasBeenUsed(createShortLinkRequest)
                    .flatMap(usedCustomLink -> {
                        if (usedCustomLink) {
                            return Mono.error(new ResourceNotFoundException("The custom alias is already present."));
                        }
                        return Mono.just(createShortLink(createShortLinkRequest, createShortLinkRequest.getCustomAlias()));
                    });
        }
        return kgsService.getCurrentKey()
                    .map(kgsResponse -> createShortLink(createShortLinkRequest, kgsResponse.getHashKey()));
    }
    private ShortLink createShortLink(CreateShortLinkRequest createShortLinkRequest, String alias) {
        return new ShortLink(
                createShortLinkRequest.getUserID(),
                createShortLinkRequest.getOriginalLink(),
                ShortLinkUtil.localBaseUrl + alias,
                LocalDateTime.now().toString(),
                createShortLinkRequest.getExpiryDate());
    }

    public ShortLinkResponse mapShortLinkToShortLinkResponse(ShortLink shortLink) {
        return new ShortLinkResponse(shortLink.getUserID(),
                shortLink.getOriginalLink(),
                shortLink.getShortedLink(),
                shortLink.getExpiryDate(),
                shortLink.getCreationDate());
    }
}
