package com.example.quily.dao;

import com.example.quily.exception.EmailFormatException;
import com.example.quily.model.User;
import com.example.quily.request.SignUpRequest;
import com.example.quily.services.UserService;
import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Component
public class UserDao {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

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

    public Mono<String> emailVerification(SignUpRequest req) {
        if (!checkEmailPattern(req.email)) {
            return Mono.error(new EmailFormatException("Email format is not considerable," +
                    " Please provide email with correct format eg. `user_name@(gmail or domain_name).com`"));
        }
        return userService.sendEmailVerificationLink(signUpReqToUser(req));
    }
}
