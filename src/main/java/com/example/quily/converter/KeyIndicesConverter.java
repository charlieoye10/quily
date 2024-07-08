package com.example.quily.converter;

import com.example.quily.dao.KeyGeneratorDAO;
import com.example.quily.model.KeyIndices;
import com.example.quily.request.KeyGeneratorRequest;
import com.example.quily.response.KeyGeneratorResponse;
import com.example.quily.response.ResponseBody;
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
   public KeyIndices getModelFromRequest(KeyGeneratorRequest keyGeneratorRequest) {
      return new KeyIndices(keyGeneratorRequest.getId(), keyGeneratorRequest.getIndex1(), keyGeneratorRequest.getIndex2(),
         keyGeneratorRequest.getIndex3(), keyGeneratorRequest.getIndex4(), keyGeneratorRequest.getIndex5(), keyGeneratorRequest.getIndex6());
   }

   @Override
   public ResponseBody<KeyGeneratorResponse> getResponseFromModel(KeyIndices keyIndices) {
      final KeyGeneratorResponse keyGeneratorResponse =
         keyGeneratorDAO.getResponseFromKeyIndices(keyIndices);
      return new ResponseBody<>(200, keyGeneratorResponse);
   }
}
