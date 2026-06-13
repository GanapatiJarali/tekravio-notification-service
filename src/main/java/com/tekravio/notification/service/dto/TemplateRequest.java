package com.tekravio.notification.service.dto;

import io.lettuce.core.protocol.CommandHandler;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class TemplateRequest {
    @NotBlank(message = "Template code mandatory")
    private String templateCode;
    private Long version;
    private List<VariantsRequest> variantsRequests;
}
