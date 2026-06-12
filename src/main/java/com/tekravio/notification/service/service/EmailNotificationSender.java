package com.tekravio.notification.service.service;

import com.tekravio.notification.service.dto.NotificationRequest;
import com.tekravio.notification.service.entity.Notification;
import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Status;
import com.tekravio.notification.service.util.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class EmailNotificationSender implements NotificationSender {

    private final CommonService commonService;

    public EmailNotificationSender(CommonService commonService) {
        this.commonService = commonService;
    }

    public void send(NotificationRequest request) {
        Notification notification = commonService.fetchNotificationById(request.getNotificationId()).get();
        try {
            log.info("Email Notification send request: {}", request);
            notification.setStatus(Status.PROCESSING);
            //sendGrid api call
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
        return Channel.EMAIL;
    }
}