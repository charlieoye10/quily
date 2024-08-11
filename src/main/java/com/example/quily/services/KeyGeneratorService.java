package com.example.quily.services;

import com.example.quily.constants.KeyGeneratorConstants;
import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.exception.ResourceNotFoundException;
import com.example.quily.model.KeyIndices;
import com.example.quily.repositories.KeyGeneratorRepository;
import com.example.quily.response.KeyGeneratorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KeyGeneratorService {
   private final KeyGeneratorDAO keyGeneratorDAO;
   private final KeyGeneratorRepository keyGeneratorRepository;
   private final String errorMessage = KeyGeneratorConstants.KEY_NOT_FOUND_MESSAGE;

   @Autowired
   public KeyGeneratorService(KeyGeneratorDAO keyGeneratorDAO, KeyGeneratorRepository keyGeneratorRepository) {
      this.keyGeneratorDAO = keyGeneratorDAO;
      this.keyGeneratorRepository = keyGeneratorRepository;
   }

   final Long id = KeyGeneratorConstants.KeyGeneratedIndicesId;
   private KeyIndices currentIndices;

   @EventListener(ContextRefreshedEvent.class)
   public void init() {
      currentIndices = keyGeneratorRepository.getKeyIndices(id).block();
      if (currentIndices == null) {
         throw new ResourceNotFoundException(errorMessage);
      }
   }

   public synchronized Mono<KGSResponseDetail> getCurrentKey() {
      if (currentIndices != null) {
         KeyGeneratorResponse currentHashKey = keyGeneratorDAO.getResponseFromKeyIndices(currentIndices);
         currentIndices = keyGeneratorDAO.getUpdatedIndices(currentIndices);
         return Mono.just(new KGSResponseDetail(currentHashKey, currentIndices));
      }
      return Mono.error(
         new ResourceNotFoundException(errorMessage));
   }

   public Mono<KeyIndices> updateKeyIndices(KeyIndices keyIndices) {
      return keyGeneratorRepository.updateKeyIndicesSafely(
         keyIndices.getId().intValue(),
         keyIndices.getIndex1(),
         keyIndices.getIndex2(),
         keyIndices.getIndex3(),
         keyIndices.getIndex4(),
         keyIndices.getIndex5(),
         keyIndices.getIndex6()
      ).map(updatedRows -> keyIndices);
   }

   @Getter
   @AllArgsConstructor
   public static class KGSResponseDetail {
      private KeyGeneratorResponse keyGeneratorResponse;
      private KeyIndices nextIndices;
   }
}