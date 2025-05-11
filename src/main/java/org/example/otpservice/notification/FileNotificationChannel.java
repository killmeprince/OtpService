package org.example.otpservice.notification;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

@Component("file")
public class FileNotificationChannel implements NotificationChannel {
    @Override
    public void send(String fileName, String code) {
        String line = Instant.now() + " OTP: " + code + System.lineSeparator();
        try {
            Files.write(Paths.get(fileName), line.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("File write failed", e);
        }
    }
}
