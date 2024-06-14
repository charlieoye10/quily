package com.example.quily.dao;

import com.example.quily.exception.AlreadyExistEntityException;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.KeyIndices;
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
import java.util.Optional;

@Component
public class ShortLinkDAO {
    @Autowired
    KGSService kgsService;

    @Autowired
    ShortLinkServiceImpl shortLinkServiceImpl;

    public Mono<ShortLink> createShortLinkAndUpdateIndices(CreateShortLinkRequest createShortLinkRequest) {
        if (createShortLinkRequest.getCustomAlias() != null) {
            return shortLinkServiceImpl.hasCustomAliasBeenUsed(createShortLinkRequest)
                    .flatMap(usedCustomLink -> {
                        if (usedCustomLink) {
                            return Mono.error(new AlreadyExistEntityException("The custom alias is already present."));
                        }
                        return createShortLink(createShortLinkRequest, createShortLinkRequest.getCustomAlias(), Optional.empty());
                    });
        }

        return kgsService.getCurrentKey()
                    .flatMap(kgsResponseDetail -> createShortLink(createShortLinkRequest, kgsResponseDetail.getKgsResponse().getHashKey(), Optional.of(kgsResponseDetail.getNextIndices())));
    }


    private Mono<ShortLink> createShortLink(CreateShortLinkRequest createShortLinkRequest, String alias, Optional<KeyIndices> keyIndicesOpt) {
        ShortLink shortLink = new ShortLink(
                createShortLinkRequest.getUserID(),
                createShortLinkRequest.getOriginalLink(),
                ShortLinkUtil.localBaseUrl + alias,
                LocalDateTime.now().toString(),
                createShortLinkRequest.getExpiryDate());

        return shortLinkServiceImpl.createSortLinkAndUpdateIndices(shortLink, keyIndicesOpt);
    }

    public ShortLinkResponse mapShortLinkToShortLinkResponse(ShortLink shortLink) {
        return new ShortLinkResponse(shortLink.getUserID(),
                shortLink.getOriginalLink(),
                shortLink.getShortedLink(),
                shortLink.getExpiryDate(),
                shortLink.getCreationDate());
    }
}
