package com.tekravio.notification.service.service;

import com.tekravio.notification.service.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationDispatcher dispatcher;

    @KafkaListener(topics = {"notifications.EMAIL", "notifications.SMS", "notifications.WHATSAPP", "notifications.PUSH"}, groupId = "notification-group")
    public void consume(NotificationRequest request) {
        dispatcher.dispatch(request);
    }
}