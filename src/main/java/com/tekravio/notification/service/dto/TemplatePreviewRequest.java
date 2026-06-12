package com.tekravio.notification.service.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TemplatePreviewRequest {
    private Map<String, Object> templateVariables;
}
