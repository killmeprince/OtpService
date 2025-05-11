package org.example.otpservice.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component("telegram")
public class TelegramNotificationChannel implements NotificationChannel {

    private final RestTemplate rest;
    @Value("${telegram.bot-token}")
    private String botToken;

    public TelegramNotificationChannel(RestTemplate rest) {
        this.rest = rest;
    }

    @Override
    public void send(String chatId, String code) {
        String text = "Ваш OTP-код: " + code;
        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                botToken,
                URLEncoder.encode(chatId, StandardCharsets.UTF_8),
                URLEncoder.encode(text, StandardCharsets.UTF_8)
        );
        rest.getForObject(url, String.class);
    }
}
