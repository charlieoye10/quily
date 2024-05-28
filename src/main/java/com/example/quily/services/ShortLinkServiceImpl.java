package com.example.quily.services;

import com.example.quily.model.ShortLink;
import com.example.quily.repositories.ShortLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Component
public class ShortLinkServiceImpl implements DbService<ShortLink>{
    @Autowired
    ShortLinkRepository shortLinkRepository;

    @Override
    public Flux<ShortLink> findAll() {
        return null;
    }

    @Override
    public Mono<ShortLink> findByUniqueId(Long id) {
        return null;
    }

    @Override
    public Mono<ShortLink> save(ShortLink shortLink) {
        return shortLinkRepository.save(shortLink);
    }

    @Override
    public Mono<ShortLink> update(ShortLink shortLink) {
        return null;
    }

    @Override
    public void Delete(Long id) {}
}
