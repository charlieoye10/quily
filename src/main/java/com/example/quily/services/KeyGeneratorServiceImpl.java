package com.example.quily.services;

import com.example.quily.DAO.KeyGeneratorDAO;
import com.example.quily.ExceptionHandler.ResourceNotFoundException;
import com.example.quily.Repositories.KeyGeneratorRepository;
import com.example.quily.model.KeyIndices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Component
public class KeyGeneratorServiceImpl implements KeyGeneratorService {
    @Autowired
    KeyGeneratorDAO keyGeneratorDAO;
    @Autowired
    KeyGeneratorRepository keyGeneratorRepository;

    @Override
    public KeyIndices saveUpdatedIndices(KeyIndices keyIndices) throws RuntimeException {
       Optional<KeyIndices> keyOpt = Optional.of(keyGeneratorRepository.save(keyIndices));
       if (keyOpt.isEmpty()) {
           throw new RuntimeException("Unable to update in DB");
       }
       return keyOpt.get();
    }

    public KeyIndices processCreateBeforeSave(Long id) throws RuntimeException {
        KeyIndices initialIndices = new KeyIndices(id, 0, 0, 0, 0, 0, 0);
        return saveUpdatedIndices(initialIndices);
    }

    @Override
    public KeyIndices getCurrentKeyIndices(Long id) throws RuntimeException {
        Optional<KeyIndices> key = keyGeneratorRepository.findById(id);
        Boolean isReallyNoAnyKeyPresent = true; // need to handle *********
        KeyIndices currentIndices;
        if (key.isEmpty() && isReallyNoAnyKeyPresent) {
            currentIndices = processCreateBeforeSave(id);
        } else if (key.isEmpty()) {
            throw new ResourceNotFoundException("key indices with id :" + id + " is not present in DB or unable to fetch in DB");
        } else currentIndices = key.get();

        KeyIndices nextIndices = keyGeneratorDAO.getUpdatedIndices(currentIndices);
        saveUpdatedIndices(nextIndices);
        return currentIndices;
    }

    @Override
    public String getHashString(KeyIndices indices) {
        return keyGeneratorDAO.giveSixLengthHash(indices);
    }
}
