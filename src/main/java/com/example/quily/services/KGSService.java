package com.example.quily.services;

import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.response.KGSResponse;
import com.example.quily.util.KGSUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KGSService {
    @Autowired
    KeyGeneratorDAO keyGeneratorDAO;

    @Autowired
    DbService<KeyIndices> dbService;

    Long id = KGSUtil.KeyGeneratedIndicesId;
    private KeyIndices currentIndices;

    @PostConstruct
    public void init() {
        currentIndices = dbService.findByUniqueId(id).block();
    }

    public synchronized Mono<KGSResponse> getCurrentKey() {
        if (currentIndices != null) {
            KGSResponse currentHashKey = keyGeneratorDAO.getHashKeyInKGSResponse(currentIndices);
            currentIndices = keyGeneratorDAO.getUpdatedIndices(currentIndices);
            return Mono.just(currentHashKey);
        }
        return Mono.error(
                new ResourceNotFoundException("unable to fetch keyIndices of id: " + id + " from DB"));
    }
}