package com.tekravio.notification.service.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private int errorCode;
    private String errorDescription;
}
