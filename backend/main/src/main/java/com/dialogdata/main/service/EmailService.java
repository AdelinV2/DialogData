package com.dialogdata.main.service;

import com.dialogdata.main.entity.PasswordReset;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final PasswordResetService passwordResetService;

    @Value("${EMAIL_USERNAME}")
    private String emailUsername;

    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(emailUsername);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Transactional
    public void sendPasswordResetEmail(String email) {

        PasswordReset passwordReset = passwordResetService.create(email);

        String resetLink = "http://localhost:3000/password-reset/" + passwordReset.getToken();

        String subject = "Password Reset Request";
        String body = "To reset your password, please click the following link:\n" + resetLink;

        sendEmail(email, subject, body);
    }
}
