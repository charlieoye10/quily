package com.example.quily.exception;

public class InternalServerError extends RuntimeException {
   public InternalServerError(String errorMessage) { super(errorMessage); }
}
