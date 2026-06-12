package com.tekravio.notification.service.service;

import com.tekravio.notification.service.dto.NotificationRequest;
import com.tekravio.notification.service.entity.Notification;
import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Status;
import com.tekravio.notification.service.util.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Slf4j
public class PushNotificationSender implements NotificationSender {

    @Autowired
    private CommonService commonService;

    @Override
    public void send(NotificationRequest request) {
        Notification notification = commonService.fetchNotificationById(request.getNotificationId()).get();
        try {
            log.info("Push Notification send request: {}", request);
            notification.setStatus(Status.PROCESSING);
            //Firebase FCM api call
            notification.setStatus(Status.SENT);
            notification.setSentAt(LocalDateTime.now());
        } catch (Exception ex) {
            notification.setStatus(Status.FAILED);
            notification.setFailedAt(LocalDateTime.now());
        }
        commonService.saveNotification(notification);
    }

    @Override
    public Channel channel() {
        return Channel.PUSH;
    }
}
