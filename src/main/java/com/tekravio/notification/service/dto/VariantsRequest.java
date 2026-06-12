package com.tekravio.notification.service.dto;

import com.tekravio.notification.service.enumration.Channel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VariantsRequest {
    private Channel channel;
    @NotBlank(message = "Mandatory subject")
    private String subject;//Your order {{orderId}} is on its way
    @NotBlank(message = "Title Mandatory")
    private String title;
    @NotBlank(message = "Body Mandatory")
    private String body;//<h2>Hi {{customerName}}</h2>
}
