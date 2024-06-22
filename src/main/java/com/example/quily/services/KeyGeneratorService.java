package com.example.quily.services;

import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.exception.GreaterIndicesFoundException;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.repositories.KeyGeneratorRepository;
import com.example.quily.response.KeyGeneratorResponse;
import com.example.quily.util.KeyGeneratorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class KeyGeneratorService implements DbService<KeyIndices, Long> {
   private final KeyGeneratorDAO keyGeneratorDAO;
   private final KeyGeneratorRepository keyGeneratorRepository;
   private final DatabaseClient databaseClient;

   @Autowired
   public KeyGeneratorService(KeyGeneratorDAO keyGeneratorDAO, KeyGeneratorRepository keyGeneratorRepository, DatabaseClient databaseClient) {
      this.keyGeneratorDAO = keyGeneratorDAO;
      this.keyGeneratorRepository = keyGeneratorRepository;
      this.databaseClient = databaseClient;
   }

   final Long id = KeyGeneratorUtil.KeyGeneratedIndicesId;
   private KeyIndices currentIndices;

   @EventListener(ContextRefreshedEvent.class)
   public void init() {
      currentIndices = findByUniqueId(id).block();
      if (currentIndices == null) {
         throw new ResourceNotFoundException("Unable to fetch keyIndices of id: " + id + " from DB");
      }
   }

   public synchronized Mono<KGSResponseDetail> getCurrentKey() {
      if (currentIndices != null) {
         KeyGeneratorResponse currentHashKey = keyGeneratorDAO.getHashKeyInKGSResponse(currentIndices);
         currentIndices = keyGeneratorDAO.getUpdatedIndices(currentIndices);
         return Mono.just(new KGSResponseDetail(currentHashKey, currentIndices));
      }
      return Mono.error(
         new ResourceNotFoundException("unable to fetch keyIndices of id: " + id + " from DB"));
   }

   @Override
   public Flux<KeyIndices> findAll() {
      return null;
   }

   @Override
   public Mono<KeyIndices> findByUniqueId(Long id) {
      return keyGeneratorRepository.findById(id);
   }

   @Override
   public Mono<KeyIndices> save(KeyIndices keyIndices) {
      return keyGeneratorRepository.save(keyIndices);
   }

   @Override
   public Mono<KeyIndices> update(KeyIndices keyIndices) {
      return keyGeneratorRepository.save(keyIndices);
   }

   @Override
   public void delete(Long id) {
   }

   public Mono<KeyIndices> updateIfGreater(KeyIndices keyIndices) {
      return databaseClient.sql("UPDATE key_indices SET index1 = :index1, index2 = :index2, index3 = :index3, index4 = :index4, index5 = :index5, index6 = :index6 " +
            "WHERE id = :id AND (index6 * 100000 + index5 * 10000 + index4 * 1000 + index3 * 100 + index2 * 10 + index1) < " +
            "(:index6 * 100000 + :index5 * 10000 + :index4 * 1000 + :index3 * 100 + :index2 * 10 + :index1)")
         .bind("index1", keyIndices.getIndex1())
         .bind("index2", keyIndices.getIndex2())
         .bind("index3", keyIndices.getIndex3())
         .bind("index4", keyIndices.getIndex4())
         .bind("index5", keyIndices.getIndex5())
         .bind("index6", keyIndices.getIndex6())
         .bind("id", keyIndices.getId())
         .fetch()
         .rowsUpdated(
         ).flatMap(
            row -> {
               if (row > 0)
                  return Mono.just(keyIndices);
               else
                  return Mono.error(new GreaterIndicesFoundException(("Greater value already exist in db")));
            }
         );
   }

   @Getter
   @AllArgsConstructor
   public static class KGSResponseDetail {
      private KeyGeneratorResponse keyGeneratorResponse;
      private KeyIndices nextIndices;
   }
}