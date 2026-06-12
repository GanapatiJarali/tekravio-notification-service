package com.tekravio.notification.service.mapper;

import com.tekravio.notification.service.dto.NotificationResponse;
import com.tekravio.notification.service.entity.Notification;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse entityToResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationId(notification.getNotificationId());
        response.setRecipient(notification.getRecipient());
        response.setChannel(notification.getChannel());
        response.setStatus(notification.getStatus());
        response.setPriority(notification.getPriority());
        response.setMessageContent(notification.getMessageContent());
        response.setRetryCount(notification.getRetryCount());
        response.setMaxRetries(notification.getMaxRetries());
        response.setSentAt(notification.getSentAt());
        response.setFailedAt(notification.getFailedAt());
        response.setCreatedAt(notification.getCreatedAt());
        response.setUpdatedAt(notification.getUpdatedAt());
        return response;
    }
}
