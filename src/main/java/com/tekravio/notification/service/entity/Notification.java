package com.tekravio.notification.service.entity;

import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Priority;
import com.tekravio.notification.service.enumration.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notificationId;
    @OneToOne
    @JoinColumn(name = "recipient_id")
    private NotificationRecipient recipient;
    @Enumerated(EnumType.STRING)
    private Channel channel;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @ManyToOne
    @JoinColumn(name = "template_id")
    private NotificationTemplate template;
    private String messageContent;
    private int retryCount;
    private int maxRetries;
    private LocalDateTime sentAt;
    private LocalDateTime failedAt;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
