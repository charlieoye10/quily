package com.example.quily.repositories;

import com.example.quily.model.ShortLink;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ShortLinkRepository extends R2dbcRepository<ShortLink, Long> {
}
