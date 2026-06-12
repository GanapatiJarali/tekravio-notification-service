package com.tekravio.notification.service.util;

import com.tekravio.notification.service.entity.Notification;
import com.tekravio.notification.service.entity.NotificationRecipient;
import com.tekravio.notification.service.entity.NotificationTemplate;
import com.tekravio.notification.service.entity.NotificationTemplateVariant;
import com.tekravio.notification.service.entity.repo.NotificationRecipientRepository;
import com.tekravio.notification.service.entity.repo.NotificationRepository;
import com.tekravio.notification.service.entity.repo.NotificationTemplateRepository;
import com.tekravio.notification.service.entity.repo.NotificationTemplateVariantRepo;
import com.tekravio.notification.service.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommonService {
    private final NotificationRepository notificationRepository;
    private final NotificationRecipientRepository notificationRecipientRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationTemplateVariantRepo notificationTemplateVariantRepo;

    public CommonService(NotificationRepository notificationRepository, NotificationRecipientRepository notificationRecipientRepository, NotificationTemplateRepository templateRepository, NotificationTemplateRepository notificationTemplateRepository, NotificationTemplateVariantRepo notificationTemplateVariantRepo) {
        this.notificationRepository = notificationRepository;
        this.notificationRecipientRepository = notificationRecipientRepository;
        this.templateRepository = templateRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationTemplateVariantRepo = notificationTemplateVariantRepo;
    }

    public NotificationRecipient fetchByNotificationRecipient(Long id) {
        return notificationRecipientRepository.findById(id).orElseThrow(() -> new ValidationException(200, "Notification reciepeint not found.", "Notification reciepeint not found."));
    }

    public Optional<Notification> fetchNotificationById(String notificationId) {
        return notificationRepository.findByNotificationId(notificationId);
    }

    public Optional<NotificationTemplate> fetchByNotificationTemplateId(Long id) {
        return templateRepository.findById(id);
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public Optional<NotificationTemplate> findByTemplate(String templateCode) {
        return templateRepository.findByTemplateCode(templateCode);
    }

    public NotificationTemplate saveTemplate(NotificationTemplate templateCode) {
        return templateRepository.save(templateCode);
    }

    public void saveTemplateVariantAll(List<NotificationTemplateVariant> notificationTemplateVariants) {
        notificationTemplateVariantRepo.saveAll(notificationTemplateVariants);
    }

}
