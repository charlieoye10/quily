package com.example.quily.dao;

import com.example.quily.model.User;
import com.example.quily.request.SignUpRequest;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class UserDao {
    @Autowired
    PasswordEncoder passwordEncoder;

    public String emailToUsername(String email) {
        return email.substring(0, email.indexOf("@"));
    }

    public User signUpReqToUser(SignUpRequest req) {
        return new User(req.email, emailToUsername(req.email), passwordEncoder.encode(req.password), false);
    }

    public Boolean checkEmailPattern(String email) {
        return Pattern.compile(CommonUtil.emailPatternRegex)
                .matcher(email)
                .matches();
    }
}
