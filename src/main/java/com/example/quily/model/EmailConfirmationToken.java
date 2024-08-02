package com.example.quily.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.ZonedDateTime;


@RequiredArgsConstructor
@Data
@Table(name = "EmailConfirmationToken")
public class EmailConfirmationToken {
   @Id
   private Long id;
   @NonNull
   private String confirmationToken;
   @NonNull
   private String userEmail;
   private ZonedDateTime updateTime;

   public EmailConfirmationToken(@NonNull String confirmationToken, @NonNull String userEmail, @NonNull ZonedDateTime updateTime) {
      this.confirmationToken = confirmationToken;
      this.userEmail = userEmail;
      this.updateTime = updateTime;
   }
}
