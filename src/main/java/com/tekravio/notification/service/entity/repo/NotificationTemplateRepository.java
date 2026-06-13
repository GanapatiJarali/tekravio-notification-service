package com.tekravio.notification.service.entity.repo;

import com.tekravio.notification.service.entity.NotificationTemplate;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByTemplateCode(String templateCode);

    @Query("SELECT MAX(t.version) FROM NotificationTemplate t WHERE t.templateCode = :templateCode")
    Integer findMaxVersionByTemplateCode(@Param("templateCode") String templateCode);
}
