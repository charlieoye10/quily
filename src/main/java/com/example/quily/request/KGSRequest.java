package com.example.quily.request;

import com.example.quily.model.KeyIndices;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KGSRequest {

   private Long  id;

   private int index1;

   private int index2;

   private int index3;

   private int index4;

   private int index5;

   private int index6;

   public KeyIndices toKeyIndices() {
      return new KeyIndices(id, index1, index2, index3, index4, index5, index6);
   }
}


