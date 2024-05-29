package com.example.quily.handler;

import com.example.quily.dao.ShortLinkDAO;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ShortLinkHandler {
    @Autowired
    DbService <ShortLink> dbService;

    @Autowired
    ShortLinkDAO shortLinkDAO;

    public Mono<ServerResponse> createShortLink(ServerRequest serverRequest) {
        Mono<CreateShortLinkRequest> monoShortLinkRequest =
                serverRequest.bodyToMono(CreateShortLinkRequest.class);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        monoShortLinkRequest
                                .flatMap(shortLinkDAO::mapCreateShortLinkRequestToShortLink)
                                .flatMap(shortLink -> dbService.save(shortLink))
                                .map(shortLink ->
                                        shortLinkDAO.mapShortLinkToShortLinkResponse(shortLink)),
                        ShortLinkResponse.class
                );
    }
}
