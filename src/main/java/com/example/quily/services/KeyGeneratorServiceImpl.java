package com.example.quily.services;

import com.example.quily.exception.GreaterIndicesFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.repositories.KGSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Component
public class KeyGeneratorServiceImpl implements DbService<KeyIndices, Long> {
    @Autowired
    KGSRepository kGSRepository;

    @Autowired
    DatabaseClient databaseClient;

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
        return kGSRepository.save(keyIndices);
    }

    @Override
    public Mono<KeyIndices> update(KeyIndices keyIndices) {
        return kGSRepository.save(keyIndices);
    }

    @Override
    public void Delete(Long id) {}

    public Mono<KeyIndices> updateIfGreater(KeyIndices newObj)
    {
        return databaseClient.sql("UPDATE key_indices SET index1 = :index1, index2 = :index2, index3 = :index3, index4 = :index4, index5 = :index5, index6 = :index6 " +
                "WHERE id = :id AND (index6 * 100000 + index5 * 10000 + index4 * 1000 + index3 * 100 + index2 * 10 + index1) < " +
                "(:index6 * 100000 + :index5 * 10000 + :index4 * 1000 + :index3 * 100 + :index2 * 10 + :index1)")
                .bind("index1", newObj.getIndex1())
                .bind("index2", newObj.getIndex2())
                .bind("index3", newObj.getIndex3())
                .bind("index4", newObj.getIndex4())
                .bind("index5", newObj.getIndex5())
                .bind("index6", newObj.getIndex6())
                .bind("id", newObj.getId())
                .fetch()
                .rowsUpdated(
                ).flatMap(
                        row -> {
                            if (row > 0)
                                return Mono.just(newObj);
                            else
                                return Mono.error(new GreaterIndicesFoundException(("Greater value already exist in db")));
                        }
                );
    }
}