package com.example.quily.repositories;

import com.example.quily.model.KeyIndices;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface KGSRepository extends R2dbcRepository<KeyIndices, Long> {
}
