package com.example.quily.DAO;

import com.example.quily.model.KeyIndices;
import org.springframework.stereotype.Component;

@Component
public class KeyGeneratorDAOImpl implements KeyGeneratorDAO {
    @Override
    public int[] toArray(KeyIndices keyIndices) {
        int index1 = keyIndices.getIndex1();
        int index2 = keyIndices.getIndex2();
        int index3 = keyIndices.getIndex3();
        int index4 = keyIndices.getIndex4();
        int index5 = keyIndices.getIndex5();
        int index6 = keyIndices.getIndex6();

        return new int[] {index1, index2, index3, index4, index5, index6};
    }

    @Override
    public void updateIndices(int[] indexArray, int currentIndex, int carry) {
        if (currentIndex == hashStringLength) return;
        int value = (indexArray[currentIndex] + carry) % baseSize;
        int nextCarry = (indexArray[currentIndex] + carry) / baseSize;
        indexArray[currentIndex] = value;
        updateIndices(indexArray, currentIndex + 1, nextCarry);
    }

    @Override
    public KeyIndices getUpdatedIndices(KeyIndices keyIndices) {
        int [] indexArray = toArray(keyIndices);
        updateIndices(indexArray, 0, 1);
        keyIndices.setIndex1(indexArray[0]);
        keyIndices.setIndex2(indexArray[1]);
        keyIndices.setIndex3(indexArray[2]);
        keyIndices.setIndex4(indexArray[3]);
        keyIndices.setIndex5(indexArray[4]);
        keyIndices.setIndex6(indexArray[5]);
        return keyIndices;
    }

    @Override
    public String giveSixLengthHash(KeyIndices keyIndices) {
        int [] indexArray = toArray(keyIndices);
        return "" + base62_1.charAt(indexArray[0]) +
                base62_2.charAt(indexArray[1]) +
                base62_3.charAt(indexArray[2]) +
                base62_4.charAt(indexArray[3]) +
                base62_5.charAt(indexArray[4]) +
                base62_6.charAt(indexArray[5]);
    }
}
