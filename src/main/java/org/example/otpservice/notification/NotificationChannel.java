package org.example.otpservice.notification;

public interface NotificationChannel {
    /**
     * @param destination (телефон, email, chatId, файл-путь)
     * @param code       само сообщение-код
     */
    void send(String destination, String code);
}