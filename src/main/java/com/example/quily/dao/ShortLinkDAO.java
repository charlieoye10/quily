package com.example.quily.dao;

import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.KGSService;
import com.example.quily.services.ShortLinkServiceImpl;
import com.example.quily.util.CommonUtil;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ShortLinkDAO {
    @Autowired
    KGSService kgsService;

    @Autowired
    ShortLinkServiceImpl shortLinkServiceImpl;

    public Mono<ShortLink> mapCreateShortLinkRequestToShortLink(CreateShortLinkRequest createShortLinkRequest) {
        if(createShortLinkRequest.getCustomAlias() !=null){
            return shortLinkServiceImpl.findByCustomAlias(createShortLinkRequest)
                    .flatMap(customLink -> {
                        return kgsService.getCurrentKey()
                                .flatMap(kgsResponse -> {
                                    if (customLink) {
                                        return Mono.error(new ResourceNotFoundException("The custom alias is already present."));
                                    } else {
                                        String shortedLink = createShortLinkRequest.getCustomAlias();
                                        return Mono.just(new ShortLink(
                                                createShortLinkRequest.getUserID(),
                                                createShortLinkRequest.getOriginalLink(),
                                               ShortLinkUtil.localBaseUrl + shortedLink,
                                                CommonUtil.getCurrentDateTimeInFormat(),
                                                createShortLinkRequest.getExpiryDate()));
                                    }
                                });
                    });
        }
        else {
            return kgsService.getCurrentKey()
                    .map(kgsResponse ->
                            new ShortLink(
                                    createShortLinkRequest.getUserID(),
                                    createShortLinkRequest.getOriginalLink(),
                                    ShortLinkUtil.localBaseUrl + kgsResponse.getHashKey(),
                                    CommonUtil.getCurrentDateTimeInFormat(),
                                    createShortLinkRequest.getExpiryDate()));
        }

    }

    public ShortLinkResponse mapShortLinkToShortLinkResponse(ShortLink shortLink) {
        return new ShortLinkResponse(shortLink.getUserID(),
                shortLink.getOriginalLink(),
                shortLink.getShortedLink(),
                shortLink.getExpiryDate(),
                shortLink.getCreationDate());
    }
}
