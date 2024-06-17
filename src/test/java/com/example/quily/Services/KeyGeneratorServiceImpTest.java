package com.example.quily.Services;

import com.example.quily.Conf.TestConfig;
import com.example.quily.model.KeyIndices;
import com.example.quily.services.KeyGeneratorServiceImpl;
import com.example.quily.exception.GreaterIndicesFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataR2dbcTest
@Import({KeyGeneratorServiceImpl.class, TestConfig.class})
public class KeyGeneratorServiceImpTest {

    @Autowired
    private KeyGeneratorServiceImpl keyGeneratorService;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {

        databaseClient.sql("DROP TABLE IF EXISTS key_indices;").then().block();

        databaseClient.sql("CREATE TABLE key_indices (id SERIAL PRIMARY KEY, index1 INT, index2 INT, index3 INT, index4 INT, index5 INT, index6 INT);").then().block();

        KeyIndices existingObj = new KeyIndices(1L, 1, 1, 1, 1, 1, 1);

        databaseClient.sql("INSERT INTO key_indices (id, index1, index2, index3, index4, index5, index6) VALUES (:id, :index1, :index2, :index3, :index4, :index5, :index6)").bind("id", existingObj.getId()).bind("index1", existingObj.getIndex1()).bind("index2", existingObj.getIndex2()).bind("index3", existingObj.getIndex3()).bind("index4", existingObj.getIndex4()).bind("index5", existingObj.getIndex5()).bind("index6", existingObj.getIndex6()).fetch().rowsUpdated().block();
    }

    @Test
    void testUpdateIfGreaterWhenGreater() {

        KeyIndices newObj = new KeyIndices(1L, 2, 2, 2, 2, 2, 2);

        Mono<KeyIndices> updatedObjMono = keyGeneratorService.updateIfGreater(newObj);

        StepVerifier.create(updatedObjMono).assertNext(updatedObj -> {
            assertThat(updatedObj.getId()).isEqualTo(1L);
            assertThat(updatedObj.getIndex1()).isEqualTo(2);
            assertThat(updatedObj.getIndex2()).isEqualTo(2);
            assertThat(updatedObj.getIndex3()).isEqualTo(2);
            assertThat(updatedObj.getIndex4()).isEqualTo(2);
            assertThat(updatedObj.getIndex5()).isEqualTo(2);
            assertThat(updatedObj.getIndex6()).isEqualTo(2);
        }).verifyComplete();
    }

    @Test
    void testUpdateIfGreaterWhenNotGreater() {


        KeyIndices newObj = new KeyIndices(1L, 0, 1, 1, 1, 1, 1);

        Mono<KeyIndices> updatedObjMono = keyGeneratorService.updateIfGreater(newObj);

        StepVerifier.create(updatedObjMono).expectError(GreaterIndicesFoundException.class).verify();
    }
}
