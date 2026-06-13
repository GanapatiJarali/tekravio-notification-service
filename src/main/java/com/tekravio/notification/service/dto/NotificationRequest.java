package com.tekravio.notification.service.dto;

import com.tekravio.notification.service.enumration.Channel;
import com.tekravio.notification.service.enumration.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class NotificationRequest {

    @NotNull(message = "Notification id Mandatory")
    private String notificationId; // idempotency key (UUID)

    @NotBlank
    private Long recipientId;

    @NotNull
    private RecipientDetails recipient;

    @NotNull
    private Channel channel; // EMAIL, SMS, PUSH, WHATSAPP

    private String subject; // optional

    @NotBlank
    private String messageContent;

    private Long templateId;

    private Map<String, String> templateVariables;

    @NotNull
    private Priority priority;

    private Map<String, Object> metadata;

}