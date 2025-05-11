package org.example.otpservice.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("email")
public class EmailNotificationChannel implements NotificationChannel {
    private final JavaMailSender mailSender;
    private final String from;

    public EmailNotificationChannel(JavaMailSender mailSender,
                                    @Value("${spring.mail.username}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void send(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject("your OTP-code");
        msg.setText("your agreement code: " + code);
        mailSender.send(msg);
    }
}
