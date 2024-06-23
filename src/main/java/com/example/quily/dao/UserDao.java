package com.example.quily.dao;

import com.example.quily.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserDao {
   public Boolean isEmailPatternCorrect(String email) {
      return Pattern.compile(CommonUtil.emailPatternRegex)
         .matcher(email)
         .matches();
   }
}
