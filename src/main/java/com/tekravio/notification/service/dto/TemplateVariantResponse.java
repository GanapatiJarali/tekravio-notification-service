package com.tekravio.notification.service.dto;

import com.tekravio.notification.service.enumration.Channel;
import lombok.Data;

@Data
public class TemplateVariantResponse {

    private Long id;

    private Channel channel;

    private String subject;

    private String title;

    private String body;
}