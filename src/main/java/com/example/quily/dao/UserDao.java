package com.example.quily.dao;

import com.example.quily.model.User;
import com.example.quily.request.SignUpRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDao {
//    @Autowired
//    PasswordEncoder passwordEncoder;

    public String emailToUsername(String email) {
        if (email != null && email.endsWith("@gmail.com")) {
            return email.substring(0, email.indexOf("@"));
        }
        return null;
    }

    public User signUpReqToUser(SignUpRequest req) {
        return new User(req.email, emailToUsername(req.email), req.password);
    }
}
