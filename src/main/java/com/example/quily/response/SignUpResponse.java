package com.example.quily.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignUpResponse {
    public String email;

    public String creationTime;

    public ResponseBody<SignUpResponse> toSuccessResponse() {
        return new ResponseBody<>(200, "Successfully registered with Quily", this);
    }
}
