package com.example.quily.exception;

public class AlreadyExistEntityException extends RuntimeException {
    public AlreadyExistEntityException(String message) {
        super(message);
    }
}