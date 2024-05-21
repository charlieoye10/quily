package com.example.quily.DAO;

import com.example.quily.dao.KeyGeneratorDAOImpl;
import com.example.quily.model.KeyIndices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KeyGeneratorDAOImplTest {

    @InjectMocks
    private KeyGeneratorDAOImpl keyGeneratorDAO;

    @Mock
    private KeyIndices keyIndices;
    private KeyIndices keyIndices1;
    private KeyIndices keyIndices2;
    private KeyIndices keyIndices3;
    private KeyIndices keyIndices4;
    private KeyIndices keyIndices5;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(keyGeneratorDAO, "hashStringLength", 6);
        ReflectionTestUtils.setField(keyGeneratorDAO, "baseSize", 62);
        ReflectionTestUtils.setField(keyGeneratorDAO, "base62_1", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        ReflectionTestUtils.setField(keyGeneratorDAO, "base62_2", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        ReflectionTestUtils.setField(keyGeneratorDAO, "base62_3", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        ReflectionTestUtils.setField(keyGeneratorDAO, "base62_4", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        ReflectionTestUtils.setField(keyGeneratorDAO, "base62_5", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        ReflectionTestUtils.setField(keyGeneratorDAO, "base62_6", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        keyIndices = new KeyIndices(1L, 0, 1, 2, 3, 4, 5);
        keyIndices1 = new KeyIndices(1L, 0, 1, 2, 3, 4, 5);
        keyIndices2 = new KeyIndices(1L, 61, 0, 0, 0, 0, 0);
        keyIndices3 = new KeyIndices(1L, 0, 0, 0, 0, 0, 61);
        keyIndices4 = new KeyIndices(1L, 0, 0, 0, 0, 61, 61);
        keyIndices5 = new KeyIndices(1L, 0, 1, 2, 3, 4, 5);
    }

    @Test
    void getSixLengthHashShouldReturnValidString() {
        String expectedHash = "ABCDEF";
        String actualHash = keyGeneratorDAO.getSixLengthHash(keyIndices);
        assertEquals(expectedHash, actualHash);
        assertEquals(expectedHash.length(), actualHash.length());
    }

    @Test
    void getHashKeyShouldReturnSIxLength() {
        String actualHash = keyGeneratorDAO.getSixLengthHash(keyIndices);
        assertEquals(ReflectionTestUtils.getField(keyGeneratorDAO, "hashStringLength"), actualHash.length());
    }


    @Test
    public void getUpdatedIndices1() {

        KeyIndices updatedIndices = keyGeneratorDAO.getUpdatedIndices(keyIndices1);
        assertEquals(1, updatedIndices.getIndex1());
        assertEquals(1, updatedIndices.getIndex2());
        assertEquals(2, updatedIndices.getIndex3());
        assertEquals(3, updatedIndices.getIndex4());
        assertEquals(4, updatedIndices.getIndex5());
        assertEquals(5, updatedIndices.getIndex6());
    }

    @Test
    public void getUpdatedIndices2() {

        KeyIndices updatedIndices = keyGeneratorDAO.getUpdatedIndices(keyIndices2);
        assertEquals(0, updatedIndices.getIndex1());
        assertEquals(1, updatedIndices.getIndex2());
        assertEquals(0, updatedIndices.getIndex3());
        assertEquals(0, updatedIndices.getIndex4());
        assertEquals(0, updatedIndices.getIndex5());
        assertEquals(0, updatedIndices.getIndex6());
    }

    @Test
    public void getUpdatedIndices3() {

        KeyIndices updatedIndices = keyGeneratorDAO.getUpdatedIndices(keyIndices3);
        assertEquals(1, updatedIndices.getIndex1());
        assertEquals(0, updatedIndices.getIndex2());
        assertEquals(0, updatedIndices.getIndex3());
        assertEquals(0, updatedIndices.getIndex4());
        assertEquals(0, updatedIndices.getIndex5());
        assertEquals(61, updatedIndices.getIndex6());
    }

    @Test
    public void getUpdatedIndices4() {

        KeyIndices updatedIndices = keyGeneratorDAO.getUpdatedIndices(keyIndices4);
        assertEquals(1, updatedIndices.getIndex1());
        assertEquals(0, updatedIndices.getIndex2());
        assertEquals(0, updatedIndices.getIndex3());
        assertEquals(0, updatedIndices.getIndex4());
        assertEquals(61, updatedIndices.getIndex5());
        assertEquals(61, updatedIndices.getIndex6());
    }

    @Test
    public void getUpdatedIndices5() {

        KeyIndices updatedIndices = keyGeneratorDAO.getUpdatedIndices(keyIndices5);
        assertEquals(1, updatedIndices.getIndex1());
        assertEquals(1, updatedIndices.getIndex2());
        assertEquals(2, updatedIndices.getIndex3());
        assertEquals(3, updatedIndices.getIndex4());
        assertEquals(4, updatedIndices.getIndex5());
        assertEquals(5, updatedIndices.getIndex6());
    }

    @Test
    public void toArrayFromkeyIndices() {

        int[] result = keyGeneratorDAO.toArray(keyIndices);
        int[] expectedResult = {0, 1, 2, 3, 4, 5};
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void toArrayShouldReturnSIxLength() {

        int[] result = keyGeneratorDAO.toArray(keyIndices);
        assertEquals(ReflectionTestUtils.getField(keyGeneratorDAO,"hashStringLength"), result.length);
    }
}