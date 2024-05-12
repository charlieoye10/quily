package com.example.quily.controller;

import com.example.quily.model.KeyIndices;
import com.example.quily.services.KeyGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class KeyGeneratorController {
    final KeyGeneratorService keyGeneratorService;

    @Value("${KeyGeneratedIndicesId}")
    Long keyIndicesId;

    @Autowired
    public KeyGeneratorController(KeyGeneratorService keyGeneratorService) {
        this.keyGeneratorService = keyGeneratorService;
    }

    @GetMapping("/getHashKey")
    public ResponseEntity<String> getHashString() {
        KeyIndices indices = keyGeneratorService.getCurrentKeyIndices(keyIndicesId);
        String hashString = keyGeneratorService.getHashString(indices);
        return ResponseEntity.ok(hashString);
    }
}
