package com.tekravio.notification.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class TemplateResponse {

    private Long id;

    private String templateCode;

    private Integer version;

    private boolean active;

    private List<TemplateVariantResponse> variants;
}