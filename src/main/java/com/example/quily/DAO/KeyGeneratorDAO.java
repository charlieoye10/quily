package com.example.quily.DAO;

import com.example.quily.model.KeyIndices;
import org.springframework.beans.factory.annotation.Value;

public interface KeyGeneratorDAO {
    int [] toArray(KeyIndices keyIndices);

    void updateIndices(int [] indexArray, int currentIndex, int carry);

    KeyIndices getUpdatedIndices(KeyIndices keyIndices);

    String giveSixLengthHash(KeyIndices keyIndices);
}
