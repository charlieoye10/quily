package com.example.quily.DAOTest;

import com.example.quily.dao.KeyGeneratorDAOImpl;
import com.example.quily.model.KeyIndices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KeyGeneratorDAOImplTest {

    @InjectMocks
    private KeyGeneratorDAOImpl keyGeneratorDAO;

    @Mock
    private KeyIndices keyIndices;

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
        keyIndices = new KeyIndices(1L, 0, 1, 2, 3, 4,5);
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
        assertEquals(6, actualHash.length());
    }
}