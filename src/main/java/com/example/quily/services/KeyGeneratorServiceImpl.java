package com.example.quily.services;

import com.example.quily.DAO.KeyGeneratorDAO;
import com.example.quily.Exception.ResourceNotFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.repositories.KGSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Component
public class KeyGeneratorServiceImpl implements DbService<KeyIndices> {
    @Autowired
    KeyGeneratorDAO keyGeneratorDAO;
    @Autowired
    KGSRepository kGSRepository;

    @Override
    public Flux<KeyIndices> findAll() {
        return null;
    }

    @Override
    public Mono<KeyIndices> findByUniqueId(Long id) {
        return kGSRepository.findById(id);
    }

    @Override
    public Mono<KeyIndices> save(KeyIndices keyIndices) {
        return null;
    }

    @Override
    public Mono<KeyIndices> update(KeyIndices keyIndices) {
        return kGSRepository.save(keyIndices);
    }

    @Override
    public void Delete(Long id) {}
}