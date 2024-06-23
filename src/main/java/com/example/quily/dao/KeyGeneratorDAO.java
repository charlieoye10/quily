
package com.example.quily.dao;

import com.example.quily.model.KeyIndices;
import com.example.quily.response.KeyGeneratorResponse;
import com.example.quily.constants.KeyGeneratorConstants;
import org.springframework.stereotype.Component;

@Component
public class KeyGeneratorDAO {
    final int hashStringLength = KeyGeneratorConstants.HashStringLength;
    final int baseSize = KeyGeneratorConstants.BaseSize;
    final String base62_1 = KeyGeneratorConstants.Base62_1;
    final String base62_2 = KeyGeneratorConstants.Base62_2;
    final String base62_3 = KeyGeneratorConstants.Base62_3;
    final String base62_4 = KeyGeneratorConstants.Base62_4;
    final String base62_5 = KeyGeneratorConstants.Base62_5;
    final String base62_6 = KeyGeneratorConstants.Base62_6;

    public int[] toArray(KeyIndices keyIndices) {
        int index1 = keyIndices.getIndex1();
        int index2 = keyIndices.getIndex2();
        int index3 = keyIndices.getIndex3();
        int index4 = keyIndices.getIndex4();
        int index5 = keyIndices.getIndex5();
        int index6 = keyIndices.getIndex6();

        return new int[]{index1, index2, index3, index4, index5, index6};
    }

    public void updateIndices(int[] indexArray, int currentIndex, int carry) {
        if (currentIndex == hashStringLength) return;

        int value = (indexArray[currentIndex] + carry) % baseSize;
        int nextCarry = (indexArray[currentIndex] + carry) / baseSize;
        indexArray[currentIndex] = value;
        updateIndices(indexArray, currentIndex + 1, nextCarry);
    }

    public KeyIndices getUpdatedIndices(KeyIndices keyIndices) {
        int[] indexArray = toArray(keyIndices);
        updateIndices(indexArray, 0, 1);
        keyIndices.setIndex1(indexArray[0]);
        keyIndices.setIndex2(indexArray[1]);
        keyIndices.setIndex3(indexArray[2]);
        keyIndices.setIndex4(indexArray[3]);
        keyIndices.setIndex5(indexArray[4]);
        keyIndices.setIndex6(indexArray[5]);
        return keyIndices;
    }

    public String getSixLengthHash(KeyIndices keyIndices) {
        int[] indexArray = toArray(keyIndices);
        return "" + base62_1.charAt(indexArray[0]) +
           base62_2.charAt(indexArray[1]) +
           base62_3.charAt(indexArray[2]) +
           base62_4.charAt(indexArray[3]) +
           base62_5.charAt(indexArray[4]) +
           base62_6.charAt(indexArray[5]);
    }

    public KeyGeneratorResponse getResponseFromKeyIndices(KeyIndices keyIndices) {
        final int[] indices = toArray(keyIndices);
        final String hashKey = getSixLengthHash(keyIndices);
        return new KeyGeneratorResponse(indices, hashKey);
    }

    public KeyIndices getKeyIndicesFromResponse(KeyGeneratorResponse keyGeneratorResponse) {
        int[] indices = keyGeneratorResponse.getIndices();
        return new KeyIndices(1L, indices[0], indices[1], indices[2], indices[3], indices[4], indices[5]);
    }
}
