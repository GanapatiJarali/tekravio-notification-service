package com.tekravio.notification.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
//@EnableKafka
public class TekravioNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TekravioNotificationServiceApplication.class, args);
    }

}
