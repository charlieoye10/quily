package com.example.quily.services;
import com.example.quily.model.KeyIndices;

public interface KeyGeneratorService {
     KeyIndices getCurrentKeyIndices(Long id) throws RuntimeException;

     KeyIndices saveUpdatedIndices(KeyIndices keyIndices) throws RuntimeException;

     String getHashString(KeyIndices indices);
}
