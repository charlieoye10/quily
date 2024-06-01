package com.example.quily.response;

public class SignUpResponse {
    public String email;

    public String creationTime;

    public String message;

    public SignUpResponse(String email, String creationTime, String message) {
        this.email = email;
        this.creationTime = creationTime;
        this.message = message;
    }
}
