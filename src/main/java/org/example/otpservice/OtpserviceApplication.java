package org.example.otpservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OtpserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtpserviceApplication.class, args);
    }

}
