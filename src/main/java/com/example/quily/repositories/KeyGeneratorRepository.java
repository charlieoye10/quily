package com.example.quily.repositories;

import com.example.quily.model.KeyIndices;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static com.example.quily.constants.ColumnNameConstants.*;
import static com.example.quily.constants.KeyGeneratorConstants.SqlQueryToCallUpdateProcedure;
import static com.example.quily.constants.KeyGeneratorConstants.SqlQueryToCallGetProcedure;

@Repository
public class KeyGeneratorRepository {
   private final DatabaseClient client;

   @Autowired
   public KeyGeneratorRepository(DatabaseClient client) {
      this.client = client;
   }

   public Mono<KeyIndices> getKeyIndices(Long id) {
      return client.sql(SqlQueryToCallGetProcedure)
         .bind(ID, id)
         .fetch()
         .first()
         .map(CommonUtil::parseKeyIndices);
   }

   public Mono<Long> updateKeyIndicesSafely(int id, int index1, int index2, int index3, int index4, int index5, int index6) {
      return client.sql(SqlQueryToCallUpdateProcedure)
         .bind(ID, id)
         .bind(INDEX1, index1)
         .bind(INDEX2, index2)
         .bind(INDEX3, index3)
         .bind(INDEX4, index4)
         .bind(INDEX5, index5)
         .bind(INDEX6, index6)
         .map(row -> row.get(ROW_UPDATED, Long.class))
         .one();
   }
}