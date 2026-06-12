package com.tekravio.notification.service.service;

import com.tekravio.notification.service.dto.NotificationRequest;
import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
public class NotificationDispatcher {

    private final Map<Channel, NotificationSender> senderMap;

    public NotificationDispatcher(List<NotificationSender> senders) {

        this.senderMap = senders.stream()
                .collect(Collectors.toMap(
                        NotificationSender::channel,
                        notificationSender -> notificationSender
                ));
    }

    public void dispatch(NotificationRequest request) {
        NotificationSender notificationSender = senderMap.get(request.getChannel());
        if (notificationSender == null) {
            throw new ValidationException(1008, "Notification channel not found..!", "Notification channel not found");
        }
        notificationSender.send(request);
    }
}