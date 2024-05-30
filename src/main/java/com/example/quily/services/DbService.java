
package com.example.quily.services;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DbService<T, I> {
    Flux<T> findAll();

    Mono<T> findByUniqueId(I id);

    Mono<T> save(T t);

    Mono<T> update(T t);

    void delete(I id);
}
