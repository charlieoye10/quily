package com.example.quily.Repositories;

import com.example.quily.model.KeyIndices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyGeneratorRepository extends JpaRepository<KeyIndices, Long> {}
