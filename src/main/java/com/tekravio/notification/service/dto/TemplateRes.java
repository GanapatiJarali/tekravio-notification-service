package com.tekravio.notification.service.dto;

import com.tekravio.notification.service.enumration.Channel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TemplateRes {

    private String templateCode;

    private Integer version;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    private Map<Channel, TemplateVariantResponse> variants;

    @Data
    public static class TemplateVariantResponse {

        private String subject;  // EMAIL only (nullable for others)

        private String title;    // PUSH only (nullable for others)

        private String body;

        private Channel channel;
    }
}

