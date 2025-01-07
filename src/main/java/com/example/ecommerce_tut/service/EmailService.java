package com.example.ecommerce_tut.service;

import com.example.ecommerce_tut.model.Order;
import com.example.ecommerce_tut.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("spring.mail.username")
    private String from;

    public void sendOrderConfirmationMail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(order.getAddress());
        message.setSubject("Order Confirmation");
        message.setText("Your order has been placed successfully.");

        mailSender.send(message);
    }
    public void sendConfirmationCode(User user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(user.getEmail());
        message.setSubject("Confirm your email");
        message.setText("Please confirm your email by entering this code " + user.getConfirmationCode());

        mailSender.send(message);
    }

}
