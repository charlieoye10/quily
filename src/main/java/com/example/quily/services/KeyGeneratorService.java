package com.example.quily.services;
import com.example.quily.DAO.KeyGeneratorDAO;
import com.example.quily.DAO.KeyGeneratorDAOImpl;
import com.example.quily.model.KeyIndices;

import java.util.List;

public interface KeyGeneratorService {
     List<KeyIndices> getIndices();

     KeyIndices saveUpdatedIndices(KeyIndices keyIndices);

     KeyIndices getCurrentKeyIndices();

     KeyIndices createBeforeSave();

    String getHashString(KeyIndices indices);
}
