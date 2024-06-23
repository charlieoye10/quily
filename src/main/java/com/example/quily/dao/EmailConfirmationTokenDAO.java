package com.example.quily.dao;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EmailConfirmationTokenDAO {
   private final JavaMailSender javaMailSender;

   @Autowired
   public EmailConfirmationTokenDAO(JavaMailSender javaMailSender) {
      this.javaMailSender = javaMailSender;
   }

   public void sendEmail(User user, String token) {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject("Complete Registration!");
      mailMessage.setText("To confirm your account, please click here : "
         + ShortLinkConstants.EMAIL_VERIFICATION_URL + token);
      javaMailSender.send(mailMessage);
   }

   public String generateMD5Hash(String input) {
      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         byte[] messageDigest = md.digest(input.getBytes());
         return convertByteArrayToHexString(messageDigest);
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException(e);
      }
   }

   private String convertByteArrayToHexString(byte[] arrayBytes) {
      StringBuilder sb = new StringBuilder();
      for (byte b : arrayBytes) {
         sb.append(String.format("%02x", b));
      }
      return sb.toString();
   }

   public String getConfirmationToken(User user) {
      return generateMD5Hash(user.getEmail());
   }
}
