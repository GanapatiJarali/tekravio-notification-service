package com.tekravio.notification.service.service;

import com.tekravio.notification.service.dto.NotificationRequest;
import com.tekravio.notification.service.enumration.Channel;

public interface NotificationSender {
    void send(NotificationRequest request);
    Channel channel();
}