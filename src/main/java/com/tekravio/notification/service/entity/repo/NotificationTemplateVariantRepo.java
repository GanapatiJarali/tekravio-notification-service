package com.tekravio.notification.service.entity.repo;

import com.tekravio.notification.service.entity.NotificationTemplate;
import com.tekravio.notification.service.entity.NotificationTemplateVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationTemplateVariantRepo extends JpaRepository<NotificationTemplateVariant, Long> {
    List<NotificationTemplateVariant> findByTemplate(NotificationTemplate notificationTemplate);
}
