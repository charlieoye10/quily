package com.example.quily.controller;

import com.example.quily.DAO.KeyGeneratorDAO;
import com.example.quily.model.KeyIndices;
import com.example.quily.services.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class KeyGeneratorController {
    @Autowired
    DbService<KeyIndices> dbService;

    @Autowired
    KeyGeneratorDAO keyGeneratorDAO;

    @Value("${KeyGeneratedIndicesId}")
    Long keyIndicesId;

    @GetMapping("/getHashKey")
    public ResponseEntity<String> getHashString() {
        KeyIndices indices = dbService.findById(keyIndicesId);
        String hashString = keyGeneratorDAO.giveSixLengthHash(indices);
        return ResponseEntity.ok(hashString);
    }
}