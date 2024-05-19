package com.example.quily.DAO;

import com.example.quily.model.KeyIndices;
import com.example.quily.response.KGSResponse;

public interface KeyGeneratorDAO {
    int [] toArray(KeyIndices keyIndices);

    void updateIndices(int [] indexArray, int currentIndex, int carry);

    KeyIndices getUpdatedIndices(KeyIndices keyIndices);

    String getSixLengthHash(KeyIndices keyIndices);

    KGSResponse getHashKeyInKGSResponse(KeyIndices keyIndices);
}
