package com.example.quily.services;

import com.example.quily.DAO.KeyGeneratorDAO;
import com.example.quily.ExceptionHandler.ResourceNotFoundException;
import com.example.quily.Repositories.KeyGeneratorRepository;
import com.example.quily.model.KeyIndices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Component
public class KeyGeneratorServiceImpl implements DbService<KeyIndices> {
    @Autowired
    KeyGeneratorDAO keyGeneratorDAO;
    @Autowired
    KeyGeneratorRepository keyGeneratorRepository;

    @Override
    public List<KeyIndices> findAll() {
        return null;
    }

    @Override
    public KeyIndices findById(Long id) {
        Optional<KeyIndices> key = keyGeneratorRepository.findById(id);
        KeyIndices currentIndices;
        if (key.isEmpty()) {
            throw new ResourceNotFoundException("key indices with id :" + id + " is not present in DB or unable to fetch in DB");
        } else currentIndices = key.get();

        KeyIndices nextIndices = keyGeneratorDAO.getUpdatedIndices(currentIndices);
        save(nextIndices);
        return currentIndices;
    }

    @Override
    public KeyIndices save(KeyIndices keyIndices) {
        return null;
    }

    @Override
    public KeyIndices update(KeyIndices keyIndices) {
        Optional<KeyIndices> keyOpt = Optional.of(keyGeneratorRepository.save(keyIndices));
        if (keyOpt.isEmpty()) {
            throw new RuntimeException("Unable to update in DB");
        }
        return keyOpt.get();
    }

    @Override
    public void Delete(Long id) {}
}