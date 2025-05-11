package org.example.otpservice.notification;

import com.cloudhopper.smpp.*;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("sms")
public class SmsNotificationChannel implements NotificationChannel {
    private final SmppClient client;
    private final SmppSessionConfiguration config;

    public SmsNotificationChannel(
            @Value("${smpp.host}")       String host,
            @Value("${smpp.port}")       int port,
            @Value("${smpp.system-id}")  String systemId,
            @Value("${smpp.password}")   String password,
            @Value("${smpp.source-addr}") String sourceAddr
    ) {
        this.client = new DefaultSmppClient();
        this.config = new SmppSessionConfiguration();
        config.setHost(host);
        config.setPort(port);
        config.setSystemId(systemId);
        config.setPassword(password);
        config.setType(SmppBindType.TRANSCEIVER);
        // source bind address as TON/NPI + address
        config.setAddressRange(new Address((byte)0x03, (byte)0x00, sourceAddr));
    }

    @Override
    public void send(String to, String code) {
        config.setConnectTimeout(20_000);
        config.setRequestExpiryTimeout(10_000);

        Logger logger = LoggerFactory.getLogger(SmsNotificationChannel.class);
        SmppSessionHandler sessionHandler = new DefaultSmppSessionHandler(logger);

        com.cloudhopper.smpp.SmppSession session = null;
        try {
            session = client.bind(config, sessionHandler);

            SubmitSm pdu = new SubmitSm();
            pdu.setSourceAddress(config.getAddressRange());
            pdu.setDestAddress(new Address((byte)0x01, (byte)0x01, to));
            pdu.setShortMessage(("Ваш OTP-код: " + code).getBytes());

            session.submit(pdu, config.getRequestExpiryTimeout());

        } catch (SmppTimeoutException
                 | SmppChannelException
                 | UnrecoverablePduException
                 | RecoverablePduException
                 | InterruptedException e) {
            throw new RuntimeException("SMS send failed", e);

        } finally {
            if (session != null) {

                try { session.close(); } catch (Exception ignored) {}
            }
        }
    }
}
