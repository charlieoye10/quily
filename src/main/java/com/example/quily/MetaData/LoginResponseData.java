package com.example.quily.MetaData;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseData {
    private String userName;
    private String email;
}
