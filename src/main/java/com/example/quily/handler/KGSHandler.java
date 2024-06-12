package com.example.quily.handler;

import com.example.quily.exception.ErrorResponse;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.request.KGSRequest;
import com.example.quily.services.KGSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
                .flatMap(kgsResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(kgsResponse))
                .onErrorResume(ResourceNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse("Internal Server Error with error message: ", e.getMessage()) {
                                }));

    }

    public Mono<ServerResponse> getAvailableKey() {
        return kGSService.getCurrentKey()
                .flatMap(kgsResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(kgsResponse.getKgsResponse()))
                .onErrorResume(ResourceNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse("Internal Server Error with error message: ", e.getMessage()) {
                                }));

    }
}