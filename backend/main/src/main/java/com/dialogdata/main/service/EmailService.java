package com.dialogdata.main.service;

import com.dialogdata.main.entity.Order;
import com.dialogdata.main.entity.PasswordReset;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final PasswordResetService passwordResetService;
    private final ProductService productService;

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

    public void sendHtmlEmail(String to, String subject, String body) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(emailUsername);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

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

    public void sendOrderEmail(String email, Order order) throws MessagingException {

        String subject = "Order Confirmation";

        String body = "<h2>Order Confirmation</h2>" +
                "<p>Thank you for your order! Your order details are as follows:</p>" +
                "<p><strong>Order ID:</strong> " + order.getId() + "<br>" +
                "<strong>Total Amount:</strong> " + order.getTotalPrice() + "</p>" +
                "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse:collapse;'>" +
                "<tr><th>Name</th><th>Quantity</th><th>Price</th></tr>" +
                order.getCart().getCartEntries().stream()
                        .map(entry -> {
                            String name = productService.findProductById(entry.getProduct().getId()).getName();
                            return "<tr><td>" + name + "</td><td>" + entry.getQuantity() + "</td><td>" + entry.getTotalPricePerEntry() + "</td></tr>";
                        })
                        .collect(java.util.stream.Collectors.joining()) +
                "</table>" +
                "<p>Thank you for shopping with us! &#10084;</p>";

        sendHtmlEmail(email, subject, body);
    }
}
