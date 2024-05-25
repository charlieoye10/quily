package com.example.quily.services;

import com.example.quily.model.ShortLink;
import com.example.quily.repositories.ShortLinkRepository;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class ShortLinkServices {

    @Value("${ShortedLink}")
    String ShortedLinks;

    @Autowired
    ShortLinkRepository shortLinkRepository;
    ShortLinkResponse shortLinkResponse;
    KGSService kgsService;

    public ShortLinkServices(KGSService kgsService) {
        this.kgsService = kgsService;
    }

    public Mono<ShortLink> mapCreateShortLinkRequestToShortLink(CreateShortLinkRequest createShortLinkRequest) {
        return kgsService.getCurrentKey()
                .map(kgsResponse ->
                        new ShortLink(
                                createShortLinkRequest.getOriginalLink(),
                                ShortedLinks + kgsResponse.getHashKey(),
                                LocalDateTime.now().toString(),
                                createShortLinkRequest.getExpiryDate()));
    }


    public Mono<ShortLink> saveShortLink(Mono<ShortLink> shortLink) {
        return shortLink.flatMap(shortLinkRepository::save);
    }
    public ShortLinkResponse mapShortLinkToShortLinkResponse(ShortLink shortLink) {
        return new ShortLinkResponse(shortLink.getUserID(),shortLink.getOriginalLink(),shortLink.getShortedLink(),shortLink.getExpiryDate(),shortLink.getCreationDate());
    }
}
