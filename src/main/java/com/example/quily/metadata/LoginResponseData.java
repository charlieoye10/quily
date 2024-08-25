package com.example.quily.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseData {
    private String userName;
    private String email;
}
