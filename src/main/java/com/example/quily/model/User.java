package com.example.quily.model;

import com.example.quily.response.SignUpResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Table(name = "users")
public class User {
    @Id
    private String email;
    private String userName;
    private String password;

    public User(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
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

    public SignUpResponse toSignUpResponse() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma 'on' dd MMMM yyyy", Locale.ENGLISH);
        return new SignUpResponse(email, LocalDateTime.now().format(formatter));
    }
}
