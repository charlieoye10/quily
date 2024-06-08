package com.example.quily.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String email;
    private String userName;
    private String password;
    private boolean isActive;
}
