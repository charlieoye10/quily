package com.example.quily.handler;

import com.example.quily.dao.ShortLinkDAO;
import com.example.quily.model.ShortLink;
import com.example.quily.request.CreateShortLinkRequest;
import com.example.quily.response.OriginalLinkResponse;
import com.example.quily.response.ResponseBody;
import com.example.quily.response.ShortLinkResponse;
import com.example.quily.services.DbService;
import com.example.quily.services.ShortLinkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ShortLinkHandler {
    @Autowired
    DbService<ShortLink, Long> dbService;

    @Autowired
    ShortLinkDAO shortLinkDAO;

    @Autowired
    ShortLinkServiceImpl shortLinkService;

    public Mono<ServerResponse> createShortLink(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateShortLinkRequest.class)
                .flatMap(req ->
                        shortLinkDAO.createShortLinkAndUpdateIndices(req)
                                .map(shortLink -> shortLinkDAO.mapShortLinkToShortLinkResponse(shortLink).toSuccessResponse())
                                .flatMap(response -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(response)));
    }


    public Mono<ServerResponse> getOriginalLink(ServerRequest serverRequest) {
        return shortLinkService.findOriginalLink(serverRequest.uri().toString())
                .flatMap(originalLink -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(originalLink.toSuccessResponse()))
                .switchIfEmpty(ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(
                                new ResponseBody<OriginalLinkResponse>(400, "Looks like the short link is invalid", null)
                        ));
    }
}
