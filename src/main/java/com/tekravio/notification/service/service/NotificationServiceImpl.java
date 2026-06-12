package com.tekravio.notification.service.service;

import com.tekravio.notification.service.common.BaseResponse;
import com.tekravio.notification.service.dto.NotificationRequest;
import com.tekravio.notification.service.dto.NotificationResponse;
import com.tekravio.notification.service.dto.PageResponse;
import com.tekravio.notification.service.entity.Notification;
import com.tekravio.notification.service.entity.NotificationRecipient;
import com.tekravio.notification.service.entity.NotificationTemplate;
import com.tekravio.notification.service.entity.repo.NotificationRecipientRepository;
import com.tekravio.notification.service.entity.repo.NotificationRepository;
import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Status;
import com.tekravio.notification.service.exception.ValidationException;
import com.tekravio.notification.service.mapper.NotificationMapper;
import com.tekravio.notification.service.util.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository notificationRecipientRepository;
    private final CommonService commonService;
    private final EncryptionService encryptionService;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationRecipientRepository notificationRecipientRepository, CommonService commonService, EncryptionService encryptionService, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
//        this.kafkaTemplate = kafkaTemplate;
        this.notificationRecipientRepository = notificationRecipientRepository;
        this.commonService = commonService;
        this.encryptionService = encryptionService;
        this.notificationMapper = notificationMapper;
    }


    @Override
    public BaseResponse sendNotification(NotificationRequest request) {
        NotificationRecipient notificationRecipient = commonService.fetchByNotificationRecipient(request.getRecipientId());
        Optional<Notification> notificationOptional = notificationRepository.findByNotificationId(request.getNotificationId());
        if (notificationOptional.isPresent()) {
            throw new ValidationException(2001, "Notification id duplicate ", "notification id duplicate");
        }
        Optional<NotificationTemplate> notificationTemplate = commonService.fetchByNotificationTemplateId(request.getTemplateId());
        if (notificationTemplate.isEmpty()) {
            throw new ValidationException(2002, "Notification template not found", "Notification template not found");
        }
        Notification notification = new Notification();
        notification.setNotificationId(request.getNotificationId());
        notification.setRecipient(notificationRecipient);
        notification.setChannel(request.getChannel());
        notification.setStatus(Status.PENDING);
        notification.setPriority(request.getPriority());
        notification.setTemplate(notificationTemplate.get());
        notification.setMessageContent(encryptionService.encrypt(request.getMessageContent()));
        notificationRepository.save(notification);
//        kafkaTemplate.send("notifications." + request.getChannel().name(), request);
        return BaseResponse.success(null);
    }

    @Override
    public BaseResponse<NotificationResponse> fetchNotification(String notificationId) {
        Notification notification = commonService.fetchNotificationById(notificationId).orElseThrow(() -> new ValidationException(2002, "Notification not found", "notification not found"));
        NotificationResponse notificationResponse = notificationMapper.entityToResponse(notification);
        return BaseResponse.success(notificationResponse);
    }

    @Override
    public BaseResponse<NotificationResponse> retryNotification(String notificationId) {
        Notification notification = commonService.fetchNotificationById(notificationId).orElseThrow(() -> new ValidationException(2002, "Notification not found", "notification not found"));
        if (notification.getStatus().equals(Status.SENT)) {
            throw new ValidationException(2020, "Notification in DB", "Notification in DB");
        }
        NotificationResponse notificationResponse = notificationMapper.entityToResponse(notification);
        return BaseResponse.success(notificationResponse);
    }

    @Override
    public BaseResponse<PageResponse<NotificationResponse>> notificationHistory(Long recipientId, Channel channel, Status status, int page, int size) {
        size = Math.min(size, 100);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notifications = notificationRepository.findNotifications(recipientId, channel, status, pageable);
        PageResponse<NotificationResponse> notificationResponse = new PageResponse<>();
        notificationResponse.setData(notifications.getContent().stream().map(notificationMapper::entityToResponse).collect(Collectors.toList()));
        notificationResponse.setTotalElements(notificationResponse.getTotalElements());
        notificationResponse.setTotalPages(notificationResponse.getTotalPages());
        return BaseResponse.success(notificationResponse);
    }
}
