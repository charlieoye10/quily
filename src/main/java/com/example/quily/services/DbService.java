package com.example.quily.services;
import com.example.quily.model.KeyIndices;

import java.util.List;

public interface DbService<T> {
    List<T> findAll();

    T findById(Long id);

    T save(T t);

    T update(T t);

    void Delete(Long id);
}