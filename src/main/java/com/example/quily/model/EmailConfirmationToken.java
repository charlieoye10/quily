package com.example.quily.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@RequiredArgsConstructor
@Table(name = "EmailConfirmationToken")
public class EmailConfirmationToken {
	@Id
	private Long id;
	@NonNull
	private String confirmationToken;
	@NonNull private String userEmail;
	@NonNull private String createdTime;
}
