package com.example.quily.services;

import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.request.KGSRequest;
import com.example.quily.response.KGSResponse;
import com.example.quily.util.KGSUtil;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public class KGSService {
    @Autowired
    KeyGeneratorDAO keyGeneratorDAO;

    @Autowired
    DbService<KeyIndices, Long> dbService;
    @Autowired
    KeyGeneratorServiceImpl serviceImpl;

    Long id = KGSUtil.KeyGeneratedIndicesId;
    private KeyIndices currentIndices;

    @PostConstruct
    public void init() {
        currentIndices = dbService.findByUniqueId(id).block();
    }

    public synchronized Mono<KGSResponseDetail> getCurrentKey() {
        if (currentIndices != null) {
            KGSResponse currentHashKey = keyGeneratorDAO.getHashKeyInKGSResponse(currentIndices);
            currentIndices = keyGeneratorDAO.getUpdatedIndices(currentIndices);
            return Mono.just(new KGSResponseDetail(currentHashKey, currentIndices));
        }
        return Mono.error(
                new ResourceNotFoundException("unable to fetch keyIndices of id: " + id + " from DB"));
    }

    public Mono<KeyIndices> saveCurrentKey(KGSRequest kgsRequest) {
        return serviceImpl.updateIfGreater(kgsRequest.toKeyIndices());
    }

    @Getter
    @AllArgsConstructor
    public static class KGSResponseDetail
    {
        private KGSResponse kgsResponse;
        private KeyIndices nextIndices;
    }

}