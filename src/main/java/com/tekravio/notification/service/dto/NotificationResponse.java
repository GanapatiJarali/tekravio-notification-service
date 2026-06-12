package com.tekravio.notification.service.dto;

import com.tekravio.notification.service.entity.NotificationRecipient;
import com.tekravio.notification.service.entity.NotificationTemplate;
import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Priority;
import com.tekravio.notification.service.enumration.Status;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private String notificationId;
    private NotificationRecipient recipient;
    private Channel channel;
    private Status status;
    private Priority priority;
    private String messageContent;
    private int retryCount;
    private int maxRetries;
    private LocalDateTime sentAt;
    private LocalDateTime failedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

