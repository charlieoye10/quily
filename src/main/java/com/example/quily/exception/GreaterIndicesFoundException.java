package com.example.quily.exception;

public class GreaterIndicesFoundException extends RuntimeException{
    public GreaterIndicesFoundException(String message) {
        super(message);
    }
}