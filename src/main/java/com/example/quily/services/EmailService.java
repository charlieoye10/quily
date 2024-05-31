package com.example.quily.services;

import com.example.quily.model.User;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(User user, String token) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Complete Registration!");
		mailMessage.setText("To confirm your account, please click here : "
				+ ShortLinkUtil.emailVerificationUrl + "?token=" + token);
		javaMailSender.send(mailMessage);
	}
}
