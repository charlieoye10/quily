package com.example.quily.dao;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.model.User;
import com.example.quily.services.KeyGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class EmailConfirmationTokenDAO {
   private final JavaMailSender javaMailSender;
   private final KeyGeneratorService keyGeneratorService;

   @Autowired
   public EmailConfirmationTokenDAO(JavaMailSender javaMailSender,
                                    KeyGeneratorService keyGeneratorService) {
      this.javaMailSender = javaMailSender;
      this.keyGeneratorService  = keyGeneratorService;
   }

   public void sendEmail(User user, String token, String subject, String text, String link) {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setTo(user.getEmail());
      mailMessage.setSubject(subject);
      mailMessage.setText(link + token);
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

   public Mono<KeyGeneratorService.KGSResponseDetail> getConfirmationToken() {
      return keyGeneratorService.getCurrentKey();
   }
}
