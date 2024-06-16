package com.example.quily.handler;

import com.example.quily.model.KeyIndices;
import com.example.quily.request.KGSRequest;
import com.example.quily.response.ResponseBody;
import com.example.quily.services.KGSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class KGSHandler {
    @Autowired
    private final KGSService kGSService;

    public KGSHandler(KGSService kGSService) {
        this.kGSService = kGSService;
    }

    public Mono<ServerResponse> saveData(ServerRequest request) {
        Mono<KGSRequest> kgsRequest = request.bodyToMono(KGSRequest.class);
        return  kgsRequest.flatMap(kGSService::saveCurrentKey)
                .flatMap(keyIndices -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(keyIndices.successResponseBody()))
                .switchIfEmpty(ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ResponseBody<KeyIndices>(
                                400, "Greater Indices found! couldn't update key indices", null)));

    }

    public Mono<ServerResponse> getAvailableKey() {
        return kGSService.getCurrentKey()
                .flatMap(kgsResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(kgsResponse.getKgsResponse().toSuccessResponse()));

    }
}