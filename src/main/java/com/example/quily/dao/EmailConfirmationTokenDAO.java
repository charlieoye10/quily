package com.example.quily.dao;

import com.example.quily.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EmailConfirmationTokenDAO {
   public static String generateMD5Hash(String input) {
      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         byte[] messageDigest = md.digest(input.getBytes());
         return convertByteArrayToHexString(messageDigest);
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException(e);
      }
   }

   private static String convertByteArrayToHexString(byte[] arrayBytes) {
      StringBuilder sb = new StringBuilder();
      for (byte b : arrayBytes) {
         sb.append(String.format("%02x", b));
      }
      return sb.toString();
   }

   public static String getConfirmationToken(User user) {
      return generateMD5Hash(user.getEmail());
   }
}
