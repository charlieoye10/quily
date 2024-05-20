
package com.example.quily.services;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DbService<T> {
    Flux<T> findAll();

    Mono<T> findByUniqueId(Long id);

    Mono<T> save(T t);

    Mono<T> update(T t);

    void Delete(Long id);
}
