package com.tekravio.notification.service.service;

import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.dto.NotificationRequest;
import com.tekravio.notification.service.dto.NotificationResponse;
import com.tekravio.notification.service.dto.PageResponse;
import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Status;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.PublicKey;
import java.time.LocalDateTime;


public interface NotificationService {
    public BaseResponse sendNotification(NotificationRequest request);

    BaseResponse<NotificationResponse> fetchNotification(String notificationId);

    BaseResponse<NotificationResponse> retryNotification(String notificationId);


    BaseResponse<PageResponse<NotificationResponse>> notificationHistory(Long recipientId, Channel channel, Status status, int page, int size);
}
