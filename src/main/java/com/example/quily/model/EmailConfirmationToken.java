package com.example.quily.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;

@Table(name = "EmailConfirmationToken")
public class EmailConfirmationToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String confirmationToken;
	private String userEmail;
	private String createdTime;

	public EmailConfirmationToken() {}

	public EmailConfirmationToken(String confirmationToken, String createdTime, String userEmail) {
		this.confirmationToken = confirmationToken;
		this.createdTime = createdTime;
		this.userEmail = userEmail;
	}

	public Long getId() {
		return id;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public String getUserEmail() {
		return userEmail;
	}
}
