package com.example.quily.request;

import com.example.quily.dao.UserDao;
import com.example.quily.model.User;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SignUpRequest {
    public String email;
    public String password;
}
