package com.example.quily.model;

import com.example.quily.response.SignUpResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import com.example.quily.util.CommonUtil;

@Table(name = "users")
public class User {
    @Id
    private String email;
    private String userName;
    private String password;
    private boolean isActive;

    public User(String email, String userName, String password, Boolean isActive) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String userEmail) {
        this.email = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public SignUpResponse toSignUpResponse(String message) {
        return new SignUpResponse(email, CommonUtil.getCurrentDateTimeInFormat(), message);
    }
}
