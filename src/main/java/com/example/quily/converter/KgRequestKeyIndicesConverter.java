package com.example.quily.converter;

import com.example.quily.model.KeyIndices;
import com.example.quily.request.KeyGeneratorRequest;
import org.springframework.stereotype.Component;

@Component
public class KgRequestKeyIndicesConverter implements Converter<KeyGeneratorRequest, KeyIndices> {

   @Override
   public KeyIndices convertFrom(KeyGeneratorRequest keyGeneratorRequest) {
      return new KeyIndices(keyGeneratorRequest.getId(), keyGeneratorRequest.getIndex1(), keyGeneratorRequest.getIndex2(),
         keyGeneratorRequest.getIndex3(), keyGeneratorRequest.getIndex4(), keyGeneratorRequest.getIndex5(), keyGeneratorRequest.getIndex6());
   }

   @Override
   public KeyGeneratorRequest convertTo(KeyIndices keyIndices) {
      return new KeyGeneratorRequest(keyIndices.getId(), keyIndices.getIndex1(), keyIndices.getIndex2(),
         keyIndices.getIndex3(), keyIndices.getIndex4(), keyIndices.getIndex5(), keyIndices.getIndex6());
   }
}
