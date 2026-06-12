package com.tekravio.notification.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RecipientDetails {

    @Email
    private String email;

    @Pattern(regexp = "\\+?[1-9]\\d{1,14}") // E.164
    private String phoneNumber;

    @Size(min = 10, max = 200)
    private String deviceToken;
}