package com.example.quily.handler;

import com.example.quily.dao.ShortLinkDAOImp;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.ShortLinkServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ShortLinkHandler {
    @Autowired
    ShortLinkServices shortLinkServices;

    public Mono<ServerResponse> createShortLink(ServerRequest serverRequest) {
        Mono<ShortLinkDAOImp> monoShortLinkRequest =
                serverRequest.bodyToMono(ShortLinkDAOImp.class);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        monoShortLinkRequest
                                .map(shortLinkServices::mapCreateShortLinkRequestToShortLink)
                                .flatMap(shortLink -> shortLinkServices.saveShortLink(shortLink))
                                .map(shortLink ->
                                        shortLinkServices.mapShortLinkToShortLinkResponse(shortLink)),
                        ShortLinkResponse.class
                );
    }
}
