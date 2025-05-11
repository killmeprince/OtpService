package org.example.otpservice.service;

import org.example.otpservice.notification.NotificationChannel;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class NotificationService {
    private final Map<String, NotificationChannel> channels;

    public NotificationService(Map<String, NotificationChannel> channels) {
        this.channels = channels;
    }

    public void notify(String channelId, String destination, String code) {
        NotificationChannel chan = channels.get(channelId);
        if (chan == null) {
            throw new IllegalArgumentException("Unknown channel: " + channelId);
        }
        chan.send(destination, code);
    }
}
