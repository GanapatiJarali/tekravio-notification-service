package com.tekravio.notification.service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "ClientId Mandatory")
    private String clientId;
    @NotBlank(message = "ClientSecret Mandatory")
    private String clientSecret;
    @NotBlank(message = "Role can not be empty")
    private String role;//optional it has store in the db based on the clientId,clientSecret
}

