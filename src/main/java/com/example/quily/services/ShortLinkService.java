package com.example.quily.services;

import com.example.quily.model.KeyIndices;
import com.example.quily.model.ShortLink;
import com.example.quily.request.KGSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class ShortLinkService {

    @Autowired
    DbService<ShortLink, Long> dbService;

    @Autowired
    KGSService kgsService;
    public Mono<ShortLink> createSortLinkAndUpdateIndices(ShortLink shortLink, KeyIndices currentIndices)
    {
        Mono<ShortLink> link=  dbService.save(shortLink);

        Mono<KeyIndices> indices= kgsService.saveCurrentKey(new KGSRequest(
                currentIndices.getId(),
                currentIndices.getIndex1(),
                currentIndices.getIndex2(),
                currentIndices.getIndex3(),
                currentIndices.getIndex4(),
                currentIndices.getIndex5(),
                currentIndices.getIndex6()
        ));

        return Mono.zip(link, indices)
                .map(Tuple2::getT1);
    }
}
