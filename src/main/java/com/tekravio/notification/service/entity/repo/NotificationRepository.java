package com.tekravio.notification.service.entity.repo;

import com.tekravio.notification.service.entity.Notification;
import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByNotificationId(String notificationId);


    @Query("""
            SELECT n
            FROM Notification n
            WHERE n.recipient.id = :recipientId
               OR n.channel = :channel
               OR n.status = :status
            """)
    Page<Notification> findNotifications(
            Long recipientId,
            Channel channel,
            Status status,
            Pageable pageable);
}

