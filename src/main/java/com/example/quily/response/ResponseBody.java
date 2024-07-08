package com.example.quily.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBody<T> {
   private int statusCode;
   private String message;
   private T data;

   public ResponseBody(int statusCode, T data) {
      this.statusCode = statusCode;
      this.message = null;
      this.data = data;
   }
}