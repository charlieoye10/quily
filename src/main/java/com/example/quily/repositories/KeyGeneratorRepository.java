package com.example.quily.repositories;

import com.example.quily.model.KeyIndices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public class KeyGeneratorRepository {
   private static final String COLUMN_ID = "id";
   private static final String COLUMN_INDEX1 = "index1";
   private static final String COLUMN_INDEX2 = "index2";
   private static final String COLUMN_INDEX3 = "index3";
   private static final String COLUMN_INDEX4 = "index4";
   private static final String COLUMN_INDEX5 = "index5";
   private static final String COLUMN_INDEX6 = "index6";
   private static final String sqlQueryToCallUpdateProcedure =
      "CALL update_key_indices_only_if_db_indices_is_smaller(:id, :index1, :index2, :index3, :index4, :index5, :index6)";
   private static final String sqlQueryToCallGetProcedure = "CALL get_key_indices_by_id(:id)";
   private final DatabaseClient client;

   @Autowired
   public KeyGeneratorRepository(DatabaseClient client) {
      this.client = client;
   }

   public Mono<KeyIndices> getKeyIndices(Long id) {
      return client.sql(sqlQueryToCallGetProcedure)
         .bind("id", id)
         .fetch()
         .first()
         .map(this::getKeyIndicesFromUpdateRow);
   }

   public Mono<Integer> updateKeyIndicesSafely(int id, int index1, int index2, int index3, int index4, int index5, int index6) {
      return client.sql(sqlQueryToCallUpdateProcedure)
         .bind("id", id)
         .bind("index1", index1)
         .bind("index2", index2)
         .bind("index3", index3)
         .bind("index4", index4)
         .bind("index5", index5)
         .bind("index6", index6)
         .map(row -> row.get("rows_updated", Integer.class))
         .one();
   }

   private KeyIndices getKeyIndicesFromUpdateRow(Map<String, Object> fetchedRow) {
      return new KeyIndices(
         (Long) (fetchedRow.get(COLUMN_ID)),
         (Integer) fetchedRow.get(COLUMN_INDEX1),
         (Integer) fetchedRow.get(COLUMN_INDEX2),
         (Integer) fetchedRow.get(COLUMN_INDEX3),
         (Integer) fetchedRow.get(COLUMN_INDEX4),
         (Integer) fetchedRow.get(COLUMN_INDEX5),
         (Integer) fetchedRow.get(COLUMN_INDEX6));
   }
}