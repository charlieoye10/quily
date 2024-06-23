package com.example.quily.converter;

import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.model.KeyIndices;
import com.example.quily.request.KeyGeneratorRequest;
import com.example.quily.response.KeyGeneratorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KeyIndicesConverter implements Converter<KeyGeneratorRequest, KeyGeneratorResponse, KeyIndices> {
   private final KeyGeneratorDAO keyGeneratorDAO;

   @Autowired
   public KeyIndicesConverter(KeyGeneratorDAO keyGeneratorDAO) {
      this.keyGeneratorDAO = keyGeneratorDAO;
   }

   @Override
   public KeyIndices convertRequestToModel(KeyGeneratorRequest keyGeneratorRequest) {
      return new KeyIndices(keyGeneratorRequest.getId(), keyGeneratorRequest.getIndex1(), keyGeneratorRequest.getIndex2(),
         keyGeneratorRequest.getIndex3(), keyGeneratorRequest.getIndex4(), keyGeneratorRequest.getIndex5(), keyGeneratorRequest.getIndex6());
   }

   @Override
   public KeyGeneratorResponse convertModelToResponse(KeyIndices keyIndices) {
      return keyGeneratorDAO.getHashKeyInKGSResponse(keyIndices);
   }
}
