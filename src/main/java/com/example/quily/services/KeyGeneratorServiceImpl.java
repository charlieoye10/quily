package com.example.quily.services;

import com.example.quily.DAO.KeyGeneratorDAO;
import com.example.quily.Repositories.KeyGeneratorRepository;
import com.example.quily.model.KeyIndices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class KeyGeneratorServiceImpl implements KeyGeneratorService {
    KeyGeneratorDAO keyGeneratorDAO;
    KeyGeneratorRepository keyGeneratorRepository;

    @Autowired
    public KeyGeneratorServiceImpl(KeyGeneratorDAO keyGeneratorDAO, KeyGeneratorRepository keyGeneratorRepository) {
        this.keyGeneratorDAO = keyGeneratorDAO;
        this.keyGeneratorRepository = keyGeneratorRepository;
    }

    @Override
    public List<KeyIndices> getIndices() {
        try {
            return keyGeneratorRepository.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("getting error while fetching indices in DB with message: " + ex.getMessage());
        }
    }

    @Override
    public KeyIndices saveUpdatedIndices(KeyIndices keyIndices) {
       try {
           return keyGeneratorRepository.save(keyIndices);
       } catch (Exception ex) {
           throw new RuntimeException("getting error while saving updated indices in DB with message: " + ex.getMessage());
       }
    }

    @Override
    public KeyIndices createBeforeSave() {
        KeyIndices initialIndices = new KeyIndices(1L, 0, 0, 0, 0, 0, 0);
        return saveUpdatedIndices(initialIndices);
    }

    @Override
    public KeyIndices getCurrentKeyIndices() {
        List<KeyIndices> indices = getIndices();
        if (indices.isEmpty()) {
            return createBeforeSave();
        }

        KeyIndices currentIndices = indices.get(0);
        KeyIndices nextIndices = keyGeneratorDAO.getUpdatedIndices(currentIndices);
        saveUpdatedIndices(nextIndices);
        return  currentIndices;
    }

    @Override
    public String getHashString(KeyIndices indices) {
        return keyGeneratorDAO.giveSixLengthHash(indices);
    }
}
