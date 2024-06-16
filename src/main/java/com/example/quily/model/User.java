package com.example.quily.model;

import com.example.quily.response.SignUpResponse;
import com.example.quily.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @NonNull private String email;
    @NonNull private String userName;
    private String password;
    private boolean isActive;

    public SignUpResponse toSignUpResponse() {
        return new SignUpResponse(email, CommonUtil.getCurrentDateTimeInFormat());
    }
}
