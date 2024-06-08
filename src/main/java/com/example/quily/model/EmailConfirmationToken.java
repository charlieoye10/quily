package com.example.quily.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
@Table(name = "EmailConfirmationToken")
public class EmailConfirmationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NonNull  private String confirmationToken;
	@NonNull private String userEmail;
	@NonNull private String createdTime;
}
