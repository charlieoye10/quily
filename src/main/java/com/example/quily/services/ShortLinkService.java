package com.example.quily.services;

import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.request.KGSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Optional;

@Service
public class ShortLinkService {

    @Autowired
    DbService<ShortLink, Long> dbService;

    @Autowired
    KGSService kgsService;
    public Mono<ShortLink> createSortLinkAndUpdateIndices(ShortLink shortLink,  Optional<KeyIndices> currentIndicesOpt)
    {
        Mono<ShortLink> link=  dbService.save(shortLink);

        Mono<KeyIndices> indices= currentIndicesOpt.map(currentIndices ->
                kgsService.saveCurrentKey(new KGSRequest(
                currentIndices.getId(),
                currentIndices.getIndex1(),
                currentIndices.getIndex2(),
                currentIndices.getIndex3(),
                currentIndices.getIndex4(),
                currentIndices.getIndex5(),
                currentIndices.getIndex6()
        ))).orElse(Mono.empty());

        return Mono.zip(link, indices)
                .map(Tuple2::getT1);
    }
}
