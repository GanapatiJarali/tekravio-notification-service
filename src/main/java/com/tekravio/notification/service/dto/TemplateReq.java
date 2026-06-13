package com.tekravio.notification.service.dto;

import com.tekravio.notification.service.enumration.Channel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TemplateReq {


    private Integer version;

    private Boolean active = true;

    @NotEmpty(message = "At least one template variant is required")
    private List<TemplateVariantRequest> variants;

    @Data
    public static class TemplateVariantRequest {

        @NotNull(message = "Channel is required")
        private Channel channel;


        private String subject;


        private String title;

        @NotBlank(message = "Body is required")
        private String body;


        private Map<String, Object> metadata;
    }
}