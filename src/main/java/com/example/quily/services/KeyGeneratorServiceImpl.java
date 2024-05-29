package com.example.quily.services;

import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.exception.GreaterIndicesFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.query.SQLQueries;
import com.example.quily.repositories.KGSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
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

    @Autowired
    DatabaseClient databaseClient;

    @Autowired
    SQLQueries query;

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

    public Mono<KeyIndices> updateIfGreater(Long id, KeyIndices newObj)
    {
        return databaseClient.sql(query.updateIndicesIfIndicesIsGreater())
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
                            if (row > 0) {
                                return Mono.just(newObj);
                            }
                            else {
                                return Mono.error(new GreaterIndicesFoundException(("Greater value already exist in db")));
                            }
                        }
                );
    }
}