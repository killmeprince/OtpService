package org.example.otpservice.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.hamcrest.Matchers.matchesPattern;

@SpringBootTest
class TelegramChannelTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TelegramNotificationChannel telegramChannel;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void whenSend_thenRestTemplateCalledWithCorrectUrl() {
        String chatId = "12345";
        String code   = "654321";
        String regex = "https://api\\.telegram\\.org/bot.+?/sendMessage\\?chat_id=12345.*";
        mockServer.expect(requestTo(matchesPattern(regex))).andRespond(withSuccess("{\"ok\":true}", MediaType.APPLICATION_JSON));
        telegramChannel.send(chatId, code);
        mockServer.verify();
    }
}
