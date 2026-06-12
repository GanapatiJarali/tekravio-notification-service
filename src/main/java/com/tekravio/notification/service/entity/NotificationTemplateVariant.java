package com.tekravio.notification.service.entity;

import com.tekravio.notification.service.enumration.Channel;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notification_template_variant")
@Data
public class NotificationTemplateVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private NotificationTemplate template;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    private String subject;

    private String title;

    @Lob
    private String body;
}